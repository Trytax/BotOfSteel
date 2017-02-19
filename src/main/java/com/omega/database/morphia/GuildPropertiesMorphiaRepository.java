package com.omega.database.morphia;

import com.omega.database.GuildPropertiesRepository;
import com.omega.guild.GuildProperties;
import org.mongodb.morphia.Datastore;
import sx.blah.discord.handle.obj.IGuild;

public class GuildPropertiesMorphiaRepository extends MorphiaRepository<GuildProperties> implements GuildPropertiesRepository {

    public GuildPropertiesMorphiaRepository(Datastore datastore) {
        super(datastore, GuildProperties.class);
    }

    @Override
    public GuildProperties findByGuild(IGuild guild) {
        return datastore.createQuery(type).field(GuildProperties.Fields.guildId.name()).equal(guild.getID()).get();
    }
}
