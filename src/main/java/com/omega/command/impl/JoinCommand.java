package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.List;

@Command(name = "join", aliases = "j")
public class JoinCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinCommand.class);

    public JoinCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "The bot will join your current voice channel")
    public void joinCommand() {
        List<IVoiceChannel> connectedVoiceChannels = by.getConnectedVoiceChannels();
        IVoiceChannel connectedVoiceChannel = connectedVoiceChannels.stream()
                .filter(voiceChannel -> voiceChannel.getGuild().getID().equals(message.getGuild().getID()))
                .findAny()
                .orElse(null);

        if (connectedVoiceChannel != null) {
            join(connectedVoiceChannel);
        } else {
            SenderUtil.reply(message, "You are not connected to a voice channel");
        }
    }

    @Signature(help = "Bot will join the specified voice channel")
    public void joinCommand(@Parameter(name = "voiceChannelName") String voiceChannelName) {
        final IGuild guild = message.getGuild();
        IVoiceChannel voiceChannel = guild.getVoiceChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(voiceChannelName)).findFirst().orElse(null);
        if (voiceChannel != null) {
            join(voiceChannel);
        } else {
            SenderUtil.reply(message, "I don't see voice channel with name " + voiceChannelName);
        }
    }

    private void join(IVoiceChannel voiceChannel) {
        String resultMessage;
        if (voiceChannel.getConnectedUsers().contains(by.getClient().getOurUser())) {
            resultMessage = "I'm already into this voice channel";
        } else if (voiceChannel.getUserLimit() > 0 && voiceChannel.getConnectedUsers().size() >= voiceChannel.getUserLimit()) {
            resultMessage = "Max users reached for voice channel " + voiceChannel.getName();
        } else {
            try {
                voiceChannel.join();
                resultMessage = "Joined voice channel " + voiceChannel.getName();
            } catch (MissingPermissionsException e) {
                resultMessage = "I don't have permissions to connect to voice channel " + voiceChannel.getName();
            }
        }

        SenderUtil.reply(message, resultMessage);
    }
}
