package flipkart.connection.pool.model;

public class Connection {



    private String key;

    public  Connection(String key){ key = key;}

    public String getKey(){
        return key;
    }
    public void setKey(String key){
        key= key;
    }

    public void close() {
    }
}
