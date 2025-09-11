package org.kodelabs.domain.common.mongo;

import org.bson.Document;
import org.bson.conversions.Bson;

public final class AggregationExprs {

    private AggregationExprs() {
    }

    public static Document elemMatch(Document document) {
        return new Document("$elemMatch", document);
    }

    public static Bson sortArray(String input, Bson sortBy) {
        return new Document("$sortArray",
                new Document("input", "$" + input)
                        .append("sortBy", sortBy));
    }

    public static Bson combineSorts(Bson... sorts) {
        Document doc = new Document();
        for (Bson s : sorts) {
            doc.putAll(s.toBsonDocument(Document.class, null));
        }
        return doc;
    }
}
