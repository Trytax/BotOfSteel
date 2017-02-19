package com.omega.guild;

import sx.blah.discord.handle.obj.IGuild;

import java.util.HashMap;
import java.util.Map;

public class GuildManager {

    private final Map<String, GuildContext> guilds = new HashMap<>();

    private GuildManager() {
    }

    public static GuildManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public GuildContext getContext(IGuild guild) {
        if (guild == null) {
            return null;
        }

        if (guilds.containsKey(guild.getID())) {
            return guilds.get(guild.getID());
        } else {
            GuildContext context = new GuildContext(guild);
            guilds.put(guild.getID(), context);

            return context;
        }
    }

    private static class SingletonHolder {
        private static final GuildManager INSTANCE = new GuildManager();
    }
}
