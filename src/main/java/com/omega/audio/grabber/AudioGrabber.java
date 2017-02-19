package com.omega.audio.grabber;

import com.omega.audio.AudioPlayerManager;

import java.util.List;

public abstract class AudioGrabber {

    /**
     * @return true if the source must be handled by this grabber, else otherwise
     */
    public abstract boolean valid(String source);

    public abstract List<AudioPlayerManager.Track> grab(String source, Listener listener) throws GrabberException;

    public interface Listener {

        void onStart();

        void onProgress(float progress, int current, int total);

        void onFinish(List<AudioPlayerManager.Track> tracks);

        void onError(Exception e);
    }
}
