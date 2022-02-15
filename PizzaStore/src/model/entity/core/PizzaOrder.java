package model.entity.core;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.io.Serializable;

@LocalBean
@Stateful
public class PizzaOrder implements Serializable {

    private Long pizzaId;
    private String pizzaName;
    private Long pizzaSizeId;
    private String pizzaSizeName;
    private Integer pizzaNumber;


    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public Long getPizzaSizeId() {
        return pizzaSizeId;
    }

    public void setPizzaSizeId(Long pizzaSizeId) {
        this.pizzaSizeId = pizzaSizeId;
    }

    public String getPizzaSizeName() {
        return pizzaSizeName;
    }

    public void setPizzaSizeName(String pizzaSizeName) {
        this.pizzaSizeName = pizzaSizeName;
    }

    public Integer getPizzaNumber() {
        return pizzaNumber;
    }

    public void setPizzaNumber(Integer pizzaNumber) {
        this.pizzaNumber = pizzaNumber;
    }
}
