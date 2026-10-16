package com.lld.format;

import com.lld.LogRecord;

public class PlainTextFormatter implements Formatter {
    @Override
    public String format(LogRecord record) {
        return String.format("[%s] [%s] [%s] %s", 
                record.timeStamp(), 
                record.level(), 
                record.threadName(), 
                record.message());
    }
}
