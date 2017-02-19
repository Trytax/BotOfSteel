package com.omega.guild;


import com.omega.audio.GuildAudioPlayer;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.GuildPropertiesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;

import java.util.Arrays;

public class GuildContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildContext.class);

    private final IGuild guild;

    private GuildProperties properties;

    private GuildAudioPlayer audioPlayer;

    public GuildContext(IGuild guild) {
        this.guild = guild;

        GuildPropertiesRepository propertiesRepository = DatastoreManagerSingleton.getInstance().getRepository(GuildPropertiesRepository.class);
        this.properties = propertiesRepository.findByGuild(guild);
        if (properties != null) {
            LOGGER.debug("Saved properties found, {}", properties.toString());
        } else {
            LOGGER.debug("Loading default properties");
            this.properties = new GuildProperties(guild);
            initProperties();
        }
    }

    private void initProperties() {
        Arrays.stream(Property.values()).forEach(property -> properties.setProperty(property, property.getDefaultValue()));
    }

    public void destroy() {
        audioPlayer.cleanup();
    }

    public GuildProperties getProperties() {
        return properties;
    }

    public GuildAudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void setAudioPlayer(GuildAudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }
}
