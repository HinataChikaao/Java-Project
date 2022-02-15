package model.bl;

import core.Activation;
import model.entity.PizzaEntity;
import model.entity.PizzaSizeEntity;
import model.entity.core.PizzaDistinct;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class PizzaMgr extends CDIBaseModel implements Serializable {

    @Transactional
    public List<PizzaEntity> selectPizza(Activation activation) throws Exception {
        return entityManager.createQuery("from PizzaEntity piz where piz.activate = :active", PizzaEntity.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public PizzaEntity selectPizzaById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from PizzaEntity piz where piz.pizzaId = :pizId " +
                "and piz.activate = :active", PizzaEntity.class)
                .setParameter("active", activation)
                .setParameter("pizId", id).getSingleResult();
    }


    @Transactional
    public PizzaEntity selectPizzaByCodeAndSizeId(Integer code, Long sizeId, Activation activation) throws Exception {
        return entityManager.createQuery("from PizzaEntity piz where piz.pizzaCode = :pizcode and " +
                "piz.pizzaSize.pizzaSizeId = :psid " +
                "and piz.activate = :active", PizzaEntity.class)
                .setParameter("active", activation)
                .setParameter("pizcode", code)
                .setParameter("psid", sizeId).getSingleResult();
    }


    @Transactional
    public List<PizzaDistinct> selectPizzaDistinct(Activation activation) throws Exception {
        Query pizzaDistinct = entityManager.createNativeQuery("select distinct(pizza_name), " +
                "pizza_code from pizza where activate = 1", "pizzaDistinct");
        return pizzaDistinct.getResultList();
    }

    @Transactional
    public List<PizzaSizeEntity> selectPizzaByPizzaCode(Long pizzaCode, Activation activation) throws Exception {
        return entityManager.createQuery("select piz.pizzaSize from PizzaEntity piz " +
                "where piz.activate = :active and piz.pizzaCode = :pzCode", PizzaSizeEntity.class)
                .setParameter("active", activation)
                .setParameter("pzCode", pizzaCode)
                .getResultList();
    }


    @Transactional
    public List<String> selectPizzaImages(Activation activation) throws Exception {
        return entityManager.createQuery("select piz.pizzaPic from PizzaEntity piz where  " +
                " piz.activate = :active", String.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public PizzaEntity findPizza(PizzaEntity pizzaEntity) throws Exception {
        return entityManager.find(PizzaEntity.class, pizzaEntity.getPizzaId());
    }

    @Transactional
    public void insertPizza(PizzaEntity pizzaEntity) throws Exception {
        entityManager.persist(pizzaEntity);
    }

    @Transactional
    public void insertPizza(List<PizzaEntity> pizzaEntityList) throws Exception {
        pizzaEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deletePizza(PizzaEntity pizzaEntity) throws Exception {
        entityManager.remove(pizzaEntity);
    }

    @Transactional
    public void deletePizza(List<PizzaEntity> pizzaEntityList) throws Exception {
        pizzaEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updatePizza(PizzaEntity pizzaEntity) throws Exception {
        PizzaEntity pizza = entityManager.find(PizzaEntity.class, pizzaEntity.getPizzaId());
        pizza.setActivate(pizzaEntity.getActivate());
        pizza.setPizzaDesc(pizzaEntity.getPizzaDesc());
        pizza.setPizzaName(pizzaEntity.getPizzaName());
        pizza.setPizzaPic(pizzaEntity.getPizzaPic());
        pizza.setPizzaPrice(pizzaEntity.getPizzaPrice());
        pizza.setPizzaSize(pizzaEntity.getPizzaSize());
    }

    @Transactional
    public void updatePizza(List<PizzaEntity> pizzaEntityList) throws Exception {
        for (PizzaEntity pizzaEntity : pizzaEntityList) {
            updatePizza(pizzaEntity);
        }
    }

    @Transactional
    public boolean existPizza(PizzaEntity pizzaEntity) throws Exception {
        return entityManager.find(PizzaEntity.class, pizzaEntity.getPizzaId()) != null;
    }
}