package model.bl;


import model.entity.PizzaModelEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class PizzaModelMgr extends CDIBaseModel implements Serializable {

    @Transactional
    public List<PizzaModelEntity> selectPizzaModel() throws Exception {
        return entityManager.createQuery("from PizzaModelEntity pmodel", PizzaModelEntity.class)
                .getResultList();
    }

    @Transactional
    public PizzaModelEntity selectPizzaById(long id) throws Exception {
        return entityManager.createQuery("from PizzaModelEntity pmodel where pmodel.pizzaModelId = :pmId ", PizzaModelEntity.class)
                .setParameter("pmId", id).getSingleResult();
    }

    @Transactional
    public PizzaModelEntity selectPizzaByCode(Integer code) throws Exception {
        return entityManager.createQuery("from PizzaModelEntity pmodel where pmodel.pizzaModelCode = :pmCode ",
                PizzaModelEntity.class).setParameter("pmCode", code).getSingleResult();
    }

    @Transactional
    public boolean existPizza(PizzaModelEntity pizzaModelEntity) throws Exception {
        return entityManager.find(PizzaModelEntity.class, pizzaModelEntity.getPizzaModelId()) != null;
    }

}
