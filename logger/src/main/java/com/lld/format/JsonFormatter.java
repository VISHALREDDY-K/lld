package com.lld.format;

import com.lld.LogRecord;

public class JsonFormatter implements Formatter {
    @Override
    public String format(LogRecord record) {
        return String.format("{\"timeStamp\": \"%s\", \"level\": \"%s\", \"threadName\": \"%s\", \"message\": \"%s\"}", 
                record.timeStamp(), 
                record.level(), 
                record.threadName(), 
                record.message());
    }
    
}
