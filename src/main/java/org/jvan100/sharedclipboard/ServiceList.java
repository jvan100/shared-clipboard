package org.jvan100.sharedclipboard;

import java.util.ArrayList;
import java.util.List;

public class ServiceList<T> {

    private volatile List<T> services;

    public ServiceList() {
        this.services = new ArrayList<>();
    }

    public synchronized void addService(T service) {
        services.add(service);
    }

    public List<T> getServices() {
        return services;
    }

}
