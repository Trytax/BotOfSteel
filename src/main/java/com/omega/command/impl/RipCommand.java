package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.util.List;

@Command(name = "rip")
public class RipCommand extends AbstractCommand {

    public RipCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Print the list of banned users")
    public void ripCommand() {
        try {
            List<IUser> bannedUsers = message.getGuild().getBannedUsers();
            StringBuilder builder = new StringBuilder();
            builder.append("Rest In Peace").append('\n').append('\n');
            for (int i = 0; i < bannedUsers.size(); i++) {
                IUser bannedUser = bannedUsers.get(i);
                builder.append(":skull_crossbones: ").append(bannedUser.getName()).append(" :skull_crossbones:");

                if (i < bannedUsers.size()) {
                    builder.append('\n');
                }
            }

            SenderUtil.reply(message, builder.toString());
        } catch (RateLimitException e) {
            e.printStackTrace();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }
}
