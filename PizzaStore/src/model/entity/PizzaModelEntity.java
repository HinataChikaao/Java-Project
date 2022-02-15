package model.entity;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.sql.Timestamp;

@LocalBean
@Stateful

@javax.persistence.Entity
@javax.persistence.Table(name = "PIZZA_MODEL", schema = "PIZZA")
public class PizzaModelEntity implements Serializable {

    private Long pizzaModelId;
    private String pizzaModelName;
    private Integer PizzaModelCode;
    private Timestamp timeStamp;

    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "pizza_model_seq", sequenceName = "PIZZA_MODEL_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pizza_model_seq")
    @javax.persistence.Column(name = "PIZZA_MODEL_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getPizzaModelId() {
        return pizzaModelId;
    }

    public void setPizzaModelId(Long pizzaModelId) {
        this.pizzaModelId = pizzaModelId;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_MODEL_NAME", columnDefinition = "VARCHAR2(128)", nullable = false)
    public String getPizzaModelName() {
        return pizzaModelName;
    }

    public void setPizzaModelName(String pizzaModelName) {
        this.pizzaModelName = pizzaModelName;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_MODEL_CODE", columnDefinition = "NUMBER(4,0)", nullable = false, unique = true)
    public Integer getPizzaModelCode() {
        return PizzaModelCode;
    }

    public void setPizzaModelCode(Integer pizzaModelCode) {
        PizzaModelCode = pizzaModelCode;
    }

    @javax.persistence.Version
    @javax.persistence.Column(name = "TIME_STAMP", columnDefinition = "TIMESTAMP(6)", nullable = false)
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
