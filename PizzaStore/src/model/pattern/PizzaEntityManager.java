package model.pattern;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.Serializable;


@LocalBean
@Stateless
public class PizzaEntityManager implements Serializable {

    @PersistenceContext(unitName = "PizzaShopping",
            type = PersistenceContextType.TRANSACTION)

    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void pizzaPersist(Object object) {
        entityManager.persist(object);
    }

}
