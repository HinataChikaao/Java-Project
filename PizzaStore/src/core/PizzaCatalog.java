package core;

import model.bl.PizzaMgr;
import model.entity.PizzaEntity;
import model.entity.core.PizzaDistinct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@LocalBean
@Stateless
public class PizzaCatalog implements Serializable {

    private Logger logger = LoggerFactory.getLogger(PizzaCatalog.class);

    @EJB(name = "PizzaMgr")
    private PizzaMgr pizzaMgr;

    private List<PizzaEntity> pizzaEntityList;
    private List<PizzaDistinct> pizzaDistinctList;
    private Map<Long, PizzaEntity> pizzaEntityMap;


    @PostConstruct
    private void init() {
        pizzaEntityMap = new HashMap<>();
        loadImages();
        loadPizzaDistinct();
    }

    private void loadImages() {
        try {
            pizzaEntityList = pizzaMgr.selectPizza(Activation.Active);
            pizzaEntityList.forEach(item -> pizzaEntityMap.put(item.getPizzaId(), item));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void loadPizzaDistinct() {
        try {
            pizzaDistinctList =  pizzaMgr.selectPizzaDistinct(Activation.Active);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    public void update() {
        loadImages();
    }


    /* -------------------------------------------- */

    public List<PizzaEntity> getPizzaEntityList() {
        return pizzaEntityList;
    }

    public Map<Long, PizzaEntity> getPizzaEntityMap() {
        return pizzaEntityMap;
    }

    public List<PizzaDistinct> getPizzaDistinctList() {
        return pizzaDistinctList;
    }
}
