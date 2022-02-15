package lethbridge.model.bl;


import lethbridge.model.entities.AccountValueEntity;
import lethbridge.model.pattern.CDIBaseModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;

@Named
@RequestScoped
public class AccountValueMgr extends CDIBaseModel implements Serializable {

    private boolean iskExistUser(String userID) throws Exception {
        return entityManager.find(AccountValueEntity.class, userID) != null;
    }

    public void resetUser(String userID) throws Exception {
        if (iskExistUser(userID)) {
            AccountValueEntity accountValueEntity =
                    entityManager.find(AccountValueEntity.class, userID);
            if (accountValueEntity != null) {
                accountValueEntity.setAccountValue(0.00);
                accountValueEntity.setDailyPL(0.00);
                accountValueEntity.setPlToDate(0.00);
                accountValueEntity.setCashBalance(0.00);
                accountValueEntity.setPortFolioValue(0.00);
            }
        } else {
            throw new Exception("User not found ...");
        }
    }

    public void registerUser(String userID) throws Exception {
        entityManager.persist(new AccountValueEntity(userID, 00.0, 00.0,
                00.0, 00.0, 00.0));
    }

    public void removeUser(String userID) throws Exception {
        AccountValueEntity accountValueEntity =
                entityManager.find(AccountValueEntity.class, userID);
        if (accountValueEntity != null) {
            entityManager.remove(accountValueEntity);
        }
    }

    @Transactional
    public void updateUser(String userID, double accountValue, double portfolio, double dailyPL,
                           double plToDate, double cashBalance) throws Exception {
        AccountValueEntity accountValueEntity =
                entityManager.find(AccountValueEntity.class, userID);
        if (accountValueEntity != null) {
            accountValueEntity.setAccountValue(accountValue);
            accountValueEntity.setPortFolioValue(portfolio);
            accountValueEntity.setDailyPL(dailyPL);
            accountValueEntity.setPlToDate(plToDate);
            accountValueEntity.setCashBalance(cashBalance);
        }
    }
}
