package com.omega.database.morphia;

import com.omega.audio.AudioTrack;
import com.omega.database.AudioTrackRepository;
import org.mongodb.morphia.Datastore;

public class AudioTrackMorphiaRepository extends MorphiaRepository<AudioTrack> implements AudioTrackRepository {

    public AudioTrackMorphiaRepository(Datastore datastore) {
        super(datastore, AudioTrack.class);
    }

}
