package flipkart.connection.pool.core;


import flipkart.connection.pool.model.Connection;

public interface ConnectionPool {
    Connection getConnection() throws Exception;
    boolean releaseConnection(Connection connection);
}