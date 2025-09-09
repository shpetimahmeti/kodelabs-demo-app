package org.kodelabs.domain.common.repository;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Sorts;
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
import org.kodelabs.domain.common.dto.PaginationFacetResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public abstract class BaseRepository<T> {

    //Inject base entity collection
    protected ReactiveMongoCollection<T> collection;
    protected Class<T> entityClass;

    protected CodecRegistry pojoRegistry;
    protected Codec<T> codec;
    protected Function<Document, T> codecMapper;

    protected BaseRepository() {
    }

    protected BaseRepository(ReactiveMongoCollection<T> collection, Class<T> entityClass) {
        this.collection = collection;
        this.entityClass = entityClass;

        this.pojoRegistry = fromRegistries(
                collection.getCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        this.codec = pojoRegistry.get(entityClass);
        this.codecMapper = this::mapWithCodec;
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
        totalCountPipeline.add(Aggregates.count("count"));

        List<Bson> pipeline = List.of(
                Aggregates.facet(
                        new Facet("items", itemsPipeline),
                        new Facet("totalCount", totalCountPipeline)
                ),
                Aggregates.project(
                        new Document("items", 1)
                                .append("totalCount", new Document("$arrayElemAt", List.of("$totalCount.count", 0)))
                )
        );

        Multi<Document> agg = Multi.createFrom()
                .publisher(collection.withDocumentClass(Document.class).aggregate(pipeline));

        return agg.collect().first()
                .onItem().ifNull().continueWith(new Document("items", Collections.emptyList()).append("totalCount", 0))
                .onItem().transform(doc -> {
                    List<Document> docs = doc.getList("items", Document.class, Collections.emptyList());
                    List<T> items = docs.stream().map(codecMapper).collect(Collectors.toList());

                    PaginationFacetResult<T> result = new PaginationFacetResult<>();
                    result.setItems(items);
                    result.setTotalCount(extractCount(doc, "totalCount"));
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
        if (copy.containsKey("id") && !copy.containsKey("_id")) {
            copy.put("_id", copy.get("id"));
        }
        BsonDocument bsonDocument = copy.toBsonDocument(BsonDocument.class, pojoRegistry);
        try (BsonDocumentReader reader = new BsonDocumentReader(bsonDocument)) {
            return codec.decode(reader, DecoderContext.builder().build());
        }
    }
}
