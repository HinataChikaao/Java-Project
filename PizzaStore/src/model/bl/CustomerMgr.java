package model.bl;

import core.Activation;
import model.entity.CustomerEntity;
import model.entity.RolesEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@LocalBean
@Stateless
public class CustomerMgr extends CDIBaseModel implements Serializable {

    @EJB(name = "RolesEntity")
    private RolesEntity rolesEntity;

    @Transactional
    public List<CustomerEntity> selectCustomer(Activation activation) throws Exception {
        return entityManager.createQuery("from CustomerEntity cus where cus.activate = :active",
                CustomerEntity.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public CustomerEntity selectCustomerById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from CustomerEntity cus where cus.customerId = :cid and cus.activate = :active",
                CustomerEntity.class)
                .setParameter("cid", id)
                .setParameter("active", activation)
                .getSingleResult();
    }

    @Transactional
    public CustomerEntity selectCustomerByUsername(String username, Activation activation) throws Exception {
        return entityManager.createQuery("from CustomerEntity cus where cus.username = :cun and cus.activate = :active",
                CustomerEntity.class)
                .setParameter("cun", username)
                .setParameter("active", activation)
                .getSingleResult();
    }


    @Transactional
    public CustomerEntity findCustomer(CustomerEntity customerEntity) throws Exception {
        return entityManager.find(CustomerEntity.class, customerEntity.getCustomerId());
    }

    @Transactional
    public void insertCustomer(CustomerEntity customerEntity) throws Exception {
        BigDecimal subsNo = (BigDecimal) entityManager.createNativeQuery("select SUBS_SEQ.nextval from DUAL").getSingleResult();
        rolesEntity = new RolesEntity();
        customerEntity.setSubsNumber(subsNo.longValue());
        rolesEntity.setRoleName("customer");
        rolesEntity.setUsername(customerEntity.getUsername());
        entityManager.persist(customerEntity);
        entityManager.persist(rolesEntity);
    }

    @Transactional
    public void insertCustomer(List<CustomerEntity> customerEntityList) throws Exception {
        customerEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deleteCustomer(CustomerEntity customerEntity) throws Exception {
        entityManager.remove(customerEntity);
    }

    @Transactional
    public void deleteCustomer(List<CustomerEntity> customerEntityList) throws Exception {
        customerEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updateCustomer(CustomerEntity customerEntity) throws Exception {
        CustomerEntity customer = entityManager.find(CustomerEntity.class, customerEntity.getCustomerId());
        customer.setActivate(customerEntity.getActivate());
        customer.setCustomerId(customerEntity.getCustomerId());
        customer.setDiscount(customerEntity.getDiscount());
        customer.setEmail(customerEntity.getEmail());
        customer.setFirstName(customerEntity.getFirstName());
        customer.setLastName(customerEntity.getLastName());
        customer.setMobileNumber(customerEntity.getMobileNumber());
        customer.setPhoneNumber(customerEntity.getPhoneNumber());
        customer.setSubsNumber(customerEntity.getSubsNumber());
    }

    @Transactional
    public void updateCustomer(List<CustomerEntity> customerEntityList) throws Exception {
        for (CustomerEntity customerEntity : customerEntityList) {
            updateCustomer(customerEntity);
        }
    }

    @Transactional
    public boolean existCustomer(CustomerEntity customerEntity) throws Exception {
        return entityManager.find(CustomerEntity.class, customerEntity.getCustomerId()) != null;
    }


    @Transactional
    public boolean existUsername(String username) throws Exception {
        List<CustomerEntity> customerList = entityManager.createQuery("from CustomerEntity cus " +
                "where cus.username = :un", CustomerEntity.class)
                .setParameter("un", username).getResultList();
        return customerList != null && customerList.size() != 0;
    }

}
