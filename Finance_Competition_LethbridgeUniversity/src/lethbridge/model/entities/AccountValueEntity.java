package lethbridge.model.entities;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Stateful
@LocalBean

@javax.persistence.Entity
@javax.persistence.Table(name = "ACCOUNT_VALUE", schema = "LETHBRIDGE")
public class AccountValueEntity implements Serializable {


    private String userID;
    private double accountValue;
    private double dailyPL;
    private double plToDate;
    private double cashBalance;
    private double portFolioValue;
    private Date timeStamp;

    public AccountValueEntity() {
    }

    public AccountValueEntity(String userID,
                              double accountValue,
                              double dailyPL,
                              double plToDate,
                              double cashBalance,
                              double portFolioValue) {
        this();
        this.userID = userID;
        this.accountValue = accountValue;
        this.dailyPL = dailyPL;
        this.plToDate = plToDate;
        this.cashBalance = cashBalance;
        this.portFolioValue = portFolioValue;
    }

    @org.hibernate.validator.constraints.NotEmpty( message = "User username number not entered")
    @javax.validation.constraints.NotNull( message = "User username number not entered")
    @javax.persistence.Id
    @javax.persistence.Column(name = "USER_ID", columnDefinition = "varchar2(256")
    public String getUserID() {
        return Optional.ofNullable(userID).map(String::trim).orElse("0");
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "ACCOUNT_VALUE", columnDefinition = "number")
    public double getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(double accountValue) {
        this.accountValue = accountValue;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "DAILY_PL", columnDefinition = "number")
    public double getDailyPL() {
        return dailyPL;
    }

    public void setDailyPL(double dailyPL) {
        this.dailyPL = dailyPL;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PL_TO_DATE", columnDefinition = "number")
    public double getPlToDate() {
        return plToDate;
    }

    public void setPlToDate(double plToDate) {
        this.plToDate = plToDate;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "CASH_BALANCE", columnDefinition = "number")
    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PORTFOLIO_VALUE", columnDefinition = "number")
    public double getPortFolioValue() {
        return portFolioValue;
    }

    public void setPortFolioValue(double portFolioValue) {
        this.portFolioValue = portFolioValue;
    }

    @javax.persistence.Version
    @javax.persistence.Column(name = "TIME_STAMP", columnDefinition = "date")
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

}
