package com.omega.database.morphium;

import com.omega.audio.Playlist;
import com.omega.database.PlaylistRepository;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.query.Query;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class PlaylistMorphiumRepository extends MorphiumRepository<Playlist> implements PlaylistRepository {

    public PlaylistMorphiumRepository(Morphium morphium) {
        super(morphium, Playlist.class);
    }

    public boolean exists(String playlistName) {
        return morphium.createQueryFor(type).f(Playlist.Fields.normalizedName).eq(playlistName.toLowerCase()).countAll() >= 1;
    }

    @Override
    public void deleteByName(String playlistName) {
        morphium.delete(morphium.createQueryFor(Playlist.class).f(Playlist.Fields.normalizedName).eq(playlistName.toLowerCase()));
    }

    @Override
    public Playlist findByName(String playlistName) {
        return morphium.createQueryFor(Playlist.class)
                .f(Playlist.Fields.normalizedName).eq(playlistName.toLowerCase())
                .get();
    }

    @Override
    public List<Playlist> findByUserPrivacy(IUser user) {
        Query<Playlist> q = morphium.createQueryFor(Playlist.class);
        return q.f(Playlist.Fields.userId).eq(user.getID())
                .f(Playlist.Fields.privacy).eq(Playlist.Privacy.USER.name())
                .asList();
    }

    @Override
    public List<Playlist> findByGuildPrivacy(IGuild guild) {
        return morphium.createQueryFor(Playlist.class)
                .f(Playlist.Fields.guildId).eq(guild.getID())
                .f(Playlist.Fields.privacy).eq(Playlist.Privacy.GUILD.name())
                .asList();
    }
}
