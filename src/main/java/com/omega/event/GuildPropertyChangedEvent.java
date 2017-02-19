package com.omega.event;

import com.omega.guild.Property;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IGuild;

public class GuildPropertyChangedEvent extends Event {

    private final IGuild guild;
    private final Property property;
    private final Object value;
    private final boolean init;

    public GuildPropertyChangedEvent(IGuild guild, Property property, Object value, boolean init) {
        this.guild = guild;
        this.property = property;
        this.value = value;
        this.init = init;
    }

    public GuildPropertyChangedEvent(IGuild guild, Property property, Object value) {
        this(guild, property, value, false);
    }

    public IGuild getGuild() {
        return guild;
    }

    public Property getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }

    public boolean isInit() {
        return init;
    }
}
