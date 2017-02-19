package com.omega.database.morphium;

import com.mongodb.BasicDBObject;
import com.omega.guild.GuildProperties;
import de.caluga.morphium.TypeMapper;

public class GuildPropertiesMapper implements TypeMapper<GuildProperties> {

    @Override
    public Object marshall(GuildProperties o) {
        BasicDBObject object = new BasicDBObject();
        return object;
    }

    @Override
    public GuildProperties unmarshall(Object d) {
        return null;
    }
}
