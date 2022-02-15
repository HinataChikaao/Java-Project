package model.bl;

import core.Activation;
import model.entity.DiscountEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class DiscountMgr extends CDIBaseModel implements Serializable {

    @Transactional
    public List<DiscountEntity> selectDiscount(Activation activation) throws Exception {
        return entityManager.createQuery("from DiscountEntity dis where dis.activate = :active",
                DiscountEntity.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public DiscountEntity selectDiscountById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from DiscountEntity dis where dis.discountId = :dId and dis.activate = :active",
                DiscountEntity.class)
                .setParameter("active", activation)
                .setParameter("dId", id).getSingleResult();
    }

    @Transactional
    public DiscountEntity findDiscount(DiscountEntity discountEntity) throws Exception {
        return entityManager.find(DiscountEntity.class, discountEntity.getDiscountId());
    }

    @Transactional
    public void insertDiscount(DiscountEntity discountEntity) throws Exception {
        entityManager.persist(discountEntity);
    }

    @Transactional
    public void insertDiscount(List<DiscountEntity> discountEntityList) throws Exception {
        discountEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deleteDiscount(DiscountEntity discountEntity) throws Exception {
        entityManager.remove(discountEntity);
    }

    @Transactional
    public void deleteDiscount(List<DiscountEntity> discountEntityList) throws Exception {
        discountEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updateDiscount(DiscountEntity discountEntity) throws Exception {
        DiscountEntity discount = entityManager.find(DiscountEntity.class, discountEntity.getDiscountId());
        discount.setActivate(discountEntity.getActivate());
        discount.setAmount(discountEntity.getAmount());
        discount.setCustomerEntity(discountEntity.getCustomerEntity());
        discount.setDiscountDate(discountEntity.getDiscountDate());
        discount.setDiscountTime(discountEntity.getDiscountTime());

    }

    @Transactional
    public void updateDiscount(List<DiscountEntity> discountEntityList) throws Exception {
        for (DiscountEntity DiscountEntity : discountEntityList) {
            updateDiscount(DiscountEntity);
        }
    }

    @Transactional
    public boolean existDiscount(DiscountEntity discountEntity) throws Exception {
        return entityManager.find(DiscountEntity.class, discountEntity.getDiscountId()) != null;
    }
}
