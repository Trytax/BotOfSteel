package com.omega.database;

import com.omega.audio.Playlist;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public interface PlaylistRepository extends Repository<Playlist> {

    boolean exists(String playlistName);

    void deleteByName(String PlaylistName);

    Playlist findByName(String playlistName);

    List<Playlist> findByUserPrivacy(IUser user);

    List<Playlist> findByGuildPrivacy(IGuild guild);
}
