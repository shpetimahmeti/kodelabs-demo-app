package org.kodelabs.domain.common.entity;

import org.bson.types.ObjectId;

import java.time.Instant;

public abstract class BaseEntity {

    public String _id;
    private boolean customGenerated = false;

    public Instant createdAt;
    public Instant updatedAt;

    public String get_id() {
        return _id;
    }

    public void set_id(String id) {
        this._id = id;
        this.customGenerated = true;
    }

    public void generateId() {
        if (!customGenerated) {
            customGenerated = true;
            this._id = new ObjectId().toString();
        }
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
