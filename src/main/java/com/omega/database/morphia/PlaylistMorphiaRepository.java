package com.omega.database.morphia;

import com.omega.audio.Playlist;
import com.omega.database.PlaylistRepository;
import org.mongodb.morphia.Datastore;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class PlaylistMorphiaRepository extends MorphiaRepository<Playlist> implements PlaylistRepository {

    public PlaylistMorphiaRepository(Datastore datastore) {
        super(datastore, Playlist.class);
    }

    public boolean exists(String playlistName) {
        return datastore.createQuery(type).field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase()).count() >= 1;
    }

    @Override
    public void deleteByName(String playlistName) {
        datastore.delete(datastore.createQuery(Playlist.class).field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase()));
    }

    @Override
    public Playlist findByName(String playlistName) {
        return datastore.createQuery(Playlist.class)
                .field(Playlist.Fields.normalizedName.name()).equal(playlistName.toLowerCase())
                .get();
    }

    @Override
    public List<Playlist> findByUserPrivacy(IUser user) {
        return datastore.createQuery(Playlist.class).field(Playlist.Fields.userId.name()).equal(user.getID())
                .field(Playlist.Fields.privacy.name()).equal(Playlist.Privacy.USER.name())
                .asList();
    }

    @Override
    public List<Playlist> findByGuildPrivacy(IGuild guild) {
        return datastore.createQuery(Playlist.class)
                .field(Playlist.Fields.guildId.name()).equal(guild.getID())
                .field(Playlist.Fields.privacy.name()).equal(Playlist.Privacy.GUILD.name())
                .asList();
    }
}
