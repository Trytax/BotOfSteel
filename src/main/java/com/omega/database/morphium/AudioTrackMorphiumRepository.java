package com.omega.database.morphium;

import com.omega.audio.AudioTrack;
import com.omega.database.AudioTrackRepository;
import de.caluga.morphium.Morphium;

public class AudioTrackMorphiumRepository extends MorphiumRepository<AudioTrack> implements AudioTrackRepository {

    public AudioTrackMorphiumRepository(Morphium morphium) {
        super(morphium, AudioTrack.class);
    }

}
