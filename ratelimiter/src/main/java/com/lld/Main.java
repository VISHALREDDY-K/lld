package com.lld;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.lld.service.RateLimitingService;
import com.lld.strategies.FixedWindowStrategy;
import com.lld.strategies.SlidingWindowStrategy;
import com.lld.strategies.TokenBucketStrategy;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("      RATE LIMITER TEST SUITE           ");
        System.out.println("========================================\n");

        RateLimitingService service = RateLimitingService.getInstance();

        // -------------------------------------------------------------------
        // Scenario 1: Fixed Window Strategy
        // -------------------------------------------------------------------
        System.out.println(">>> 1. TESTING FIXED WINDOW STRATEGY (Limit: 3 req / 1000ms)");
        service.setStrategy(new FixedWindowStrategy(3, 1000));

        System.out.println("Sending 3 immediate requests for user 'alice':");
        service.handleRequest("alice");
        service.handleRequest("alice");
        service.handleRequest("alice");

        System.out.println("Sending 4th request for 'alice' (should be DENIED):");
        service.handleRequest("alice");

        System.out.println("Testing user isolation - sending request for user 'bob' (should be ALLOWED):");
        service.handleRequest("bob");

        System.out.println("Sleeping 1100ms for window to reset...");
        Thread.sleep(1100);

        System.out.println("Sending request for 'alice' in new window (should be ALLOWED):");
        service.handleRequest("alice");

        System.out.println("\n------------------------------------------------------------\n");

        // -------------------------------------------------------------------
        // Scenario 2: Sliding Window Strategy
        // -------------------------------------------------------------------
        System.out.println(">>> 2. TESTING SLIDING WINDOW STRATEGY (Limit: 2 req / 1000ms)");
        service.setStrategy(new SlidingWindowStrategy(1000, 2));

        System.out.println("Sending 2 requests at t=0ms for user 'carol':");
        service.handleRequest("carol");
        service.handleRequest("carol");

        System.out.println("Sending 3rd request immediately (should be DENIED):");
        service.handleRequest("carol");

        System.out.println("Sleeping 600ms...");
        Thread.sleep(600);
        System.out.println("Sending request at t=600ms (still within 1000ms window, should be DENIED):");
        service.handleRequest("carol");

        System.out.println("Sleeping another 500ms (total elapsed ~1100ms, first 2 requests expired)...");
        Thread.sleep(500);

        System.out.println("Sending request after 1100ms (should be ALLOWED):");
        service.handleRequest("carol");

        System.out.println("\n------------------------------------------------------------\n");

        // -------------------------------------------------------------------
        // Scenario 3: Token Bucket Strategy
        // -------------------------------------------------------------------
        System.out.println(">>> 3. TESTING TOKEN BUCKET STRATEGY (Capacity: 3, Refill: 0.002 tokens/ms -> 2 tokens/sec)");
        service.setStrategy(new TokenBucketStrategy(3, 0.002));

        System.out.println("Sending 3 requests for user 'dave' (should consume all 3 tokens):");
        service.handleRequest("dave");
        service.handleRequest("dave");
        service.handleRequest("dave");

        System.out.println("Sending 4th request immediately (bucket empty, should be DENIED):");
        service.handleRequest("dave");

        System.out.println("Sleeping 600ms to allow 1 token to refill (600ms * 0.002 = 1.2 -> 1 token)...");
        Thread.sleep(600);

        System.out.println("Sending 5th request (1 token refilled, should be ALLOWED):");
        service.handleRequest("dave");

        System.out.println("Sending 6th request immediately (should be DENIED):");
        service.handleRequest("dave");

        System.out.println("\n------------------------------------------------------------\n");

        // -------------------------------------------------------------------
        // Scenario 4: Concurrency & Thread-Safety Test
        // -------------------------------------------------------------------
        System.out.println(">>> 4. TESTING CONCURRENCY & THREAD SAFETY");
        int concurrentLimit = 10;
        int totalThreads = 30;
        service.setStrategy(new SlidingWindowStrategy(2000, concurrentLimit));

        ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(totalThreads);

        AtomicInteger allowedCount = new AtomicInteger(0);
        AtomicInteger deniedCount = new AtomicInteger(0);

        for (int i = 0; i < totalThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    boolean allowed = service.getStrategy().allowRequest("concurrentUser");
                    if (allowed) {
                        allowedCount.incrementAndGet();
                    } else {
                        deniedCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        System.out.printf("Concurrent Results: Allowed = %d, Denied = %d (Expected Allowed = %d)%n",
                allowedCount.get(), deniedCount.get(), concurrentLimit);

        if (allowedCount.get() == concurrentLimit && deniedCount.get() == (totalThreads - concurrentLimit)) {
            System.out.println("✅ CONCURRENCY TEST PASSED: No race condition!");
        } else {
            System.out.println("❌ CONCURRENCY TEST FAILED!");
        }

        System.out.println("\n========================================");
        System.out.println("       ALL TESTS COMPLETED              ");
        System.out.println("========================================");
    }
}