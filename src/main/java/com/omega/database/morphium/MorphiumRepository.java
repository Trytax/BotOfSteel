package com.omega.database.morphium;

import com.omega.database.Repository;
import de.caluga.morphium.Morphium;

public class MorphiumRepository<T> implements Repository<T> {

    protected final Morphium morphium;
    protected final Class<T> type;

    public MorphiumRepository(Morphium morphium, Class<T> type) {
        this.morphium = morphium;
        this.type = type;
    }

    @Override
    public T findById(Object id) {
        return morphium.findById(type, id);
    }

    @Override
    public void save(T entity) {
        morphium.store(entity);
    }

    @Override
    public void delete(T entity) {
        morphium.delete(entity);
    }

    @Override
    public Class<T> getType() {
        return type;
    }
}
