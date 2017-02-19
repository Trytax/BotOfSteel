package com.omega.audio.loader;

import com.omega.audio.Playlist;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.PlaylistRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddToPlaylistTrackLoader extends TrackLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddToPlaylistTrackLoader.class);

    private final Playlist playlist;

    public AddToPlaylistTrackLoader(AudioPlayerManager manager, Playlist playlist) {
        super(manager);

        this.playlist = playlist;
    }

    @Override
    void trackLoaded(AudioTrack track) {
        LOGGER.info("Loaded track : {}", track.getInfo().title);
        playlist.addTrack(new com.omega.audio.AudioTrack(track));
        DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class).save(playlist);
    }

    @Override
    void playlistLoaded(AudioPlaylist playlist) {
        LOGGER.info("Loaded playlist : {} ({} tracks)", playlist.getName(), playlist.getTracks().size());

        playlist.getTracks().forEach(track -> {
            this.playlist.addTrack(new com.omega.audio.AudioTrack(track));
        });

        DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class).save(this.playlist);
    }

    @Override
    void noMatches() {
        LOGGER.info("No matches found");
    }

    @Override
    void loadFailed(FriendlyException exception) {
        LOGGER.warn("Source loading failed", exception);
    }
}
