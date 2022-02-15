package core;

import model.bl.DrinkMgr;
import model.entity.DrinkEntity;
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
public class DrinkCatalog implements Serializable {

    private Logger logger = LoggerFactory.getLogger(DrinkCatalog.class);


    @EJB(name = "DrinkMgr")
    private DrinkMgr drinkMgr;

    private List<DrinkEntity> drinkEntityList;
    private Map<Long, DrinkEntity> drinkEntityMap;


    @PostConstruct
    private void init() {
        drinkEntityMap = new HashMap<>();
        loadImages();
    }

    private void loadImages() {

        try {
            drinkEntityList = drinkMgr.selectDrink(Activation.Active);
            drinkEntityList.forEach(item -> drinkEntityMap.put(item.getDrinkId(), item));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void update() {
        loadImages();
    }

    /* --------------------------------- */

    public List<DrinkEntity> getDrinkEntityList() {
        return drinkEntityList;
    }

    public Map<Long, DrinkEntity> getDrinkEntityMap() {
        return drinkEntityMap;
    }
}
