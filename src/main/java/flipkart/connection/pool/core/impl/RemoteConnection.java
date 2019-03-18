package flipkart.connection.pool.core.impl;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import flipkart.connection.pool.model.Connection;

public class RemoteConnection {
    public static Connection getConnection(String hostname, String port) {
        String key = hostname + port;
        Connection c = new Connection(key);
        return c;
    }
}
