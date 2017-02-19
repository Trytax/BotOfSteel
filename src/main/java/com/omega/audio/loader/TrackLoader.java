package com.omega.audio.loader;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Future;

public abstract class TrackLoader {

    protected final AudioPlayerManager manager;
    private AudioLoadResultHandler callbackHandler;
    protected Deque<TrackLoaderExecutor> tasks;

    public TrackLoader(AudioPlayerManager manager) {
        this.manager = manager;
        this.tasks = new ArrayDeque<>();
    }

    public void setCallbackHandler(AudioLoadResultHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    public void load(String source) {
        this.tasks.add(new TrackLoaderExecutor(manager, this, source));
    }

    /**
     * Block until all loading tasks are stopped or finished if was running
     **/
    public void stop() {
        while (!tasks.isEmpty()) {
            TrackLoaderExecutor loaderExecutor = tasks.poll();
            loaderExecutor.cancel();
        }
    }

    private void trackLoadedInternal(AudioTrack track) {
        trackLoaded(track);

        if (callbackHandler != null) {
            callbackHandler.trackLoaded(track);
        }
    }

    private void playlistLoadedInternal(AudioPlaylist playlist) {
        playlistLoaded(playlist);

        if (callbackHandler != null) {
            callbackHandler.playlistLoaded(playlist);
        }
    }

    private void noMatchesInternal() {
        noMatches();

        if (callbackHandler != null) {
            callbackHandler.noMatches();
        }
    }

    private void loadFailedInternal(FriendlyException e) {
        loadFailed(e);

        if (callbackHandler != null) {
            callbackHandler.loadFailed(e);
        }
    }

    abstract void trackLoaded(AudioTrack track);

    abstract void playlistLoaded(AudioPlaylist playlist);

    abstract void noMatches();

    abstract void loadFailed(FriendlyException exception);

    private static class TrackLoaderExecutor {

        private final TrackLoader loader;
        private final CancellableAudioLoadResult handler;
        private Future<Void> future;

        public TrackLoaderExecutor(AudioPlayerManager manager, TrackLoader loader, String source) {
            this.loader = loader;
            this.handler = new CancellableAudioLoadResult(loader);
            this.future = manager.loadItem(source, handler);
        }

        public void cancel() {
            handler.cancel();
            future.cancel(false);
        }
    }

    private static class CancellableAudioLoadResult implements AudioLoadResultHandler {

        private final TrackLoader loader;
        private boolean cancelled;

        public CancellableAudioLoadResult(TrackLoader loader) {
            this.loader = loader;
        }

        public void cancel() {
            this.cancelled = true;
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            if (!cancelled) {
                loader.trackLoadedInternal(track);
            }
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            if (!cancelled) {
                loader.playlistLoadedInternal(playlist);
            }
        }

        @Override
        public void noMatches() {
            if (!cancelled) {
                loader.noMatchesInternal();
            }
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            if (!cancelled) {
                loader.loadFailedInternal(exception);
            }
        }
    }
}
