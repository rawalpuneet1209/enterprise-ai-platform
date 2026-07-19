package com.rawalpuneet.enterpriseai.domain;

public interface CardPort {
    CardStatus status(String customerId, String cardId);
    CardStatus freeze(String customerId, String cardId, String idempotencyKey);
    CardStatus replace(String customerId, String cardId, String idempotencyKey);

    record CardStatus(String cardId, String state, String message) { }
}

