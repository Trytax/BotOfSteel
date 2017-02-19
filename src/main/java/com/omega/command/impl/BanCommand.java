package com.omega.command.impl;

import com.omega.BotManager;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

import java.util.List;

@Command(name = "ban")
public class BanCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(BanCommand.class);

    public BanCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Ban a motherfucker from his username")
    public void banCommand(@Parameter(name = "username") String username) {
        List<IUser> foundUsers = message.getGuild().getUsersByName(username);
        if (foundUsers.size() > 0) {
            IUser user = foundUsers.get(0);
            ban(user);
        } else {
            SenderUtil.reply(message, "User with username " + username + " not found");
        }
    }


    @Signature(help = "Ban a motherfucker from a mention")
    public void kickCommand(@Parameter(name = "user") IUser user) {
        ban(user);
    }

    private void ban(IUser user) {
        String reply;
        if (user.isBot()) {
            reply = "Nope :angry:";
        } else {
            IUser owner = BotManager.getInstance().getApplicationOwner();
            if (owner == null) {
                reply = "Unable to determine user";
            } else if (owner.getID().equals(user.getID())) {
                reply = "I won't ban my master :heart_eyes:";
            } else {
                reply = "Taste my ban hammer " + user.getName();
            }
        }

        SenderUtil.reply(message, reply);
    }
}
