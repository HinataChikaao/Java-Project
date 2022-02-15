package model.bl;

import core.Activation;
import model.entity.DrinkEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class DrinkMgr extends CDIBaseModel implements Serializable {

    @Transactional
    public List<DrinkEntity> selectDrink(Activation activation) throws Exception {
        return entityManager.createQuery("from DrinkEntity drk where drk.activate = :active",
                DrinkEntity.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public DrinkEntity selectDrinkById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from DrinkEntity drk where drk.drinkId = :drkId and drk.activate = :active",
                DrinkEntity.class).setParameter("drkId", id)
                .setParameter("active", activation)
                .getSingleResult();
    }

    @Transactional
    public DrinkEntity findDrink(DrinkEntity drinkEntity) throws Exception {
        return entityManager.find(DrinkEntity.class, drinkEntity.getDrinkId());
    }

    @Transactional
    public void insertDrink(DrinkEntity drinkEntity) throws Exception {
        entityManager.persist(drinkEntity);
    }

    @Transactional
    public void insertDrink(List<DrinkEntity> drinkEntityList) throws Exception {
        drinkEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deleteDrink(DrinkEntity drinkEntity) throws Exception {
        entityManager.remove(drinkEntity);
    }

    @Transactional
    public void deleteDrink(List<DrinkEntity> drinkEntityList) throws Exception {
        drinkEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updateDrink(DrinkEntity drinkEntity) throws Exception {
        DrinkEntity drink = entityManager.find(DrinkEntity.class, drinkEntity.getDrinkId());
        drink.setActivate(drinkEntity.getActivate());
        drink.setDrinkDesc(drinkEntity.getDrinkDesc());
        drink.setDrinkName(drinkEntity.getDrinkName());
        drink.setDrinkPic(drinkEntity.getDrinkPic());
        drink.setDrinkPrice(drinkEntity.getDrinkPrice());
    }

    @Transactional
    public void updateDrink(List<DrinkEntity> drinkEntityList) throws Exception {
        for (DrinkEntity drinkEntity : drinkEntityList) {
            updateDrink(drinkEntity);
        }
    }

    @Transactional
    public boolean existDrink(DrinkEntity drinkEntity) throws Exception {
        return entityManager.find(DrinkEntity.class, drinkEntity.getDrinkId()) != null;
    }
}
