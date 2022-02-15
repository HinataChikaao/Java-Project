package model.provider;


import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public final class DataSourceProvider {

    private final static String pizzaDataSource = "java:comp/env/jdbc/pizza";

    public static Connection getPizzaConnection() throws NamingException, SQLException {
        InitialContext context = new InitialContext();
        DataSource xaDataSource = (DataSource) context.lookup(pizzaDataSource);
        return xaDataSource.getConnection();
    }

    public static <T> T getBeanByName(Class<T> clazz) throws NamingException {
        InitialContext initialContext = new InitialContext();
        BeanManager bm = (BeanManager) initialContext.lookup("java:comp/BeanManager");
        Bean<T> bean = (Bean<T>) bm.getBeans(clazz).iterator().next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        T dao = (T) bm.getReference(bean, clazz, ctx);
        return dao;
    }

}