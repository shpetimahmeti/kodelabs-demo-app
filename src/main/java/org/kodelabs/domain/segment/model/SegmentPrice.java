package org.kodelabs.domain.segment.model;

public class SegmentPrice {
    private int amount;
    private String currency;

    public SegmentPrice() {}

    public SegmentPrice(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
