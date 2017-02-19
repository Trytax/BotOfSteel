package com.omega.audio.loader;

import com.omega.audio.Playlist;
import com.omega.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayPlaylistTrackLoader extends TrackLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayPlaylistTrackLoader.class);

    private final TrackScheduler scheduler;
    private boolean needSkip;

    public PlayPlaylistTrackLoader(AudioPlayerManager manager, TrackScheduler scheduler) {
        super(manager);

        this.scheduler = scheduler;
    }

    public void load(Playlist playlist) {
        stop();
        LOGGER.debug("All futures should be stopped");
        scheduler.clear();

        LOGGER.debug("Queue : {}", printQueue());
        if (scheduler.queueSize() > 0) {
            throw new IllegalStateException("Queue should be empty");
        }

        this.needSkip = scheduler.getPlayingTrack() != null;
        playlist.getTracks().forEach(track -> load(track.getSource()));
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        LOGGER.debug("Add track {} to queue", track.getInfo().title);
        scheduler.queue(track);
        if (needSkip) {
            needSkip = false;
            LOGGER.debug("Play next track {}", scheduler.getQueue().get(0));
            LOGGER.debug("Queue : {}", printQueue());
            scheduler.skip();
        }
    }

    private String printQueue() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        scheduler.getQueue().forEach(track -> sb.append(track.getInfo().title).append('\n'));
        sb.append(']');

        return sb.toString();
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {

    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException exception) {

    }
}
