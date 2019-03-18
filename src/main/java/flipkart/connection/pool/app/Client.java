package flipkart.connection.pool.app;

import flipkart.connection.pool.core.impl.ConnectionPool;
import flipkart.connection.pool.model.Connection;

public class Client implements Runnable{
    public Client(flipkart.connection.pool.core.ConnectionPool connectionPool) throws Exception {
        this.connectionPool = connectionPool;
    }
    Connection con = null;
    flipkart.connection.pool.core.ConnectionPool connectionPool;

    public void run()
    {
        System.out.println("current Thread Entered"+Thread.currentThread().getName());
        try {
            con = connectionPool.getConnection();
            System.out.println("connection acquired: "+Thread.currentThread().getName());

            connectionPool.releaseConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        ConnectionPool connectionPool = ConnectionPool.create("10.1.0.20", "38080");
        Client pool = new Client(connectionPool);
        for(int i = 1;i<=5;i++)
        {
            new Thread(pool).start();
        }
        connectionPool.shutdown();
    }

}