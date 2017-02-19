package com.omega.audio.grabber.youtubedl;

import com.omega.audio.AudioPlayerManager;
import com.omega.audio.grabber.AudioGrabber;
import com.omega.audio.grabber.GrabberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class YoutubeDLAudioGrabber extends AudioGrabber {

    //--no-check-certificate -x -v -f "worstaudio[abr>=96]" --audio-format vorbis -o "D:/windows/Downloads/videos/%(title)s.%(ext)s" https://www.youtube.com/playlist?list=PLrfOyQA1v_yx0f5pzmhdzxim_Az8tnNsj

    private static final Logger LOGGER = LoggerFactory.getLogger(YoutubeDLAudioGrabber.class);

    public YoutubeDLAudioGrabber() {

    }

    @Override
    public boolean valid(String source) {
        return true;
    }

    @Override
    public List<AudioPlayerManager.Track> grab(String source, Listener listener) throws GrabberException {
        YoutubeDLExecutor executor = new YoutubeDLExecutor(source, listener);
        return executor.grab();
    }
}
