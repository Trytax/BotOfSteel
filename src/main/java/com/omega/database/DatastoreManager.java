package com.omega.database;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

public class DatastoreManager {

    private Map<Class<?>, Repository> repositories;

    public DatastoreManager() {
        this.repositories = new HashMap<>();
    }

    protected void addRepositories(Repository... repositories) {
        stream(repositories).forEach(this::addRepository);
    }

    protected void addRepository(Repository repository) {
        Class<?> repositoryClass = repository.getClass();
        Class<?> repositoryInterface = Arrays.stream(repositoryClass.getInterfaces())
                .filter(aInterface -> aInterface.isInstance(repository))
                .findFirst()
                .orElse(null);
        if (repositoryInterface != null) {
            repositories.put(repositoryInterface, repository);
        }

        repositories.put(repositoryClass, repository);
    }

    public <T extends Repository> T getRepository(Class<T> repositoryType) {
        return (T) repositories.get(repositoryType);
    }
}
