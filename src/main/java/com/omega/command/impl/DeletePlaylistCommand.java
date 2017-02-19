package com.omega.command.impl;

import com.omega.audio.Playlist;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.PlaylistRepository;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "deletePlaylist", aliases = "dp")
public class DeletePlaylistCommand extends AbstractCommand {

    public DeletePlaylistCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Remove the specified playlist")
    public void deletePlaylist(String playlistName) {
        PlaylistRepository repository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        Playlist playlist = repository.findByName(playlistName);

        boolean canDelete = false;
        if (playlist != null) {
            Playlist.Privacy privacy = playlist.getPrivacy();
            if (privacy.equals(Playlist.Privacy.USER) && by.getID().equals(playlist.getUserId())) {
                canDelete = true;
            } else if (privacy.equals(Playlist.Privacy.GUILD)) {
                IGuild guild = by.getClient().getGuildByID(playlist.getGuildId());
                if (guild.getOwnerID().equals(by.getID())) {
                    canDelete = true;
                }
            }
        }

        if (canDelete) {
            repository.deleteByName(playlistName);
            SenderUtil.reply(message, "Deleted playlist " + playlistName);
        } else {
            SenderUtil.reply(message, "Playlist " + playlistName + " not found");
        }
    }
}
