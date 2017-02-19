package com.omega.database;

import com.omega.guild.GuildProperties;
import sx.blah.discord.handle.obj.IGuild;

public interface GuildPropertiesRepository extends Repository<GuildProperties> {

    GuildProperties findByGuild(IGuild guild);
}
