package com.sink;

import java.io.FileWriter;

public class FileSink implements Sink {

    private final FileWriter fileWriter;
    
    public FileSink(String filePath) {
        try {
            this.fileWriter = new FileWriter(filePath, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create FileSink: " + e.getMessage(), e);
        }
    }

    @Override
    public void write(String message) {
        try {
            fileWriter.write(message + System.lineSeparator());
            fileWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to write to file: " + e.getMessage(), e);
        }
    }
    
}
