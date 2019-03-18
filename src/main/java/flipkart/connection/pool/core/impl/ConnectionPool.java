package flipkart.connection.pool.core.impl;


import flipkart.connection.pool.model.Connection;

import java.util.ArrayList;
import java.util.List;

import static flipkart.connection.pool.constants.Constants.INITIAL_POOL_SIZE;

public class ConnectionPool
        implements flipkart.connection.pool.core.ConnectionPool {

    private String hostname;
    private String port;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();

    public ConnectionPool(String hostname, String port) {
        this.hostname = hostname;
        this.port = port;
    }

    public ConnectionPool(String hostname, String port, List<Connection> connectionPool) {
        this.hostname = hostname;
        this.port = port;
        this.connectionPool= connectionPool;
    }
    public static ConnectionPool create(
            String hostname, String port) throws Exception {

        List<Connection> pool = new ArrayList<>();
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(hostname, port));
        }
        return new ConnectionPool(hostname, port, pool);
    }


    @Override
    public Connection getConnection() throws Exception {
        System.out.println("Require connection");
        try {
            synchronized (connectionPool) {
                while (connectionPool.isEmpty()) {
                    connectionPool.wait();
                    System.out.println("Waiting: " + Thread.currentThread().getName());
                }
            }
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        }
        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        usedConnections.add(connection);

        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        try {
            synchronized (connectionPool) {
                connectionPool.add(connection);
                connectionPool.notifyAll();
            }
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        }
        return usedConnections.remove(connection);
    }

    private static Connection createConnection(
            String hostname, String port)
            throws Exception {
        return RemoteConnection.getConnection(hostname, port);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    public void shutdown() throws Exception {

        try {

            for (Connection c : usedConnections) {
                releaseConnection(c);
            }
            for (Connection c : connectionPool) {
                c.close();
            }
            connectionPool.clear();
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        }
    }


}