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
@javax.persistence.Table(name = "ORDER_DETAILS", schema = "PIZZA")
public class OrderDetailsEntity implements Serializable {


    private Long orderDetailsId;
    private CustomerOrderEntity customerOrder;
    private PizzaEntity pizzaEntity;
    private Integer pizzaNumber;
    private DrinkEntity drinkEntity;
    private Integer drinkNumber;
    private Activation activate;
    private Timestamp timeStamp;


    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "order_details_seq", sequenceName = "ORDER_DETAILS_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_details_seq")
    @javax.persistence.Column(name = "ORDER_DETAILS_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Long orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "ORDER_ID")
    public CustomerOrderEntity getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrderEntity customerOrder) {
        this.customerOrder = customerOrder;
    }

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "PIZZA_ID")
    public PizzaEntity getPizzaEntity() {
        return pizzaEntity;
    }

    public void setPizzaEntity(PizzaEntity pizzaEntity) {
        this.pizzaEntity = pizzaEntity;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_NUMBER", columnDefinition = "NUMBER(4,0)", nullable = false)
    public Integer getPizzaNumber() {
        return pizzaNumber;
    }

    public void setPizzaNumber(Integer pizzaNumber) {
        this.pizzaNumber = pizzaNumber;
    }

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "DRINK_ID")
    public DrinkEntity getDrinkEntity() {
        return drinkEntity;
    }

    public void setDrinkEntity(DrinkEntity drinkEntity) {
        this.drinkEntity = drinkEntity;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "DRINK_NUMBER", columnDefinition = "NUMBER(4,0)", nullable = false)
    public Integer getDrinkNumber() {
        return drinkNumber;
    }

    public void setDrinkNumber(Integer drinkNumber) {
        this.drinkNumber = drinkNumber;
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
