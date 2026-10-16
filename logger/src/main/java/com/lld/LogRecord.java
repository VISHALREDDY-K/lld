package com.lld;

import java.time.Instant;

public record LogRecord(Instant timeStamp, LogLevel level, String message, String threadName) {
    
}
