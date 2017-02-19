package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.List;

@Command(name = "getChannelInfo", aliases = "gci")
public class GetVoiceChannelInfoCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetVoiceChannelInfoCommand.class);

    public GetVoiceChannelInfoCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Get the current voice channel info")
    public void getChannelInfo() {
        IUser botUser = message.getClient().getOurUser();
        List<IVoiceChannel> voiceChannels = botUser.getConnectedVoiceChannels();
        IVoiceChannel connectedVoiceChannel = voiceChannels.stream()
                .filter(voiceChannel -> voiceChannel.getConnectedUsers().contains(botUser))
                .findFirst()
                .orElse(null);
        if (connectedVoiceChannel != null) {
            SenderUtil.reply(message, printChannelInfo(connectedVoiceChannel));
        } else {
            SenderUtil.reply(message, "Not connected to a voice channel");
        }
    }

    private String printChannelInfo(IVoiceChannel voiceChannel) {
        StringBuilder builder = new StringBuilder();
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getMarkdown())
                .append("Name : ").append(voiceChannel.getName()).append('\n')
                .append("Topic : ").append(voiceChannel.getTopic()).append('\n')
                .append("Creation date : ").append(voiceChannel.getCreationDate().toString()).append('\n')
                .append("User limits : ").append(voiceChannel.getUserLimit()).append('\n')
                .append("Connected users : ").append(voiceChannel.getConnectedUsers().size()).append('\n')
                .append("Bitrate : ").append(voiceChannel.getBitrate()).append('\n');

        String invitesCount = "N/A";
        try {
            invitesCount = String.valueOf(voiceChannel.getInvites().size());
        } catch (NullPointerException e) {
            invitesCount = "Need to fix NPE";
        } catch (DiscordException e) {
            LOGGER.warn("Unable to get invite count", e);
        } catch (RateLimitException e) {
            invitesCount = "Rate limit exceeded";
            LOGGER.warn("Unable to get invite count", e);
        } catch (MissingPermissionsException e) {
            invitesCount = "Permission needed";
            LOGGER.warn("Unable to get invite count", e);
        }
        builder.append("Current invites : ").append(invitesCount)
                .append(MessageBuilder.Styles.CODE_WITH_LANG.getReverseMarkdown());
        return builder.toString();
    }
}
