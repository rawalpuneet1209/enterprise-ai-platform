package com.rawalpuneet.enterpriseai.banking.cards;

import com.rawalpuneet.enterpriseai.domain.CardPort;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryCardAdapter implements CardPort {
    private final ConcurrentHashMap<String, CardStatus> cards = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CardStatus> idempotentResults = new ConcurrentHashMap<>();

    @Override public CardStatus status(String customerId, String cardId) {
        return cards.getOrDefault(key(customerId, cardId), new CardStatus(cardId, "ACTIVE", "Card is active"));
    }
    @Override public CardStatus freeze(String customerId, String cardId, String idempotencyKey) {
        return idempotentResults.computeIfAbsent(idempotencyKey, ignored -> {
            var result = new CardStatus(cardId, "FROZEN", "Card frozen successfully");
            cards.put(key(customerId, cardId), result); return result;
        });
    }
    @Override public CardStatus replace(String customerId, String cardId, String idempotencyKey) {
        return idempotentResults.computeIfAbsent(idempotencyKey, ignored -> {
            var result = new CardStatus(cardId, "REPLACEMENT_ORDERED", "Replacement card ordered");
            cards.put(key(customerId, cardId), result); return result;
        });
    }
    private String key(String customerId, String cardId) { return customerId + ":" + cardId; }
}

