package org.jvan100.sharedclipboard.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {

    private final Socket socket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public String readMessage() throws IOException, ClassNotFoundException {
        return (String) ois.readObject();
    }

    public void writeMessage(String message) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void close() throws IOException {
        if (!socket.isClosed())
            socket.close();
    }

}
