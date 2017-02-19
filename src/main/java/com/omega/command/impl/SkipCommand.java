package com.omega.command.impl;

import com.omega.BotManager;
import com.omega.audio.AudioPlayerManager;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

@Command(name = "skip")
public class SkipCommand extends AbstractCommand {

    public SkipCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Skip the current track")
    public void skipCommand() {
        if (canSkip()) {
            GuildAudioPlayer audioPlayer = AudioPlayerManager.getInstance().get(message.getGuild());
            audioPlayer.skip(1);
        }
    }

    @Signature(help = "Skip x tracks")
    public void skipCommand(@Parameter(name = "count") Integer count) {
        if (canSkip()) {
            GuildAudioPlayer audioPlayer = AudioPlayerManager.getInstance().get(message.getGuild());
            audioPlayer.skip(count);
        }
    }

    private boolean canSkip() {
        try {
            if (message.getGuild().getID().equals("127824775090405377")) {
                return BotManager.getInstance().getApplicationOwner().getID().equals(by.getID());
            } else {
                return true;
            }
        } catch (DiscordException e) {
            return false;
        }
    }
}
