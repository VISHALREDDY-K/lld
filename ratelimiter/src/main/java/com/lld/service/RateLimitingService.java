package com.lld.service;

import com.lld.strategies.RateLimitingStrategy;

public class RateLimitingService {
    private static volatile RateLimitingService instance;
    private RateLimitingStrategy strategy;

    private RateLimitingService(){

    }

    public RateLimitingStrategy getStrategy(){
        return strategy;
    }
    public void setStrategy(RateLimitingStrategy strategy){
        this.strategy = strategy;
    }

    public static RateLimitingService getInstance(){
        if(instance == null){
            synchronized(RateLimitingService.class){
                if(instance == null){
                    instance = new RateLimitingService();
                }
            }
        }
        return instance;
    }

    public void handleRequest(String userId){
        if(strategy.allowRequest(userId)){
            System.out.println("Request allowed for user: " + userId);
        }else{
            System.out.println("Request denied for user: " + userId);
        }
    }
}
