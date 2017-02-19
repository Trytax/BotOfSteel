package com.omega.command.impl;

import com.omega.command.*;
import com.omega.util.CommandExtractHelper;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.*;
import java.util.stream.IntStream;

@Command(name = "help", aliases = "h")
public class HelpCommand extends AbstractCommand {

    public HelpCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Get the list of available commands")
    public void helpCommand() throws RateLimitException, DiscordException, MissingPermissionsException {
        StringBuilder builder = new StringBuilder();
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getMarkdown()).append("Commands list : \n\n");

        Map<String, Class<? extends AbstractCommand>> commandMap = CommandManager.getInstance().getCommands();
        Collection<Class<? extends AbstractCommand>> commands = commandMap.values();
        Iterator<Class<? extends AbstractCommand>> it = commands.stream().iterator();
        while (it.hasNext()) {
            Class<? extends AbstractCommand> command = it.next();
            CommandExtractHelper.CommandInfo info = CommandExtractHelper.getCommandInfo(command);
            builder.append(info.getName());

            String[] aliases = info.getAliases();
            Iterator<String> aIt = Arrays.stream(aliases).iterator();
            while (aIt.hasNext()) {
                String alias = aIt.next();

                builder.append(", ").append(alias);
            }
            if(it.hasNext()) {
                builder.append('\n');
            }
        }
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getReverseMarkdown());

        SenderUtil.sendPrivateMessage(by, builder.toString());
    }

    @Signature(help = "Get help for the specified method")
    public void helpCommand(@Parameter(name = "commandName") String commandName) {
        Class<? extends AbstractCommand> commandClass = CommandManager.getInstance().getCommand(commandName.toLowerCase());
        CommandExtractHelper.CommandInfo commandInfo = CommandExtractHelper.getCommandInfo(commandClass);
        List<CommandExtractHelper.CommandSignatureInfo> signatureInfos = CommandExtractHelper.getCommandSignatureInfos(commandClass);

        StringBuilder builder = new StringBuilder();
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getMarkdown());

        IntStream.range(0, signatureInfos.size()).forEach(i -> {
            CommandExtractHelper.CommandSignatureInfo signatureInfo = signatureInfos.get(i);
            builder.append(commandInfo.getName()).append(' ');
            signatureInfo.getParameters().forEach(parameter ->
                    builder.append(parameter.getName())
                            .append('(')
                            .append(parameter.getType().getSimpleName())
                            .append(")")
            );
            builder.append(" - ")
                    .append(signatureInfo.getHelp());
            if (i < signatureInfos.size()) {
                builder.append('\n');
            }
        });

        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getReverseMarkdown());
        SenderUtil.sendPrivateMessage(by, builder.toString());
    }
}
