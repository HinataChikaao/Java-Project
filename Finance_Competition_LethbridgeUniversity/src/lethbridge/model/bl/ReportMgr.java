package lethbridge.model.bl;


import lethbridge.model.entities.ReportEntity;
import lethbridge.model.entities.UserTradingSumEntity;
import lethbridge.model.pattern.CDIBaseModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class ReportMgr extends CDIBaseModel implements Serializable {

    public List<ReportEntity> getAllUsers() throws Exception {

        String query = "SELECT UD.USER_ID USER_ID, UD.FIRST_NAME FIRST_NAME, UD.LAST_NAME LAST_NAME, " +
                "UD.INITIAL_INVESTMENT INITIAL_INVESTMENT, UD.CURRENT_INVESTMENT CURRENT_INVESTMENT," +
                "(SELECT LU.LAST_UPDATE LAST_UPDATE FROM (SELECT TR.LAST_UPDATE LAST_UPDATE FROM " +
                "LETHBRIDGE.TRADING TR WHERE TRIM(TR.USER_ID)= TRIM(UD.USER_ID) " +
                "ORDER BY TR.LAST_UPDATE DESC) LU  WHERE ROWNUM = 1) LAST_UPDATE, " +
                "(SELECT LDT.LOGIN_DATE||' '||LDT.LOGIN_TIME LOG_DATE_TIME FROM " +
                "(SELECT LB.THE_DATE LOGIN_DATE, LB.THE_TIME LOGIN_TIME FROM " +
                "LETHBRIDGE.LOG_BOOK LB WHERE TRIM(LB.USER_ID)= TRIM(UD.USER_ID) " +
                "ORDER BY LB.THE_DATE DESC, LB.THE_TIME DESC) LDT WHERE ROWNUM = 1) " +
                "LAST_LOGIN, (SELECT AV.ACCOUNT_VALUE ACCOUNT_VALUE FROM LETHBRIDGE.ACCOUNT_VALUE AV " +
                "WHERE TRIM(AV.USER_ID) = TRIM(UD.USER_ID)) ACCOUNT_VALUE,(SELECT AV.PORTFOLIO_VALUE PORTFOLIO_VALUE " +
                "FROM LETHBRIDGE.ACCOUNT_VALUE AV WHERE TRIM(AV.USER_ID) = TRIM(UD.USER_ID)) PORTFOLIO_VALUE FROM " +
                "LETHBRIDGE.USERSDATA UD WHERE UD.USER_ID IN " +
                "(SELECT USER_ID FROM ROLESDATA WHERE TRIM(ROLE_NAME) = 'student')";

        Query nativeQuery = entityManager.createNativeQuery(query, "StudentReportMap");
        return nativeQuery.getResultList();
    }

  
}
