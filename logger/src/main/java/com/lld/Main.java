package com.lld;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.Destination;
import com.lld.format.Formatter;
import com.lld.format.JsonFormatter;
import com.lld.format.PlainTextFormatter;
import com.sink.ConsoleSink;
import com.sink.FileSink;
import com.sink.Sink;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        
        Formatter jsonFormatter = new JsonFormatter();
        Formatter plainTextFormatter = new PlainTextFormatter();

        Sink consoleSink = new ConsoleSink();
        Sink fileSink = new FileSink("logs.txt");



        Destination consoleDestination = new Destination(
                LogLevel.DEBUG,
                jsonFormatter,
                consoleSink
        );

        Destination fileDestination = new Destination(
                LogLevel.INFO,
                plainTextFormatter,
                fileSink
        );

        Logger logger = new Logger(List.of(
                consoleDestination,
                fileDestination
        ));

        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 1; i <= 5; i++) {
            int threadNumber = i;

            executor.submit(() -> {
                logger.debug("Debug message from worker " + threadNumber);
                logger.info("Info message from worker " + threadNumber);
                logger.warn("Warning message from worker " + threadNumber);
                logger.error("Error message from worker " + threadNumber);
                logger.fatal("Fatal message from worker " + threadNumber);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}