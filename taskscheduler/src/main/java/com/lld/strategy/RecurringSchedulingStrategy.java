package com.lld.strategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class RecurringSchedulingStrategy implements SchedulingStrategy{
    private final LocalDateTime startTime;
    private final Duration interval;

    public RecurringSchedulingStrategy(LocalDateTime startTime, Duration interval) {
        this.startTime = startTime;
        this.interval = interval;
    }

    @Override
    public Optional<LocalDateTime> getNextExecutionTime(LocalDateTime lastExecutionTime) {
        if(lastExecutionTime==null){
            return Optional.of(startTime);
        }
        LocalDateTime nextExecutionTime = lastExecutionTime.plus(interval);
        return Optional.of(nextExecutionTime);
    }
    
}
