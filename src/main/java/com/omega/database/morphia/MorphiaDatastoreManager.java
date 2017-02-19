package com.omega.database.morphia;

import com.omega.database.DatastoreManager;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.MorphiumConfig;
import de.caluga.morphium.TypeMapper;
import org.mongodb.morphia.Datastore;

import java.net.UnknownHostException;
import java.util.Map;

public class MorphiaDatastoreManager extends DatastoreManager {

    private final Datastore datastore;

    public MorphiaDatastoreManager(Datastore datastore) {
        this.datastore = datastore;
    }

    public Datastore getDatastore() {
        return datastore;
    }
}
