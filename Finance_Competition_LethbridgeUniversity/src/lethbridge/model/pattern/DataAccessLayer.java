package lethbridge.model.pattern;


import java.sql.Connection;

public class DataAccessLayer implements AutoCloseable {

    protected Connection connection;


    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
            connection.close();
        }
    }
}
