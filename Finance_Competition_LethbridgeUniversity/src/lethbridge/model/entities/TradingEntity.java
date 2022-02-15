package lethbridge.model.entities;

import util.R;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

@Stateful
@LocalBean

@javax.persistence.Entity
@javax.persistence.Table(name = "TRADING", schema = "LETHBRIDGE")
public class TradingEntity implements Serializable {

    private long tradingID;
    private String userID;
    private String companyName;
    private Double lastPrice;
    private Long numberOfUnit;
    private String tradingType;
    private String lastUpdate;
    private String status;
    private Date timeStamp;
    private String statusColor;
    private String checkStatus;

    public TradingEntity() {
    }

    public TradingEntity(long tradingID, String userID,
                         String companyName, double lastPrice, long numberOfUnit,
                         String tradingType, String lastUpdate, String status,
                         Date timeStamp) {
        this.tradingID = tradingID;
        this.userID = userID;
        this.companyName = companyName;
        this.lastPrice = lastPrice;
        this.numberOfUnit = numberOfUnit;
        this.tradingType = tradingType;
        this.lastUpdate = lastUpdate;
        this.status = status;
        this.timeStamp = timeStamp;
    }


    @javax.persistence.Id
    @javax.persistence.Column(name = "TRADING_ID", columnDefinition = "number", nullable = false, precision = 0)
    @javax.persistence.SequenceGenerator(name = "trd_seq", sequenceName = "TRADING_SEQ")
    @javax.persistence.GeneratedValue(strategy = GenerationType.AUTO, generator = "trd_seq")
    public long getTradingID() {
        return tradingID;
    }

    public void setTradingID(long tradingID) {
        this.tradingID = tradingID;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "User username number not entered")
    @javax.validation.constraints.NotNull(message = "User username number not entered")
    @javax.validation.constraints.Size(min = 3, max = 256, message = "Minimum user username characters numbers is 3 and the maximum is 256")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "USER_ID", columnDefinition = "varchar2(256)", nullable = false, length = 256)
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "Company name number not entered")
    @javax.validation.constraints.NotNull(message = "Company name number not entered")
    @javax.validation.constraints.Size(min = 3, max = 256, message = "Minimum company name characters numbers is 3 and the maximum is 256")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "COMPANY_NAME", columnDefinition = "varchar2(256)", length = 256)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @javax.validation.constraints.NotNull(message = "Last price amount not entered")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "LAST_PRICE", columnDefinition = "number(12,2)", precision = 2)
    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    @javax.validation.constraints.NotNull(message = "Number of unit not entered")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "NUMBER_OF_UNIT", columnDefinition = "number(12)", precision = 0)
    public Long getNumberOfUnit() {
        return numberOfUnit;
    }

    public void setNumberOfUnit(Long numberOfUnit) {
        this.numberOfUnit = numberOfUnit;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "Trading type not entered")
    @javax.validation.constraints.NotNull(message = "Trading type not entered")
    @javax.validation.constraints.Size(min = 3, max = 6, message = "Minimum trading type characters numbers is 3 and the maximum is 6")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "TRADING_TYPE", columnDefinition = "varchar2(6)", length = 6)
    public String getTradingType() {
        return tradingType;
    }

    public void setTradingType(String tradingType) {
        this.tradingType = tradingType;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "Last update not entered")
    @javax.validation.constraints.NotNull(message = "Last update not entered")
    @javax.validation.constraints.Size(min = 10, max = 20, message = "Minimum last update characters numbers is 10 and the maximum is 20")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "LAST_UPDATE", columnDefinition = "varchar2(20)", length = 20)
    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "Status not entered")
    @javax.validation.constraints.NotNull(message = "Status not entered")
    @javax.validation.constraints.Size(min = 3, max = 10, message = "Minimum status characters numbers is 3 and the maximum is 10")
    @javax.persistence.Basic
    @javax.persistence.Column(name = "STATUS", columnDefinition = "varchar2(10)", length = 10)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @javax.persistence.Version
    @javax.persistence.Column(name = "TIME_STAMP", columnDefinition = "date")
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }


    @javax.persistence.Transient
    public String getStatusColor() {
        return statusColor + R.PublicText.SPACE;
    }

    @javax.persistence.Transient
    public String getCheckStatus() {
        if (this.status.trim().equalsIgnoreCase("filled")) {
            statusColor = "green_color_font";
            return "/resources/images/dot/greenDot.png";
        } else if (this.status.trim().equalsIgnoreCase("canceled")) {
            statusColor = "red_color_font";
            return "/resources/images/dot/redDot.png";
        }
        return "";
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

}
