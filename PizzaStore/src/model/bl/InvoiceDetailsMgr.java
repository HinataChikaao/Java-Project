package model.bl;

import core.Activation;
import model.entity.InvoiceDetailsEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class InvoiceDetailsMgr extends CDIBaseModel implements Serializable {

    @Transactional
    public List<InvoiceDetailsEntity> selectInvoiceDetailsEntity(Activation activation) throws Exception {
        return entityManager.createQuery("from InvoiceDetailsEntity invd where invd.activate = :active",
                InvoiceDetailsEntity.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public InvoiceDetailsEntity selectInvoiceDetailsById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from InvoiceDetailsEntity invd where invd.invoiceDetailsId = :invdId " +
                "and invd.activate = :active", InvoiceDetailsEntity.class)
                .setParameter("active", activation)
                .setParameter("invdId", id).getSingleResult();
    }

    @Transactional
    public InvoiceDetailsEntity findInvoiceDetails(InvoiceDetailsEntity invoiceDetailsEntity) throws Exception {
        return entityManager.find(InvoiceDetailsEntity.class, invoiceDetailsEntity.getInvoiceDetailsId());
    }

    @Transactional
    public void insertInvoiceDetails(InvoiceDetailsEntity invoiceDetailsEntity) throws Exception {
        entityManager.persist(invoiceDetailsEntity);
    }

    @Transactional
    public void insertInvoiceDetails(List<InvoiceDetailsEntity> invoiceDetailsEntityList) throws Exception {
        invoiceDetailsEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deleteInvoiceDetails(InvoiceDetailsEntity invoiceDetailsEntity) throws Exception {
        entityManager.remove(invoiceDetailsEntity);
    }

    @Transactional
    public void deleteInvoiceDetails(List<InvoiceDetailsEntity> invoiceDetailsEntityList) throws Exception {
        invoiceDetailsEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updateInvoiceDetails(InvoiceDetailsEntity invoiceDetailsEntity) throws Exception {
        InvoiceDetailsEntity invDetails = entityManager.find(InvoiceDetailsEntity.class, invoiceDetailsEntity.getInvoiceDetailsId());
        invDetails.setActivate(invoiceDetailsEntity.getActivate());
        invDetails.setInvoiceEntity(invoiceDetailsEntity.getInvoiceEntity());
        invDetails.setOrderDesc(invoiceDetailsEntity.getOrderDesc());
        invDetails.setOrderNumber(invoiceDetailsEntity.getOrderNumber());
        invDetails.setTotalPrice(invoiceDetailsEntity.getTotalPrice());
        invDetails.setUnitPrice(invoiceDetailsEntity.getUnitPrice());
    }

    @Transactional
    public void updateInvoiceDetails(List<InvoiceDetailsEntity> invoiceDetailsEntityList) throws Exception {
        for (InvoiceDetailsEntity invoiceDetailsEntity : invoiceDetailsEntityList) {
            updateInvoiceDetails(invoiceDetailsEntity);
        }
    }

    @Transactional
    public boolean existInvoiceDetails(InvoiceDetailsEntity invoiceDetailsEntity) throws Exception {
        return entityManager.find(InvoiceDetailsEntity.class, invoiceDetailsEntity.getInvoiceDetailsId()) != null;
    }
}