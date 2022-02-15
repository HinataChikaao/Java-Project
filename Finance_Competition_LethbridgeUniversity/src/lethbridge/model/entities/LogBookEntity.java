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
@javax.persistence.Table(name = "LOG_BOOK", schema = "LETHBRIDGE")
public class LogBookEntity implements Serializable {

    private String userID;
    private String actionName;
    private String message;
    private Date timeStamp;

    public LogBookEntity() {
        this.message = "NO MESSAGE";
    }

    public LogBookEntity(String userID, String actionName, String message) {
        this();
        this.userID = Optional.ofNullable(userID).map(String::trim).orElse(R.PublicText.EMPTY);
        this.actionName = Optional.ofNullable(actionName).map(String::trim).orElse(R.PublicText.EMPTY);
        this.message = Optional.ofNullable(message).map(String::trim).orElse(message);
    }

    public LogBookEntity(String userID, String actionName) {
        this();
        this.userID = userID;
        this.actionName = actionName;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "User username number not entered")
    @javax.validation.constraints.NotNull(message = "User username number not entered")
    @javax.validation.constraints.Size(min = 3, max = 256, message = "Minimum username characters numbers is 3 and the maximum is 256")
    @javax.persistence.Id
    @javax.persistence.Column(name = "USER_ID", columnDefinition = "varchar2(256)")
    public String getUserID() {
        return Optional.ofNullable(userID).map(String::trim).orElse(R.PublicText.EMPTY);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "Action name not entered")
    @javax.validation.constraints.NotNull(message = "Action name not entered")
    @javax.validation.constraints.Size(min = 3, max = 256, message = "Minimum action name characters numbers is 3 and the maximum is 256")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "ACTION_NAME", columnDefinition = "varchar2(256)")
    public String getActionName() {
        return Optional.ofNullable(actionName).map(String::trim).orElse(R.PublicText.EMPTY);
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @javax.validation.constraints.Size(max = 500, message = "Maximum message characters number is 500")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "MESSAGE", columnDefinition = "varchar2(500)")
    public String getMessage() {
        return Optional.ofNullable(message).map(String::trim).orElse(R.PublicText.EMPTY);
    }

    public void setMessage(String message) {
        this.message = message;
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
