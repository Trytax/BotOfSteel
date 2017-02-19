package com.omega.listener;

import com.omega.guild.GuildManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;
import sx.blah.discord.handle.obj.IGuild;

public class StateListener {

    private static Logger LOGGER = LoggerFactory.getLogger(StateListener.class);

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        LOGGER.info("Bot is connected and ready !");
    }

    @EventSubscriber
    public void onGuildJoin(GuildCreateEvent event) {
        IGuild guild = event.getGuild();
        LOGGER.info("Join guild {}", guild.getName());
        GuildManager.getInstance().getContext(guild);
    }

    @EventSubscriber
    public void onGuildLeave(GuildLeaveEvent event) {
        IGuild guild = event.getGuild();
        LOGGER.info("Leave guild {}", guild.getName());
        GuildManager.getInstance().getContext(guild).destroy();
    }

    @EventSubscriber
    public void onDisconnect(DisconnectedEvent event) {
        LOGGER.warn("Disconnected, reason : {}", event.getReason());
    }
}
