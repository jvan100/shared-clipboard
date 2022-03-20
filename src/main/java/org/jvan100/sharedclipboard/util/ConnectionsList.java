package org.jvan100.sharedclipboard.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConnectionsList {

    private volatile List<Connection> connections;

    public ConnectionsList() {
        this.connections = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized void addConnection(Connection connection) {
        connections.add(connection);
    }

    public List<Connection> getConnections() {
        return connections;
    }

}
