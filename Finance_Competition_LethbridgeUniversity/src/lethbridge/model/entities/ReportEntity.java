package lethbridge.model.entities;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.*;
import java.io.Serializable;

@Stateful
@LocalBean

@SqlResultSetMapping(
        name = "StudentReportMap",
        classes = {
                @ConstructorResult(
                        targetClass = ReportEntity.class,
                        columns = {
                                @ColumnResult(name = "USER_ID", type = String.class),
                                @ColumnResult(name = "FIRST_NAME", type = String.class),
                                @ColumnResult(name = "LAST_NAME", type = String.class),
                                @ColumnResult(name = "INITIAL_INVESTMENT", type = Double.class),
                                @ColumnResult(name = "CURRENT_INVESTMENT", type = Double.class),
                                @ColumnResult(name = "LAST_UPDATE", type = String.class),
                                @ColumnResult(name = "LAST_LOGIN", type = String.class),
                                @ColumnResult(name = "ACCOUNT_VALUE", type = Double.class),
                                @ColumnResult(name = "PORTFOLIO_VALUE", type = Double.class)
                        }
                )
        }
)
@javax.persistence.Entity
public class ReportEntity implements Serializable {

    private String userID;
    private String firstName;
    private String lastName;
    private double initialInvestment;
    private double currentInvestment;
    private String lastUpdate;
    private String lastLogin;
    private double accountValue;
    private double portfolioValue;

    public ReportEntity() {
    }

    public ReportEntity(String userID, String firstName,
                        String lastName, double initialInvestment,
                        double currentInvestment, String lastUpdate,
                        String lastLogin, double accountValue, double portfolioValue) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.initialInvestment = initialInvestment;
        this.currentInvestment = currentInvestment;
        this.lastUpdate = lastUpdate;
        this.lastLogin = lastLogin;
        this.accountValue = accountValue;
        this.portfolioValue = portfolioValue;
    }

    @javax.persistence.Id
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(double initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    public double getCurrentInvestment() {
        return currentInvestment;
    }

    public void setCurrentInvestment(double currentInvestment) {
        this.currentInvestment = currentInvestment;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public double getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(double accountValue) {
        this.accountValue = accountValue;
    }

    public double getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(double portfolioValue) {
        this.portfolioValue = portfolioValue;
    }


}
