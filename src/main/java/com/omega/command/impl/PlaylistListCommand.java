package com.omega.command.impl;

import com.omega.audio.Playlist;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.PlaylistRepository;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.List;

@Command(name = "playlists", aliases = "pl")
public class PlaylistListCommand extends AbstractCommand {

    public PlaylistListCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Get the list of playlists")
    public void playlistListCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        StringBuilder builder = new StringBuilder();
        builder.append(MessageBuilder.Styles.CODE.getMarkdown()).append('\n');

        PlaylistRepository playlistRepository = DatastoreManagerSingleton.getInstance().getRepository(PlaylistRepository.class);
        List<Playlist> userPlaylists = playlistRepository.findByUserPrivacy(by);
        printPlaylistsCategory(builder, "Private", userPlaylists);
        builder.append('\n');

        List<Playlist> guildPlaylists = playlistRepository.findByGuildPrivacy(message.getGuild());
        printPlaylistsCategory(builder, "Guild", guildPlaylists);

        builder.append(MessageBuilder.Styles.CODE.getReverseMarkdown());

        SenderUtil.sendPrivateMessage(by, builder.toString());
    }

    @Signature(help = "Show playlist for the mentionned user")
    public void playlistListCommand(@Parameter(name = "user") IUser user) {
        SenderUtil.reply(message, "Not for you");
    }

    private void printPlaylistsCategory(StringBuilder builder, String categoryName, List<Playlist> playlists) {
        builder.append(categoryName).append(" :").append('\n').append('\n');
        if (!playlists.isEmpty()) {
            playlists.forEach(playlist -> builder.append(playlist.getName()).append('(').append(playlist.getSize()).append(")\n"));
        } else {
            builder.append("None\n");
        }
    }
}
