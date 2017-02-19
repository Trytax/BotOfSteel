package com.omega.util;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandExtractHelper {

    public static class CommandInfo {
        private final String name;
        private final String[] aliases;

        public CommandInfo(String name, String[] aliases) {
            this.name = name;
            this.aliases = aliases;
        }

        public String getName() {
            return name;
        }

        public String[] getAliases() {
            return aliases;
        }
    }

    public static class CommandSignatureInfo {
        private final String help;
        private final List<Parameter> parameters = new ArrayList<>();

        public CommandSignatureInfo(String help) {
            this.help = help;
        }

        public void addParameter(String name, Class type) {
            parameters.add(new Parameter(name, type));
        }

        public String getHelp() {
            return help;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public static class Parameter {
            private final String name;
            private final Class type;

            public Parameter(String name, Class type) {
                this.name = name;
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public Class getType() {
                return type;
            }
        }
    }

    public static CommandInfo getCommandInfo(Class<? extends AbstractCommand> commandType) {
        CommandInfo info = null;
        Command commandAnnot = commandType.getAnnotation(Command.class);
        if (commandAnnot != null) {
            String commandName = commandAnnot.name();
            String[] aliases = commandAnnot.aliases();

            info = new CommandInfo(commandName, aliases);
        }

        return info;
    }

    public static List<CommandSignatureInfo> getCommandSignatureInfos(Class<? extends AbstractCommand> commandType) {
        Method[] methods = commandType.getDeclaredMethods();
        List<CommandSignatureInfo> signatureInfos = new ArrayList<>();
        Arrays.stream(methods).filter(method -> method.isAnnotationPresent(Signature.class)).forEachOrdered(method -> {
            Signature sigAnnot = method.getAnnotation(Signature.class);
            CommandSignatureInfo info = new CommandSignatureInfo(sigAnnot.help());
            Parameter[] params = method.getParameters();
            Arrays.stream(params).forEachOrdered(parameter -> {
                String paramName;
                if (parameter.isAnnotationPresent(com.omega.command.Parameter.class)) {
                    com.omega.command.Parameter paramAnnot = parameter.getAnnotation(com.omega.command.Parameter.class);
                    paramName = paramAnnot.name();
                } else {
                    paramName = parameter.getName();
                }

                info.addParameter(paramName, parameter.getType());
            });
            signatureInfos.add(info);
        });

        return signatureInfos;
    }
}
