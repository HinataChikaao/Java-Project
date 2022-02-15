package model.bl;

import core.Activation;
import model.entity.CustomerOrderEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class CustomerOrderMgr extends CDIBaseModel implements Serializable {

    @Transactional
    public List<CustomerOrderEntity> selectCustomerOrder(Activation activation) throws Exception {
        return entityManager.createQuery("from CustomerOrderEntity cusOrd where cusOrd.activate = :active",
                CustomerOrderEntity.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public CustomerOrderEntity selectCustomerOrderById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from CustomerOrderEntity cusOrd where cusOrd.orderId = :cid " +
                "and cusOrd.activate = :active", CustomerOrderEntity.class)
                .setParameter("cid", id)
                .setParameter("active", activation)
                .getSingleResult();
    }

    @Transactional
    public CustomerOrderEntity findCustomerOrder(CustomerOrderEntity customerOrderEntity) throws Exception {
        return entityManager.find(CustomerOrderEntity.class, customerOrderEntity.getOrderId());
    }

    @Transactional
    public void insertCustomerOrder(CustomerOrderEntity CustomerOrderEntity) throws Exception {
        entityManager.persist(CustomerOrderEntity);
    }

    @Transactional
    public void insertCustomerOrder(List<CustomerOrderEntity> customerOrderEntityList) throws Exception {
        customerOrderEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deleteCustomerOrder(CustomerOrderEntity customerOrderEntity) throws Exception {
        entityManager.remove(customerOrderEntity);
    }

    @Transactional
    public void deleteCustomerOrder(List<CustomerOrderEntity> customerOrderEntityList) throws Exception {
        customerOrderEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updateCustomerOrder(CustomerOrderEntity customerOrderEntity) throws Exception {
        CustomerOrderEntity order = entityManager.find(CustomerOrderEntity.class, customerOrderEntity.getOrderId());
        order.setActivate(customerOrderEntity.getActivate());
        order.setCustomer(customerOrderEntity.getCustomer());
        order.setOrderDate(customerOrderEntity.getOrderDate());
        order.setOrderDeliver(customerOrderEntity.getOrderDeliver());
        order.setOrderTime(customerOrderEntity.getOrderTime());
    }

    @Transactional
    public void updateCustomerOrder(List<CustomerOrderEntity> customerOrderEntityList) throws Exception {
        for (CustomerOrderEntity customerOrderEntity : customerOrderEntityList) {
            updateCustomerOrder(customerOrderEntity);
        }
    }

    @Transactional
    public boolean existCustomerOrder(CustomerOrderEntity customerOrderEntity) throws Exception {
        return entityManager.find(CustomerOrderEntity.class, customerOrderEntity.getOrderId()) != null;
    }


}
