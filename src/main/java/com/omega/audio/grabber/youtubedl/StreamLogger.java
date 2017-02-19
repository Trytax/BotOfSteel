package com.omega.audio.grabber.youtubedl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class StreamLogger extends StreamLineReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamLogger.class);

    private Level level;

    public StreamLogger(InputStream inputStream, Level level) {
        super(inputStream);

        this.level = level;
    }

    @Override
    protected void onLine(String line) {
        switch (level) {

            case DEBUG:
                LOGGER.debug(line);
                break;
            case INFO:
                LOGGER.info(line);
                break;
            case WARN:
                LOGGER.warn(line);
                break;
            case ERROR:
                LOGGER.error(line);
                break;
            default:
                LOGGER.warn("No log level set");
                break;
        }
    }

    public enum Level {
        DEBUG, INFO, WARN, ERROR;
    }
}
