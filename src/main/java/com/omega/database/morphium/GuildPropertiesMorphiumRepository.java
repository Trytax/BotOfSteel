package com.omega.database.morphium;

import com.omega.database.GuildPropertiesRepository;
import com.omega.guild.GuildProperties;
import de.caluga.morphium.Morphium;
import sx.blah.discord.handle.obj.IGuild;

public class GuildPropertiesMorphiumRepository extends MorphiumRepository<GuildProperties> implements GuildPropertiesRepository {

    public GuildPropertiesMorphiumRepository(Morphium morphium) {
        super(morphium, GuildProperties.class);
    }

    @Override
    public GuildProperties findByGuild(IGuild guild) {
        return morphium.createQueryFor(type).f(GuildProperties.Fields.guildId).eq(guild.getID()).get();
    }
}
