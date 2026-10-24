package com.lld.strategies;

import java.util.Map;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SlidingWindowStrategy implements RateLimitingStrategy {
    private final int windowSize;
    private final int maxRequests;
    private final Map<String, Deque<Long>> requestTimestamps;

    public SlidingWindowStrategy(int windowSize, int maxRequests) {
        this.windowSize = windowSize;
        this.maxRequests = maxRequests;
        this.requestTimestamps = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        Deque<Long> timestamps = requestTimestamps.computeIfAbsent(userId, k -> new ConcurrentLinkedDeque<>());

        synchronized (timestamps) {
            while (!timestamps.isEmpty() && currentTime - timestamps.peekFirst() > windowSize) {
                timestamps.pollFirst();
            }

            if (timestamps.size() < maxRequests) {
                timestamps.offerLast(currentTime);
                return true;
            }
        }

        return false;
    }

}
