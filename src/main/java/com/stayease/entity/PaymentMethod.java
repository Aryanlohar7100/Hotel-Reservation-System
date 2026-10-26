package com.stayease.entity;

public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    UPI("Instant UPI"),
    NET_BANKING("Net Banking"),
    PAY_AT_HOTEL("Pay at Hotel Counter");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
