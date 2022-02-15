package model.entity.core;

import javax.persistence.*;
import java.io.Serializable;

@SqlResultSetMapping(
        name = "pizzaDistinct",
        entities = {
                @EntityResult(entityClass = PizzaDistinct.class, fields = {
                        @FieldResult(name = "pizzaName", column = "pizza_name"),
                        @FieldResult(name = "pizzaCode", column = "pizza_code")
                }),
        }
)


@javax.persistence.Entity
public class PizzaDistinct implements Serializable {

    @javax.persistence.Id
    private String pizzaName;
    private Integer pizzaCode;


    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public Integer getPizzaCode() {
        return pizzaCode;
    }

    public void setPizzaCode(Integer pizzaCode) {
        this.pizzaCode = pizzaCode;
    }
}
