package lethbridge.model.bl;


import lethbridge.model.entities.UserTradingSumEntity;
import lethbridge.model.pattern.CDIBaseModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class UserTradingSumMgr extends CDIBaseModel implements Serializable {

    public List<UserTradingSumEntity> getAllUsersTradingSum() throws Exception {
        String query = "SELECT TR.USER_ID USER_ID, " +
                "TR.COMPANY_NAME COMPANY_NAME, " +
                "SUM (TR.NUMBER_OF_UNIT) TOTAL_STOCK_NUMBER, " +
                "SUM(NUMBER_OF_UNIT * LAST_PRICE) COST " +
                "FROM TRADING TR WHERE TR.STATUS='filled' " +
                "GROUP BY TR.USER_ID , TR.COMPANY_NAME " +
                "ORDER BY TR.USER_ID, COMPANY_NAME";
        Query nativeQuery = entityManager.createNativeQuery(query, "UsersTradingSummary");
        return nativeQuery.getResultList();
    }
}
