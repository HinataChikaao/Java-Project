package model.pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


public abstract class CDIBaseModel {

    @PersistenceContext(unitName = "PizzaShopping",
            type = PersistenceContextType.TRANSACTION)

    protected EntityManager entityManager;


}
