package com.omega.command.impl;

import com.omega.audio.AddToPlaylistAudioLoadResultHandler;
import com.omega.audio.AudioPlayerManager;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.exception.PlaylistNotFoundException;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "addToPlaylist", aliases = {"atp"})
public class AddToPlaylistCommand extends AbstractCommand {

    public AddToPlaylistCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Add the source track(s) to the specified playlist")
    public void addToPlaylistCommand(String playlistName, String source) {
        GuildAudioPlayer audioPlayer = AudioPlayerManager.getInstance().get(message.getGuild());

        try {
            audioPlayer.addToPlaylist(playlistName, source,
                    new AddToPlaylistAudioLoadResultHandler(message, playlistName));
        } catch (PlaylistNotFoundException e) {
            SenderUtil.reply(message, "Playlist " + playlistName + " not found");
        }
    }
}
