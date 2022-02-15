package model.bl;

import core.Activation;
import model.entity.CustomerOrderEntity;
import model.entity.DrinkEntity;
import model.entity.OrderDetailsEntity;
import model.entity.PizzaEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class OrderDetailsMgr extends CDIBaseModel implements Serializable {
    @Transactional
    public List<OrderDetailsEntity> selectOrderDetails(Activation activation) throws Exception {
        return entityManager.createQuery("from OrderDetailsEntity ordDtl where ordDtl.activate = :active",
                OrderDetailsEntity.class)
                .setParameter("active", Activation.Active)
                .getResultList();
    }

    @Transactional
    public OrderDetailsEntity selectOrderDetailsById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from OrderDetailsEntity ordDtl where ordDtl.orderDetailsId = :odId " +
                "and ordDtl.activate = :active", OrderDetailsEntity.class)
                .setParameter("active", Activation.Active)
                .setParameter("odId", id).getSingleResult();
    }

    @Transactional
    public List<OrderDetailsEntity> selectOrderDetailsOrderId(CustomerOrderEntity customerOrderEntity, Activation activation) throws Exception {
        return entityManager.createQuery("from OrderDetailsEntity ordDtl " +
                "where ordDtl.customerOrder.orderId = :coId and ordDtl.activate = :active", OrderDetailsEntity.class)
                .setParameter("active", activation)
                .setParameter("coId", customerOrderEntity.getOrderId())
                .getResultList();
    }


    @Transactional
    public OrderDetailsEntity selectOrderDetailsPizzaId(PizzaEntity pizzaEntity) throws Exception {
        return entityManager.createQuery("from OrderDetailsEntity od where od.pizzaEntity.pizzaId = :pzId",
                OrderDetailsEntity.class)
                .setParameter("pzId", pizzaEntity.getPizzaId()).getSingleResult();
    }


    @Transactional
    public OrderDetailsEntity selectOrderDetailsDrinkId(DrinkEntity drinkEntity) throws Exception {
        return entityManager.createQuery("from OrderDetailsEntity od where od.drinkEntity.drinkId = :dId",
                OrderDetailsEntity.class)
                .setParameter("dId", drinkEntity.getDrinkId()).getSingleResult();
    }

    @Transactional
    public OrderDetailsEntity findOrderDetails(OrderDetailsEntity orderDetailsEntity) throws Exception {
        return entityManager.find(OrderDetailsEntity.class, orderDetailsEntity.getOrderDetailsId());
    }

    @Transactional
    public void insertOrderDetails(OrderDetailsEntity orderDetailsEntity) throws Exception {
        entityManager.persist(orderDetailsEntity);
    }

    @Transactional
    public void insertOrderDetails(List<OrderDetailsEntity> orderDetailsEntityList) throws Exception {
        orderDetailsEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deleteOrderDetails(OrderDetailsEntity orderDetailsEntity) throws Exception {
        entityManager.remove(orderDetailsEntity);
    }

    @Transactional
    public void deleteOrderDetails(List<OrderDetailsEntity> OrderDetailsEntityList) throws Exception {
        OrderDetailsEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updateOrderDetails(OrderDetailsEntity orderDetailsEntity) throws Exception {
        OrderDetailsEntity orderDetails = entityManager.find(OrderDetailsEntity.class, orderDetailsEntity.getOrderDetailsId());
        orderDetails.setActivate(orderDetailsEntity.getActivate());
        orderDetails.setCustomerOrder(orderDetailsEntity.getCustomerOrder());
        orderDetails.setDrinkEntity(orderDetailsEntity.getDrinkEntity());
        orderDetails.setDrinkNumber(orderDetailsEntity.getDrinkNumber());
        orderDetails.setPizzaEntity(orderDetailsEntity.getPizzaEntity());
        orderDetails.setPizzaNumber(orderDetailsEntity.getPizzaNumber());
    }

    @Transactional
    public void updateOrderDetails(List<OrderDetailsEntity> orderDetailsEntityList) throws Exception {
        for (OrderDetailsEntity OrderDetailsEntity : orderDetailsEntityList) {
            updateOrderDetails(OrderDetailsEntity);
        }
    }

    @Transactional
    public boolean existOrderDetails(OrderDetailsEntity orderDetailsEntity) throws Exception {
        return entityManager.find(OrderDetailsEntity.class, orderDetailsEntity.getOrderDetailsId()) != null;
    }
}