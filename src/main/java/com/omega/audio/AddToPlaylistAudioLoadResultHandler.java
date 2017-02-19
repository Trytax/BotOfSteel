package com.omega.audio;

import com.omega.util.SenderUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.obj.IMessage;

public class AddToPlaylistAudioLoadResultHandler implements AudioLoadResultHandler {

    private final IMessage message;
    private final String playlistName;

    public AddToPlaylistAudioLoadResultHandler(IMessage message, String playlistName) {
        this.message = message;
        this.playlistName = playlistName;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        SenderUtil.reply(message, "Track " + track.getInfo().title + " added to playlist " + playlistName);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        SenderUtil.reply(message, "Playlist " + playlist.getName() + " (" + playlist.getTracks().size() + " tracks) added to playlist " + playlistName);
    }

    @Override
    public void noMatches() {
        SenderUtil.reply(message, "The provided source may not be supported");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        SenderUtil.reply(message, "Track load failed");
    }
}
