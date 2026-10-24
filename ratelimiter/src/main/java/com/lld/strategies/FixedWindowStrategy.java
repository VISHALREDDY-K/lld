package com.lld.strategies;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowStrategy implements RateLimitingStrategy {

    private final int maxRequests;
    private final long windowSize;
    private final Map<String, UserRequestInfo> userRequests;

    public FixedWindowStrategy(int maxRequests, long windowSize) {
        this.maxRequests = maxRequests;
        this.windowSize = windowSize;
        this.userRequests = new ConcurrentHashMap<>();
    }

    private static class UserRequestInfo {
        private long windowStart;
        private int requestCnt;

        public UserRequestInfo() {
            this.windowStart = System.currentTimeMillis();
            this.requestCnt = 0;
        }
    }

    @Override
    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        UserRequestInfo requestInfo = userRequests.computeIfAbsent(userId, k -> new UserRequestInfo());

        synchronized (requestInfo) {
            if (currentTime - requestInfo.windowStart > windowSize) {
                requestInfo.windowStart = currentTime;
                requestInfo.requestCnt = 0;
            }

            if (requestInfo.requestCnt < maxRequests) {
                requestInfo.requestCnt++;
                return true;
            }
            return false;
        }
    }
}
