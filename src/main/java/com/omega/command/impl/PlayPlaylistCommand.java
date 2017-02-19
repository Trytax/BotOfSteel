package com.omega.command.impl;

import com.omega.audio.AudioPlayerManager;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.exception.PlaylistNotFoundException;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "playPlaylist", aliases = "pp")
public class PlayPlaylistCommand extends AbstractCommand {

    public PlayPlaylistCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Clear the queue and add the specified playlist to queue")
    public void playCommand(@Parameter(name = "playlistName") String playlistName) {
        GuildAudioPlayer audioPlayer = AudioPlayerManager.getInstance().get(message.getGuild());

        try {
            audioPlayer.playPlaylist(playlistName);
        } catch (PlaylistNotFoundException e) {
            SenderUtil.reply(message, "Playlist " + playlistName + " not found");
        }
    }
}
