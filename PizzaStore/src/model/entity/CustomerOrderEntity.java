package model.entity;

import core.Activation;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EnumType;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@LocalBean
@Stateful

@javax.persistence.Entity
@javax.persistence.Table(name = "CUSTOMER_ORDER", schema = "PIZZA")
public class CustomerOrderEntity implements Serializable {


    private Long orderId;
    private CustomerEntity customer;
    private String orderDate;
    private String orderTime;
    private Integer orderDeliver;
    private Activation activate;
    private Timestamp timeStamp;

    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "customer_order_seq", sequenceName = "CUSTOMER_ORDER_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_order_seq")
    @javax.persistence.Column(name = "ORDER_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "CUSTOMER_ID")
    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "ORDER_DATE", columnDefinition = "VARCHAR2(10)", nullable = false)
    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "ORDER_TIME", columnDefinition = "VARCHAR2(10)", nullable = false)
    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "ORDER_DELIVER", columnDefinition = "NUMBER(1,0)", nullable = false)
    public Integer getOrderDeliver() {
        return orderDeliver;
    }

    public void setOrderDeliver(Integer orderDeliver) {
        this.orderDeliver = orderDeliver;
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

        if (this.orderDeliver == null) {
            this.orderDeliver = 0;
        }

        if (this.orderDate == null) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int month = now.getMonth().getValue();
            int day = now.getDayOfMonth();
            this.orderDate = String.join("-",
                    String.valueOf(year), String.valueOf(month), String.valueOf(day));
        }

        if (this.orderTime == null) {
            LocalTime now = LocalTime.now();
            int hour = now.getHour();
            int minute = now.getMinute();
            int second = now.getSecond();
            this.orderTime = String.join(":",
                    String.valueOf(hour), String.valueOf(minute), String.valueOf(second));
        }
    }

}