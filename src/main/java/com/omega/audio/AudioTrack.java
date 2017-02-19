package com.omega.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.caluga.morphium.annotations.Entity;
import de.caluga.morphium.annotations.Id;
import de.caluga.morphium.annotations.caching.Cache;
import de.caluga.morphium.driver.bson.MorphiumId;

import java.io.Serializable;

@Entity
@Cache
public class AudioTrack implements Serializable {

    public enum Fields {
        id, title, author, source, length;
    }

    @Id
    private MorphiumId id;

    private String title;
    private String author;
    private String source;
    private long length;

    public AudioTrack() {
    }

    public AudioTrack(com.sedmelluq.discord.lavaplayer.track.AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        this.title = info.title;
        this.author = info.author;
        this.source = info.identifier;
        this.length = info.length;
    }

    public AudioTrack(String title, String author, String source, int length) {
        this.title = title;
        this.author = author;
        this.source = source;
        this.length = length;
    }

    public MorphiumId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
