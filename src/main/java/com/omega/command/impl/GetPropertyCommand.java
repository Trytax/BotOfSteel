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
import sx.blah.discord.util.MessageBuilder;

import java.util.Iterator;
import java.util.Map;

@Command(name = "get")
public class GetPropertyCommand extends AbstractCommand {

    public GetPropertyCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Get all properties")
    public void getProperty() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildProperties guildProperties = guildContext.getProperties();
        Map<String, Object> properties = guildProperties.getProperties();

        StringBuilder builder = new StringBuilder(512);
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getMarkdown());

        Iterator<Map.Entry<String, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            builder.append(entry.getKey()).append(" = ").append(entry.getValue());
            if (it.hasNext()) {
                builder.append('\n');
            }
        }

        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getReverseMarkdown());

        SenderUtil.sendPrivateMessage(by, builder.toString());
    }

    @Signature(help = "Get the property for the given property")
    public void getProperty(@Parameter(name = "property") String property) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildProperties properties = guildContext.getProperties();

        try {
            Object value = properties.getProperty(property);
            SenderUtil.sendPrivateMessage(by, String.format("Property %s = %s", property, value));
        } catch (PropertyNotFoundException e) {
            SenderUtil.sendPrivateMessage(by, "Property " + property + " not found");
        }
    }
}
