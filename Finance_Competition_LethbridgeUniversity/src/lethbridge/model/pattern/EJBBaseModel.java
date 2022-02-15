package lethbridge.model.pattern;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.UserTransaction;

public abstract class EJBBaseModel {

    private static boolean extendedTransaction = false;

    @PersistenceContext(unitName = "lethbridge",
            type = PersistenceContextType.TRANSACTION)
    protected EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    protected void begin() throws Exception {

        if (this.userTransaction.getStatus() != 0) {
            this.userTransaction.begin();
            extendedTransaction = false;
        } else {
            extendedTransaction = true;
        }
    }

    protected void commit() throws Exception {
        if (!extendedTransaction) {
            userTransaction.commit();
        }
    }

    protected void rollback() throws Exception {
        userTransaction.rollback();
    }

    protected void rollback(Exception ex) throws Exception {
        userTransaction.rollback();
        throw ex;
    }

}
