package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.exception.PropertyNotFoundException;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.guild.GuildProperties;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "set")
public class SetPropertyCommand extends AbstractCommand {

    public SetPropertyCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Set a bot property")
    public void setProperty(@Parameter(name = "property") String property, @Parameter(name = "value") String value) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildProperties properties = guildContext.getProperties();
        try {
            properties.setProperty(property, value);
            SenderUtil.reply(message, "Property " + property + " set to " + value);
        } catch (PropertyNotFoundException e) {
            SenderUtil.reply(message, "Property " + property + " not found");
        }
    }
}
