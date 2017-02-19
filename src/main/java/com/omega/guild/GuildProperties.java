package com.omega.guild;

import com.omega.BotManager;
import com.omega.StringUtils;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.GuildPropertiesRepository;
import com.omega.event.GuildPropertyChangedEvent;
import com.omega.exception.PropertyNotFoundException;
import de.caluga.morphium.annotations.*;
import de.caluga.morphium.annotations.lifecycle.PostLoad;
import de.caluga.morphium.driver.bson.MorphiumId;
import sx.blah.discord.handle.obj.IGuild;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Entity
@Index
//@PartialUpdate
public class GuildProperties {

    public enum Fields {
        id, guildId, properties
    }

    @Id
    private MorphiumId id;

    @Index
    private String guildId;

    @Transient
    private IGuild guild;

    private Map<String, Object> properties;

    private static final Map<String, Property> propertyList = new HashMap<>();

    static {
        Arrays.stream(Property.values()).forEach(property -> propertyList.put(property.getProperty(), property));
    }

    public GuildProperties() {
        this.properties = new HashMap<>();
    }

    public GuildProperties(IGuild guild) {
        this();

        this.guild = guild;
        this.guildId = guild.getID();
    }

    @PostLoad
    public void postLoad() {
        System.out.println("POST LOAD : properties = " + properties);
        this.guild = BotManager.getInstance().getClient().getGuildByID(guildId);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(Property property, Class<T> type) {
        return (T) properties.get(property.getProperty());
    }

    public Object getProperty(String property) throws PropertyNotFoundException {
        if (validateProperty(property)) {
            return properties.get(property);
        } else {
            throw new PropertyNotFoundException();
        }
    }

    public void setProperty(Property property, Object value) {
        if (value == null || property.getType().isInstance(value)) {
            properties.put(property.getProperty(), value);

            save();

            guild.getClient().getDispatcher().dispatch(new GuildPropertyChangedEvent(guild, property, value));
        } else {
            throw new IllegalArgumentException(String.format("Property %s must be of type %s, provided %s",
                    property,
                    property.getType().getSimpleName(),
                    value.getClass().getSimpleName()));
        }
    }

    public void setProperty(String property, String value) throws PropertyNotFoundException, IllegalArgumentException {
        if (validateProperty(property)) {
            Property propertyEnum = propertyList.get(property);
            Object castedValue = castProperty(value, propertyEnum.getType());
            properties.put(property, castedValue);

            save();

            guild.getClient().getDispatcher().dispatch(new GuildPropertyChangedEvent(guild, propertyList.get(property), value));
        } else {
            throw new PropertyNotFoundException();
        }
    }

    public void save() {
        GuildPropertiesRepository propertiesRepository = DatastoreManagerSingleton.getInstance().getRepository(GuildPropertiesRepository.class);
        propertiesRepository.save(this);
    }

    private Object castProperty(String value, Class<?> type) throws IllegalArgumentException {
        Object parsedValue = StringUtils.parse(value);
        if (type.isInstance(parsedValue)) {
            return parsedValue;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return if the property key is a valid property
     */
    private boolean validateProperty(String property) {
        return propertyList.containsKey(property);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GuildProperties[\n");
        if (properties != null) {
            properties.forEach((s, o) -> builder.append('\t').append(s).append(" = ").append(o).append('\n'));
        } else {
            builder.append("null");
        }
        builder.append(']');

        return builder.toString();
    }
}
