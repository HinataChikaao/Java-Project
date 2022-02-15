package lethbridge.model.bl;


import lethbridge.model.entities.LogBookEntity;
import lethbridge.model.pattern.CDIBaseModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;

@Named
@RequestScoped
public class LogBookMgr extends CDIBaseModel implements Serializable {


    @Transactional
    public void registerLog(LogBookEntity logBookEntity) throws Exception {
        entityManager.persist(logBookEntity);
    }

    @Transactional
    public void removeLog(String userID) throws Exception {
        entityManager.createQuery("delete from LogBookEntity lbe where trim(lbe.userID) = :userID")
                .setParameter("userID", userID).executeUpdate();
    }

}
