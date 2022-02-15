package lethbridge.model.entities;


import util.R;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Stateful
@LocalBean

@javax.persistence.Entity
@javax.persistence.Table(name = "USERSDATA", schema = "LETHBRIDGE")
public class UserEntity implements Serializable {

    private String userID;
    private String password;
    private String firstName;
    private String lastName;
    private double initialInvestment;
    private double currentInvestment;
    private int userActive;
    private int userOnline;
    private int userCount;
    private Date timeStamp;

    public UserEntity() {
    }

    public UserEntity(String userID,
                      String password,
                      String firstName,
                      String lastName,
                      double initialInvestment,
                      double currentInvestment,
                      int userActive,
                      int userOnline,
                      int userCount) {
        this.userID = userID;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.initialInvestment = initialInvestment;
        this.currentInvestment = currentInvestment;
        this.userActive = userActive;
        this.userOnline = userOnline;
        this.userCount = userCount;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "User username number not entered")
    @javax.validation.constraints.NotNull(message = "User username number not entered")
    @javax.validation.constraints.Size(min = 3, max = 28, message = "Minimum username characters numbers is 3 and the maximum is 256")
    @javax.persistence.Id
    @javax.persistence.Column(name = "USER_ID", columnDefinition = "varchar2(256)")
    public String getUserID() {
        return Optional.ofNullable(userID).map(String::trim).orElse(R.PublicText.EMPTY);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "Password not entered")
    @javax.validation.constraints.NotNull(message = "Password not entered")
    @javax.validation.constraints.Size(min = 4, max = 16, message = "Minimum password characters numbers is 4 and the maximum is 16")
    @javax.persistence.Basic()
    @javax.persistence.Column(name = "USER_PASS", columnDefinition = "varchar2(256)")
    public String getPassword() {
        return Optional.ofNullable(password).map(String::trim).orElse(R.PublicText.EMPTY);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "name not entered")
    @javax.validation.constraints.NotNull(message = "name not entered")
    @javax.validation.constraints.Size(min = 3, max = 64, message = "Minimum name characters numbers is 3 and the maximum is 64")
    @javax.persistence.Basic()
    @javax.persistence.Column(name = "FIRST_NAME", columnDefinition = "varchar2(256)")
    public String getFirstName() {
        return Optional.ofNullable(firstName).map(String::trim).orElse(R.PublicText.EMPTY);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "Family not entered")
    @javax.validation.constraints.NotNull(message = "Family not entered")
    @javax.validation.constraints.Size(min = 3, max = 64, message = "Minimum family characters numbers is 3 and the maximum is 64")
    @javax.persistence.Basic()
    @javax.persistence.Column(name = "LAST_NAME", columnDefinition = "varchar2(256)")
    public String getLastName() {
        return Optional.ofNullable(lastName).map(String::trim).orElse(R.PublicText.EMPTY);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @javax.persistence.Basic()
    @javax.persistence.Column(name = "INITIAL_INVESTMENT", columnDefinition = "number(12,2)")
    public double getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(double initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    @javax.persistence.Basic()
    @javax.persistence.Column(name = "CURRENT_INVESTMENT", columnDefinition = "number(12,2)")
    public double getCurrentInvestment() {
        return currentInvestment;
    }

    public void setCurrentInvestment(double currentInvestment) {
        this.currentInvestment = currentInvestment;
    }

    @javax.persistence.Basic()
    @javax.persistence.Column(name = "USER_ACTIVE", columnDefinition = "number(4,0)")
    public int getUserActive() {
        return userActive;
    }

    public void setUserActive(int userActive) {
        this.userActive = userActive;
    }

    @javax.persistence.Basic()
    @javax.persistence.Column(name = "USER_ONLINE", columnDefinition = "number(4,0)")
    public int getUserOnline() {
        return userOnline;
    }

    public void setUserOnline(int userOnline) {
        this.userOnline = userOnline;
    }

    @javax.persistence.Basic()
    @javax.persistence.Column(name = "USER_COUNT", columnDefinition = "number(4,0)")
    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    @javax.persistence.Version
    @javax.persistence.Column(name = "TIME_STAMP", columnDefinition = "Date")
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
