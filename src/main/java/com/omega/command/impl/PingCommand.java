package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.DiscordStatus;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

@Command(name = "ping", aliases = "pg")
public class PingCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingCommand.class);

    public PingCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Send a ping to the bot to know if he is online. Also print the daily API response time.")
    public void pingCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        String ping = null;
        try {
            DiscordStatus.getAPIResponseTimeForDay();
        } catch (Exception e) {
            LOGGER.warn("Unable to get API response time", e);
            ping = "N/A";
        }

        by.getOrCreatePMChannel().sendMessage("Pong - Today API response time : " + ping);
    }
}
