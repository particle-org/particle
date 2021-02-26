package com.particle.game.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadTrackTool {
    private static final Logger logger = LoggerFactory.getLogger(ThreadTrackTool.class);

    private static final String ErrorMessage = "Dump thread {} with reason {}.";

    public static void printCurrentThreadTrace(String reason) {
        Thread currentThread = Thread.currentThread();

        logger.error("Dump thread {}({}) with reason {}.", currentThread.getName(), currentThread.getId(), reason);
        try {
            StackTraceElement[] elements = currentThread.getStackTrace();
            for (StackTraceElement element : elements) {
                logger.error("{}.{}({};{})", element.getClassName(), element.getMethodName(), element.getFileName(), element.getLineNumber());
            }
        } catch (Exception e) {
            logger.error("Fail to dump thread stack@", e);
        }
    }
}
