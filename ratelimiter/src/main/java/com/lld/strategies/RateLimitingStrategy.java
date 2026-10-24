package com.lld.strategies;

public interface RateLimitingStrategy {
    boolean allowRequest(String userId);
}
