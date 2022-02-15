package model.entity.core;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.io.Serializable;

@LocalBean
@Stateful
public class DrinkOrder implements Serializable {

    private Long drinkId;
    private String drinkName;
    private Integer drinkNumber;

    public Long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(Long drinkId) {
        this.drinkId = drinkId;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public Integer getDrinkNumber() {
        return drinkNumber;
    }

    public void setDrinkNumber(Integer drinkNumber) {
        this.drinkNumber = drinkNumber;
    }
}
