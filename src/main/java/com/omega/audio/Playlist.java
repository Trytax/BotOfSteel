package com.omega.audio;

import de.caluga.morphium.annotations.Entity;
import de.caluga.morphium.annotations.Id;
import de.caluga.morphium.annotations.Index;
import de.caluga.morphium.annotations.Reference;
import de.caluga.morphium.driver.bson.MorphiumId;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Index
public class Playlist {

    public enum Fields {
        id, name, normalizedName, tracks, size, userId, guildId, privacy
    }

    public enum Privacy {
        USER(0), GUILD(1);

        private int id;

        Privacy(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Privacy findById(int id) {
            return Arrays.stream(values()).filter(privacy1 -> privacy1.id == id).findFirst().orElse(null);
        }
    }

    @Id
    private MorphiumId id;

    private String name;

    private String normalizedName;

    @Reference(lazyLoading = true)
    private List<AudioTrack> tracks;
    private int size;

    private String userId;
    private String guildId;

    private Privacy privacy;

    public Playlist() {
    }

    public Playlist(String name, Privacy privacy, String guildId, String userId) {
        this.name = name;
        this.normalizedName = name.toLowerCase();

        this.tracks = new ArrayList<>();
        this.userId = userId;
        this.guildId = guildId;
        this.privacy = privacy;
    }

    public Playlist(String name, Privacy privacy, IGuild guild, IUser user) {
        this(name, privacy, guild.getID(), user.getID());
    }

    public Playlist(String name, IGuild guild, IUser user) {
        this(name, Privacy.USER, guild.getID(), user.getID());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTrack(AudioTrack track) {
        tracks.add(track);
        size++;
    }

    public void removeTrack(AudioTrack track) {
        tracks.remove(track);
        size--;
    }

    public List<AudioTrack> getTracks() {
        return tracks;
    }

    public int getSize() {
        return size;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Privacy getPrivacy() {
        return privacy;
    }
}
