package com.lld.format;

import com.lld.LogRecord;

public interface Formatter {
    String format(LogRecord record);
}
