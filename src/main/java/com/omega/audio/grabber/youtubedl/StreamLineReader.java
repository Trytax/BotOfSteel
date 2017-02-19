package com.omega.audio.grabber.youtubedl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class StreamLineReader extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamLineReader.class);

    private BufferedReader reader;

    public StreamLineReader(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));

        start();
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                onLine(line);
            }
        } catch (IOException e) {
            LOGGER.error("Error while reading input stream", e);
        }
    }

    protected abstract void onLine(String line);
}
