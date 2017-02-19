package com.omega.guild;

public enum Property {

    MUSIC_TEXT_CHANNEL("music_channel_text", String.class, null),
    MUSIC_VOICE_CHANNEL("music_channel_voice", String.class, null),
    MUSIC_QUEUE_LOOP("music_queue_loop", Boolean.class, false),
    MUSIC_QUEUE_SHUFFLE("music_queue_shuffle", Boolean.class, false),
    COMMAND_PREFIX("command_prefix", String.class, "!");

    private final String property;
    private final Class<?> type;
    private final Object defaultValue;

    Property(String property, Class<?> type, Object defaultValue) {
        this.property = property;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
