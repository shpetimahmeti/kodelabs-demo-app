package org.kodelabs.domain.common;

import org.bson.types.ObjectId;

import java.time.Instant;

public abstract class BaseEntity {

    public String _id;
    private boolean customGenerated = false;

    public Instant createdAt;
    public Instant updatedAt;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
        this.customGenerated = true;
    }

    public void generateId() {
        if (!customGenerated) {
            customGenerated = true;
            this._id = new ObjectId().toString();
        }
    }
}
