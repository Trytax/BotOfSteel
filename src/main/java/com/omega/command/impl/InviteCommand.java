package com.omega.command.impl;

import com.omega.BotManager;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.config.BotConfig;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.EnumSet;

@Command(name = "invite", aliases = "inv")
public class InviteCommand extends AbstractCommand {

    public InviteCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Get the bot invite link")
    public void inviteCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        IDiscordClient client = BotManager.getInstance().getClient();
        BotInviteBuilder builder = new BotInviteBuilder(client);
        String link = builder.withClientID(BotConfig.getInstance().getClientId())
                .withPermissions(EnumSet.of(Permissions.ADMINISTRATOR)).build();
        by.getOrCreatePMChannel().sendMessage("Invitation link : " + link);
    }
}
