package com.omega.database.morphium;

import com.omega.database.DatastoreManager;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.MorphiumConfig;
import de.caluga.morphium.TypeMapper;

import java.net.UnknownHostException;
import java.util.Map;

public class MorphiumDatastoreManager extends DatastoreManager {

    private final Morphium morphium;

    public MorphiumDatastoreManager(MorphiumConfig morphiumConfig) throws ClassNotFoundException, UnknownHostException,
            InstantiationException, IllegalAccessException, NoSuchFieldException {
        this.morphium = new Morphium(morphiumConfig);

        addRepositories(new MorphiumRepository[]{
                new AudioTrackMorphiumRepository(morphium),
                new PlaylistMorphiumRepository(morphium),
                new GuildPropertiesMorphiumRepository(morphium)
        });
    }

    public Object getId(Object entity) {
        return morphium.getId(entity);
    }

    public Morphium getMorphium() {
        return morphium;
    }
}
