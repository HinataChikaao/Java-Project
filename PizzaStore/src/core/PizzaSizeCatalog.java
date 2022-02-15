package core;

import model.bl.PizzaSizeMgr;
import model.entity.PizzaSizeEntity;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@LocalBean
@Stateless
public class PizzaSizeCatalog implements Serializable {

    @EJB(name = "PizzaSizeMgr")
    private PizzaSizeMgr pizzaSizeMgr;

    private List<PizzaSizeEntity> pizzaSizeEntityList;

    @PostConstruct
    private void init() {
        pizzaSizeEntityList = new ArrayList<>();
    }

    private void loadPizzaSize() {
        try {
            pizzaSizeEntityList = pizzaSizeMgr.selectPizzaSize(Activation.Active);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PizzaSizeEntity> getPizzaSizeEntityList() {
        return pizzaSizeEntityList;
    }
}
