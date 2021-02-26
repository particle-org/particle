package com.particle.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class HiveLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(HiveLogger.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String LOGGER_FORMATE = "[hytlog][{}][{}],{}";

    public static void log(String tag, String data) {
        LOGGER.info(LOGGER_FORMATE, DATE_FORMAT.format(System.currentTimeMillis()), tag, data);
    }

}
