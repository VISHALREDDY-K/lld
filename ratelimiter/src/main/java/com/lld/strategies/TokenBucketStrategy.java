package com.lld.strategies;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketStrategy implements RateLimitingStrategy {

    private final int capacity;
    private final double refillRate; // tokens per second
    private final Map<String, TokenBucket> buckets;

    public TokenBucketStrategy(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.buckets = new ConcurrentHashMap<>();
    }

    private static class TokenBucket {
        long lastRefillTimestamp;
        int currentTokens;

        public TokenBucket(int capacity, double refillRate) {
            this.currentTokens = capacity;
            this.lastRefillTimestamp = System.currentTimeMillis();
        }
    }

    @Override
    public boolean allowRequest(String userId) {
        TokenBucket bucket = buckets.computeIfAbsent(userId, k -> new TokenBucket(capacity, refillRate));
        synchronized (bucket) {
            refillTokens(bucket);
            if (bucket.currentTokens > 0) {
                bucket.currentTokens--;
                return true;
            }
            return false;
        }

    }

    private void refillTokens(TokenBucket bucket) {
        long now = System.currentTimeMillis();
        long timeElapsed = now - bucket.lastRefillTimestamp;
        if (timeElapsed > 0) {
            int tokensToAdd = (int) (timeElapsed * refillRate);
            bucket.currentTokens = Math.min(capacity, bucket.currentTokens + tokensToAdd);
            bucket.lastRefillTimestamp = now;
        }
    }

}
