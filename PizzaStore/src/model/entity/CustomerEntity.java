package model.entity;

import core.Activation;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;
import org.apache.commons.lang3.StringUtils;
import util.PizzaProps;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EnumType;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.sql.Timestamp;


@LocalBean
@Stateful

@javax.persistence.Entity
@javax.persistence.Table(name = "CUSTOMER", schema = "PIZZA")
public class CustomerEntity implements Serializable {

    private Long customerId;
    @NotNull
    @Length(min = 3, max = 128, message = "invalid name. length(3 - 128)")
    private String firstName;
    @NotNull
    @Length(min = 3, max = 128, message = "invalid family. length(3 - 128)")
    private String lastName;
    @NotNull
    @Length(min = 10, max = 11, message = "invalid mobile mobile number. length(10-11)")
    private String mobileNumber;
    private String phoneNumber;
    @Email(message = "invalid email address.")
    private String email;
    @NotNull
    @Length(min = 10, max = 1000, message = "invalid address. length(10 - 1000)")
    private String address;
    @NotNull
    @Length(min = 8, max = 128, message = "invalid username. length(8 - 128)")
    private String username;
    @NotNull
    @Length(min = 8, max = 255, message = "invalid password. length(8 - 255)")
    private String password;
    private Long subsNumber;
    private Double discount;
    private Activation activate;
    private Timestamp timeStamp;


    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "customer_seq", sequenceName = "CUSTOMER_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @javax.persistence.Column(name = "CUSTOMER_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "FIRST_NAME", columnDefinition = "VARCHAR2(128)", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "LAST_NAME", columnDefinition = "VARCHAR2(128)", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "MOBILE_NUMBER", columnDefinition = "VARCHAR2(32) default 'No Mobile Number'", nullable = false)
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PHONE_NUMBER", columnDefinition = "VARCHAR2(32) default 'No phone Number'", nullable = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "EMAIL", columnDefinition = "VARCHAR2(64) default 'No Email Address'", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "ADDRESS", columnDefinition = "VARCHAR2(1024) default ''", nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "USERNAME", columnDefinition = "VARCHAR2(128)", nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PASSWORD", columnDefinition = "VARCHAR2(32)", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "SUBS_NUMBER", columnDefinition = "NUMBER(10,0)", nullable = false)
    public Long getSubsNumber() {
        return subsNumber;
    }

    public void setSubsNumber(Long subsNumber) {
        this.subsNumber = subsNumber;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "DISCOUNT", columnDefinition = "NUMBER(4,0) default 0", nullable = false)
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @javax.persistence.Enumerated(EnumType.ORDINAL)
    public Activation getActivate() {
        return activate;
    }

    public void setActivate(Activation activate) {
        this.activate = activate;
    }

    @javax.persistence.Version
    @javax.persistence.Column(name = "TIME_STAMP", columnDefinition = "TIMESTAMP(6)", nullable = false)
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }


    @javax.persistence.Transient
    public String fullName() {
        return String.join(PizzaProps.PublicText.SPACE, this.firstName, this.lastName);
    }

    @PrePersist
    private void preInsert() {

        if (this.activate == null) {
            this.activate = Activation.Active;
        }

        if (this.discount == null) {
            this.discount = 0.0d;
        }

        if (this.email == null || StringUtils.isBlank(this.email)) {
            this.email = "No Email Address";
        }

        if (this.address == null || StringUtils.isBlank(this.address)) {
            this.address = "";
        }

        if (this.phoneNumber == null || StringUtils.isBlank(this.phoneNumber)) {
            this.phoneNumber = "No Phone Address";
        }

        if (this.mobileNumber == null || StringUtils.isBlank(this.mobileNumber)) {
            this.mobileNumber = "No Mobile Number";
        }

    }

}
