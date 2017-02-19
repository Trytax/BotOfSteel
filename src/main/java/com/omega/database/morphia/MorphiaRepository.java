package com.omega.database.morphia;

import com.omega.database.Repository;
import org.mongodb.morphia.Datastore;

public class MorphiaRepository<T> implements Repository<T> {

    protected final Datastore datastore;
    protected final Class<T> type;

    public MorphiaRepository(Datastore datastore, Class<T> type) {
        this.datastore = datastore;
        this.type = type;
    }

    @Override
    public T findById(Object id) {
        return datastore.get(type, id);
    }

    @Override
    public void save(T entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(T entity) {
        datastore.delete(entity);
    }

    @Override
    public Class<T> getType() {
        return type;
    }
}
