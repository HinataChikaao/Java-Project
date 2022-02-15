package model.entity;

import core.Activation;

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
@javax.persistence.Table(name = "DISCOUNT", schema = "PIZZA")
public class DiscountEntity implements Serializable {


    private Long discountId;
    private CustomerEntity customerEntity;
    private Double amount;
    private String discountDate;
    private String discountTime;
    private Activation activate;
    private Timestamp timeStamp;

    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "discount_seq", sequenceName = "DISCOUNT_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_seq")
    @javax.persistence.Column(name = "DISCOUNT_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "CUSTOMER_ID")
    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "AMOUNT", columnDefinition = "NUMBER(10,2)", nullable = false)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "DISCOUNT_DATE", columnDefinition = "VARCHAR2(10)", nullable = false)
    public String getDiscountDate() {
        return discountDate;
    }

    public void setDiscountDate(String discountDate) {
        this.discountDate = discountDate;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "DISCOUNT_TIME", columnDefinition = "VARCHAR2(10)", nullable = false)
    public String getDiscountTime() {
        return discountTime;
    }

    public void setDiscountTime(String discountTime) {
        this.discountTime = discountTime;
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

    @PrePersist
    private void preInsert() {
        if (this.activate == null) {
            this.activate = Activation.Active;
        }
    }

}
