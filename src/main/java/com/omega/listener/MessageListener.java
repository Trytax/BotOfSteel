package com.omega.listener;

import com.omega.event.CommandExecutionEvent;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.guild.Property;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private static final String PARAMETERS_PATTERN = "(?:\\s+)*(\\S+)";
    private static final String NEW_PARAMETERS_PATTERN = "(\"[^\"]*\"|'[^']*'|[\\S]+)+";

    private static final Pattern PATTERN = Pattern.compile(PARAMETERS_PATTERN);
    private static final Pattern NEW_PATTERN = Pattern.compile(NEW_PARAMETERS_PATTERN);
    private final IDiscordClient client;

    public MessageListener(IDiscordClient client) {
        this.client = client;
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        if (message.getAuthor().isBot()) {
            return;
        }

        GuildContext guildContext = GuildManager.getInstance().getContext(event.getMessage().getGuild());
        String prefix;
        if (guildContext != null) {
            prefix = guildContext.getProperties().getProperty(Property.COMMAND_PREFIX, String.class);
        } else {
            prefix = (String) Property.COMMAND_PREFIX.getDefaultValue();
        }

        String content = message.getContent();
        if (content.startsWith(prefix) && content.length() > prefix.length()) { // Command received
            Matcher matcher = PATTERN.matcher(content);
            String commandName;
            if (matcher.find()) {
                commandName = matcher.group(1).substring(prefix.length()).toLowerCase();

                List<String> args = new ArrayList<>();
                while (matcher.find()) {
                    args.add(matcher.group(1));
                }

                CommandExecutionEvent commandEvent = new CommandExecutionEvent(message, commandName, message.getAuthor(), args);
                client.getDispatcher().dispatch(commandEvent);
            }
        } else if (content.startsWith("$")) {
            Matcher matcher = NEW_PATTERN.matcher(content);
            String commandName;
            if (matcher.find()) {
                commandName = matcher.group(1).substring(prefix.length()).toLowerCase();

                List<String> args = new ArrayList<>();
                while (matcher.find()) {
                    args.add(matcher.group(1));
                }

                CommandExecutionEvent commandEvent = new CommandExecutionEvent(message, commandName, message.getAuthor(), args);
                client.getDispatcher().dispatch(commandEvent);
            }
        }
    }

}
