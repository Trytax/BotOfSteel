package com.omega.command;

import com.omega.command.impl.*;
import com.omega.event.CommandExecutionEvent;
import com.omega.util.CommandExtractHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CommandManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    private Map<String, Class<? extends AbstractCommand>> commands = new HashMap<>();

    private ExecutorService executorService;

    private CommandManager() {
        this.executorService = Executors.newCachedThreadPool();

        registerCommand(PingCommand.class);
        registerCommand(HelpCommand.class);
        registerCommand(RandomCommand.class);
        registerCommand(InviteCommand.class);
        registerCommand(JoinCommand.class);
        registerCommand(LeaveCommand.class);
        registerCommand(QueueCommand.class);
        registerCommand(PlaylistCreateCommand.class);
        registerCommand(PlaylistListCommand.class);
        registerCommand(AddToPlaylistCommand.class);
        registerCommand(PlayPlaylistCommand.class);
        registerCommand(DeletePlaylistCommand.class);
        registerCommand(PlayCommand.class);
        registerCommand(PauseCommand.class);
        registerCommand(SkipCommand.class);
        registerCommand(SetMusicChannelCommand.class);
        registerCommand(SetPropertyCommand.class);
        registerCommand(GetPropertyCommand.class);
        registerCommand(GetVoiceChannelInfoCommand.class);
        registerCommand(KickCommand.class);
        registerCommand(BanCommand.class);
        registerCommand(RipCommand.class);
        registerCommand(TrackCommand.class);
    }

    @EventSubscriber
    public void onCommandExecution(CommandExecutionEvent event) {
        executorService.execute(() -> commandExecution(event.getBy(), event.getMessage(), event.getCommand(), event.getArgs()));
    }

    private void commandExecution(IUser by, IMessage message, String commandName, List<String> args) {
        if (!commands.containsKey(commandName)) {
            return;
        }

        LOGGER.debug("Command : {}, Arguments : {}", commandName, args.toString());
        Class<? extends AbstractCommand> commandClass = commands.get(commandName);

        // Instantiate command
        AbstractCommand commandInstance;
        try {
            Constructor<? extends AbstractCommand> constructor = commandClass.getConstructor(IUser.class, IMessage.class);
            commandInstance = constructor.newInstance(by, message);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Command class " + commandClass.getName() + " need a (IUser, IMessage) constructor");
            return;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOGGER.error("Unable to create new instance for command " + commandClass.getName());
            return;
        }

        // Resolve string arguments to objects
        List<Object> castedArgs = new ArrayList<>(args.size());
        CommandParser parser = new CommandParser(message.getGuild());
        args.forEach(arg -> {
            Object parsedArg = parser.parseCommandArg(arg);
            castedArgs.add(parsedArg);
        });


        if (LOGGER.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            castedArgs.forEach(o -> sb.append(o.getClass().getSimpleName()).append(' '));

            LOGGER.debug("Search for signature {}", sb.toString());
        }

        // Find correct command signature method
        Method commandMethod = Arrays.stream(commandClass.getDeclaredMethods())
                .filter(method -> {
                    if (method.isAnnotationPresent(Signature.class)
                            && method.getParameterCount() == castedArgs.size()) { // Keep same arguments sized methods

                        if (method.getParameterCount() > 0) { // If command have arguments

                            Class<?>[] paramTypes = method.getParameterTypes();
                            IntStream intStream = IntStream.range(
                                    0, castedArgs.size()
                            )
                                    .filter(i -> {
                                        Class<?> paramType = paramTypes[i];
                                        Object castedArg = castedArgs.get(i);

                                        return paramType.equals(castedArg.getClass()) ||
                                                paramType.isAssignableFrom(castedArg.getClass());
                                    });

                            if (intStream.count() == castedArgs.size()) {
                                LOGGER.debug("Command method found");
                                return true;
                            }

                        } else {
                            return true;
                        }
                    }

                    return false;
                })
                .findFirst()
                .orElse(null);

        if (commandMethod != null) {
            try {
                LOGGER.debug("Invoke method {}", commandMethod);
                commandMethod.invoke(commandInstance, castedArgs.toArray());
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("Unable to invoke method " + commandMethod.toGenericString() + " of class " + commandClass.getName(), e);
            }
        }
    }

    private <T extends AbstractCommand> void registerCommand(Class<T> clazz) {
        CommandExtractHelper.CommandInfo commandInfo = CommandExtractHelper.getCommandInfo(clazz);
        if (commandInfo != null) {
            commands.put(commandInfo.getName().toLowerCase(), clazz);
            Arrays.stream(commandInfo.getAliases()).forEach(alias -> commands.put(alias.toLowerCase(), clazz));
        } else {
            throw new NullPointerException("No command annotation found for class " + clazz.getName());
        }

    }

    public Class<? extends AbstractCommand> getCommand(String commandName) {
        return commands.get(commandName);
    }

    public Map<String, Class<? extends AbstractCommand>> getCommands() {
        return commands;
    }

    public static CommandManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CommandManager INSTANCE = new CommandManager();
    }
}
