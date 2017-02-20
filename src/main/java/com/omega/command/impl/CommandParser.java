package com.omega.command.impl;

import com.omega.util.StringUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    private static final String MENTIONS_REGEX = "<(?<prefix>(@[!&]?)|#)(?<id>\\d+)>";
    private static final Pattern MENTION_PATTERN = Pattern.compile(MENTIONS_REGEX);

    private final IGuild guild;

    public CommandParser(IGuild guild) {
        this.guild = guild;
    }

    public Object parseCommandArg(String arg) {
        Object parseFirstPass = StringUtils.parse(arg);
        if (!(parseFirstPass instanceof String)) {
            return parseFirstPass;
        } else {
            Matcher matcher = MENTION_PATTERN.matcher(arg);
            if (matcher.find()) {
                String prefix = matcher.group("prefix");
                String id = matcher.group("id");

                switch (prefix) {
                    case "@": // User
                        IUser user = guild.getUserByID(id);
                        if (user == null) {
                            throw new IllegalArgumentException("User id seems to be invalid");
                        }
                        return user;

                    case "@&": // Role
                        IRole role = guild.getRoleByID(id);
                        if (role == null) {
                            throw new IllegalArgumentException("Role id seems to be invalid");
                        }
                        return role;

                    case "#": // Channel
                        IChannel channel = guild.getChannelByID(id);
                        if (channel == null) {
                            throw new IllegalArgumentException("Channel id seems to be invalid");
                        }
                        return channel;
                }
            }

            // Definitely a string
            System.out.println("Pure string");
            char startChar = arg.charAt(0);
            if (startChar == '\'' || startChar == '"') {
                System.out.println("Start char = " + startChar);
                return arg.replaceAll("^\"|\"$", "");
            }

            return arg;
        }
    }
}
