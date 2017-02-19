package com.omega.command.impl;

import com.omega.audio.AudioPlayerManager;
import com.omega.audio.GuildAudioPlayer;
import com.omega.audio.Playlist;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.exception.PlaylistAlreadyExists;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "newPlaylist", aliases = "np")
public class PlaylistCreateCommand extends AbstractCommand {

    public PlaylistCreateCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Create a new private playlist")
    public void playlistCreateCommand(@Parameter(name = "playlistName") String playlistName) {
        createPlaylist(playlistName, Playlist.Privacy.USER);
    }

    @Signature(help = "Create a new playlist with the specified privacy (0 = private, 1 = public)")
    public void playlistCreateCommand(@Parameter(name = "playlistName") String playlistName, @Parameter(name = "privacy") Integer privacy) {
        Playlist.Privacy resolvedPrivacy = Playlist.Privacy.findById(privacy);
        if (resolvedPrivacy != null) {
            createPlaylist(playlistName, resolvedPrivacy);
        } else {
            SenderUtil.reply(message, "Wrong privacy");
        }
    }

    private void createPlaylist(String name, Playlist.Privacy privacy) {
        GuildAudioPlayer audioPlayer = AudioPlayerManager.getInstance().get(message.getGuild());
        try {
            audioPlayer.createPlaylist(name, privacy, message.getGuild(), by);
            SenderUtil.reply(message, "Added playlist " + name);
        } catch (PlaylistAlreadyExists e) {
            SenderUtil.reply(message, "Playlist " + name + " already exists");
        }
    }
}
