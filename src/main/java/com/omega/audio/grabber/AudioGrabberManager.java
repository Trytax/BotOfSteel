package com.omega.audio.grabber;

import com.omega.audio.AudioPlayerManager;
import com.omega.audio.UnsupportedAudioSourceException;
import com.omega.audio.grabber.youtubedl.YoutubeDLAudioGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AudioGrabberManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioGrabberManager.class);

    private List<AudioGrabber> grabbers = new ArrayList<>();

    protected AudioGrabberManager() {
        grabbers.add(new YoutubeDLAudioGrabber());
    }

    public static AudioGrabberManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void grab(String source, AudioGrabber.Listener listener) {
        try {
            AudioGrabber parser = grabbers.stream().filter(audioGrabber -> audioGrabber.valid(source)).findFirst().orElse(null);
            if (parser == null) {
                throw new UnsupportedAudioSourceException("The provided source is not supported (" + source + ")");
            }
            List<AudioPlayerManager.Track> tracks = parser.grab(source, listener);
            listener.onFinish(tracks);
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    private static class SingletonHolder {
        private static final AudioGrabberManager INSTANCE = new AudioGrabberManager();
    }
}
