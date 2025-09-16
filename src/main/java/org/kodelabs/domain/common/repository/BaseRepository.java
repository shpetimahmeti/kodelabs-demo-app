package org.kodelabs.domain.common.repository;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.ClientSession;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.kodelabs.domain.common.entity.BaseEntity;
import org.kodelabs.domain.common.mongo.MongoRegistry;
import org.kodelabs.domain.common.pagination.PaginationFacetResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.kodelabs.domain.common.mongo.Fields.ID;
import static org.kodelabs.domain.common.mongo.util.AggregationExprs.arrayElemAt;
import static org.kodelabs.domain.common.mongo.Fields.AggregationFacetResultFields.__COUNT;
import static org.kodelabs.domain.common.mongo.Fields.AggregationFacetResultFields.ITEMS;
import static org.kodelabs.domain.common.mongo.Fields.AggregationFacetResultFields.TOTAL_COUNT;
import static org.kodelabs.domain.common.mongo.Fields.AggregationFacetResultFields.TOTAL_COUNT__COUNT;
import static org.kodelabs.domain.common.mongo.Fields.UPDATED_AT;
import static org.kodelabs.domain.common.mongo.Fields.asFieldRef;

public abstract class BaseRepository<T extends BaseEntity> {

    private CodecRegistry pojoRegistry;
    private Codec<T> codec;
    private ReactiveMongoCollection<T> collection;

    protected BaseRepository() {
    }

    protected BaseRepository(MongoRegistry mongoRegistry, Class<T> entityClass) {
        this.collection = mongoRegistry.getCollection(entityClass);

        this.pojoRegistry = fromRegistries(
                collection.getCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        this.codec = pojoRegistry.get(entityClass);
    }

    public Uni<UpdateResult> updateOne(ClientSession session, T entity) {
        entity.setUpdatedAt(Instant.now());
        return collection.replaceOne(session, Filters.eq(ID, entity.get_id()), entity);
    }

    public Uni<UpdateResult> updateOne(ClientSession session, Bson filter, Bson update) {
        Bson updateWithTimestamp = Updates.combine(update, Updates.set(UPDATED_AT, Instant.now()));
        return collection.updateOne(session, filter, updateWithTimestamp);
    }

    public Uni<T> findOneAndUpdateWithOptions(Bson filter, Bson update, FindOneAndUpdateOptions options) {
        Bson updateWithTimestamp = Updates.combine(update, Updates.set(UPDATED_AT, Instant.now()));

        return collection.findOneAndUpdate(filter, updateWithTimestamp, options);
    }

    public Multi<T> find(Bson filter) {
        return collection.find(filter);
    }

    public <K> Multi<K> withDocumentClass(Class<K> documentClass, List<Bson> pipeline) {
        return collection.withDocumentClass(documentClass).aggregate(pipeline);
    }

    public Uni<InsertOneResult> insertOne(T entity) {
        return insertOne(null, entity);
    }

    public Uni<InsertOneResult> insertOne(ClientSession session, T entity) {
        Objects.requireNonNull(entity, "entity");

        Instant now = Instant.now();
        entity.generateId();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        if (session == null) {
            return collection.insertOne(entity);
        }

        return collection.insertOne(session, entity);
    }

    protected Uni<PaginationFacetResult<T>> loadPaginationFacetResult(
            int page,
            int size,
            String sortField,
            boolean ascending
    ) {
        return loadPaginationFacetResult(null, page, size, sortField, ascending);
    }

    protected Uni<PaginationFacetResult<T>> loadPaginationFacetResult(
            Bson matchFilter,
            int page,
            int size,
            String sortField,
            boolean ascending
    ) {
        List<Bson> itemsPipeline = new ArrayList<>();
        List<Bson> totalCountPipeline = new ArrayList<>();

        if (matchFilter != null) {
            itemsPipeline.add(Aggregates.match(matchFilter));
            totalCountPipeline.add(Aggregates.match(matchFilter));
        }

        itemsPipeline.add(Aggregates.sort(ascending ? Sorts.ascending(sortField) : Sorts.descending(sortField)));
        itemsPipeline.add(Aggregates.skip(page * size));
        itemsPipeline.add(Aggregates.limit(size));
        totalCountPipeline.add(Aggregates.count(__COUNT));

        List<Bson> pipeline = List.of(
                Aggregates.facet(
                        new Facet(ITEMS, itemsPipeline),
                        new Facet(TOTAL_COUNT, totalCountPipeline)
                ),
                Aggregates.project(
                        new Document(ITEMS, 1)
                                .append(TOTAL_COUNT, arrayElemAt(asFieldRef(TOTAL_COUNT__COUNT), 0))
                )
        );

        Multi<Document> agg = Multi.createFrom()
                .publisher(collection.withDocumentClass(Document.class).aggregate(pipeline));

        return agg.collect().first()
                .onItem().ifNull().continueWith(new Document(ITEMS, Collections.emptyList()).append(TOTAL_COUNT, 0))
                .onItem().transform(doc -> {
                    List<Document> docs = doc.getList(ITEMS, Document.class, Collections.emptyList());
                    List<T> items = docs.stream().map(this::mapWithCodec).collect(Collectors.toList());

                    PaginationFacetResult<T> result = new PaginationFacetResult<>();
                    result.setItems(items);
                    result.setTotalCount(extractCount(doc, TOTAL_COUNT));
                    return result;
                });
    }

    private static long extractCount(Document doc, String key) {
        Object value = doc.get(key);
        if (value == null) {
            return 0L;
        }

        if (!(value instanceof Number)) {
            throw new IllegalStateException(
                    "Expected numeric '" + key + "', but got: " + value.getClass().getSimpleName() + " = " + value
            );
        }
        return ((Number) value).longValue();
    }

    private T mapWithCodec(Document doc) {
        Document copy = new Document(doc);

        BsonDocument bsonDocument = copy.toBsonDocument(BsonDocument.class, pojoRegistry);
        try (BsonDocumentReader reader = new BsonDocumentReader(bsonDocument)) {
            return codec.decode(reader, DecoderContext.builder().build());
        }
    }
}
