package com.omega.database;

import com.mongodb.MongoClient;
import com.omega.database.morphia.MorphiaDatastoreManager;
import com.omega.database.morphium.MorphiumDatastoreManager;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.MorphiumConfig;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Properties;

public class DatastoreManagerSingleton {

    private DatastoreManager datastoreManager;

    private DatastoreManagerSingleton() {
    }

    private DatastoreManager createDatastoreManager() {
        return createMorphiumDatastoreManager();
    }

    private MorphiumDatastoreManager createMorphiumDatastoreManager() {
        try {
            Properties morphiumProps = new Properties();
            morphiumProps.load(DatastoreManagerSingleton.class.getResourceAsStream("/morphium.properties"));
            MorphiumConfig cfg = MorphiumConfig.fromProperties(morphiumProps);

            return new MorphiumDatastoreManager(cfg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MorphiaDatastoreManager createMorphiaDatastoreManager() {
        Morphia morphia = new Morphia();
        Datastore datastore = morphia.createDatastore(new MongoClient("localhost:27017"), "discord_bot");
        return new MorphiaDatastoreManager(datastore);
    }

    public static DatastoreManager getInstance() {
        DatastoreManagerSingleton singleton = DatastoreManagerSingletonHolder.INSTANCE;
        if (singleton.datastoreManager == null) {
            singleton.datastoreManager = singleton.createDatastoreManager();
        }

        return singleton.datastoreManager;
    }

    private static class DatastoreManagerSingletonHolder {
        private static final DatastoreManagerSingleton INSTANCE = new DatastoreManagerSingleton();
    }
}