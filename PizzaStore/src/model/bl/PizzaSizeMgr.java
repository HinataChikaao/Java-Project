package model.bl;

import core.Activation;
import model.entity.PizzaSizeEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class PizzaSizeMgr extends CDIBaseModel implements Serializable {

    @Transactional
    public List<PizzaSizeEntity> selectPizzaSize(Activation activation) throws Exception {
        return entityManager.createQuery("from PizzaSizeEntity psize where psize.activate = :active",
                PizzaSizeEntity.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public PizzaSizeEntity selectPizzaSizeById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from PizzaSizeEntity psize where psize.pizzaSizeId = :psId " +
                "and psize.activate = :active", PizzaSizeEntity.class)
                .setParameter("active", activation)
                .setParameter("psId", id).getSingleResult();
    }

    @Transactional
    public PizzaSizeEntity findPizzaSize(PizzaSizeEntity pizzaSizeEntity) throws Exception {
        return entityManager.find(PizzaSizeEntity.class, pizzaSizeEntity.getPizzaSizeId());
    }

    @Transactional
    public void insertPizzaSize(PizzaSizeEntity pizzaSizeEntity) throws Exception {
        entityManager.persist(pizzaSizeEntity);
    }

    @Transactional
    public void insertPizzaSize(List<PizzaSizeEntity> pizzaSizeEntityList) throws Exception {
        pizzaSizeEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deletePizzaSize(PizzaSizeEntity pizzaSizeEntity) throws Exception {
        entityManager.remove(pizzaSizeEntity);
    }

    @Transactional
    public void deletePizzaSize(List<PizzaSizeEntity> pizzaSizeEntityList) throws Exception {
        pizzaSizeEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updatePizzaSize(PizzaSizeEntity pizzaSizeEntity) throws Exception {
        PizzaSizeEntity pizzaSize = entityManager.find(PizzaSizeEntity.class, pizzaSizeEntity.getPizzaSizeId());
        pizzaSize.setActivate(pizzaSizeEntity.getActivate());
        pizzaSize.setPizzaSizeDesc(pizzaSizeEntity.getPizzaSizeDesc());
        pizzaSize.setPizzaSizeName(pizzaSizeEntity.getPizzaSizeName());
    }

    @Transactional
    public void updatePizzaSize(List<PizzaSizeEntity> pizzaSizeEntityList) throws Exception {
        for (PizzaSizeEntity pizzaSizeEntity : pizzaSizeEntityList) {
            updatePizzaSize(pizzaSizeEntity);
        }
    }

    @Transactional
    public boolean existPizzaSize(PizzaSizeEntity pizzaSizeEntity) throws Exception {
        return entityManager.find(PizzaSizeEntity.class, pizzaSizeEntity.getPizzaSizeId()) != null;
    }
}