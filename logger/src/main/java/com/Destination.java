package com;

import java.util.concurrent.locks.ReentrantLock;

import com.lld.LogLevel;
import com.lld.LogRecord;
import com.lld.format.Formatter;
import com.sink.Sink;

public class Destination {
    private final LogLevel minLogLevel;
    private final Formatter formatter;
    private final Sink sink;
    private final ReentrantLock lock = new ReentrantLock();

    public Destination(LogLevel minLogLevel, Formatter formatter, Sink sink) {
        this.minLogLevel = minLogLevel;
        this.formatter = formatter;
        this.sink = sink;
    }

    public void write(LogRecord record) {
        if (record.level().ordinal() >= minLogLevel.ordinal()) {
            String formattedMessage = formatter.format(record);
            lock.lock();
            try {
                sink.write(formattedMessage);
            } finally {
                lock.unlock();
            }
        }
    }
    
}
