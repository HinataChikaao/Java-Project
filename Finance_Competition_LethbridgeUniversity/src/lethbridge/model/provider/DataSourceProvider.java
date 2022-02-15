package lethbridge.model.provider;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Stateless
@LocalBean
public class DataSourceProvider {

    private static final String LETHBRIDGE = "java:comp/env/jdbc/lethbridge";

    public static Connection getConnection() throws NamingException, SQLException {
        synchronized (new Object()) {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup(LETHBRIDGE);
            return dataSource.getConnection();
        }
    }

}
