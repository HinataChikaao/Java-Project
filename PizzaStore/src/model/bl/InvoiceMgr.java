package model.bl;

import core.Activation;
import model.entity.InvoiceEntity;
import model.pattern.CDIBaseModel;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@LocalBean
@Stateless
public class InvoiceMgr extends CDIBaseModel implements Serializable {

    @Transactional
    public List<InvoiceEntity> selectInvoice(Activation activation) throws Exception {
        return entityManager.createQuery("from InvoiceEntity inv where inv.activate = :active",
                InvoiceEntity.class)
                .setParameter("active", activation)
                .getResultList();
    }

    @Transactional
    public InvoiceEntity selectInvoiceById(long id, Activation activation) throws Exception {
        return entityManager.createQuery("from InvoiceEntity inv where inv.invoiceId = :invId " +
                "and inv.activate = :active", InvoiceEntity.class)
                .setParameter("active", activation)
                .setParameter("invId", id).getSingleResult();
    }

    @Transactional
    public InvoiceEntity findInvoice(InvoiceEntity invoiceEntity) throws Exception {
        return entityManager.find(InvoiceEntity.class, invoiceEntity.getInvoiceId());
    }

    @Transactional
    public void insertInvoice(InvoiceEntity invoiceEntity) throws Exception {
        entityManager.persist(invoiceEntity);
    }

    @Transactional
    public void insertInvoice(List<InvoiceEntity> invoiceEntityList) throws Exception {
        invoiceEntityList.forEach(item -> entityManager.persist(item));
    }

    @Transactional
    public void deleteInvoice(InvoiceEntity invoiceEntity) throws Exception {
        entityManager.remove(invoiceEntity);
    }

    @Transactional
    public void deleteInvoice(List<InvoiceEntity> invoiceEntityList) throws Exception {
        invoiceEntityList.forEach(item -> entityManager.remove(item));
    }

    @Transactional
    public void updateInvoice(InvoiceEntity invoiceEntity) throws Exception {
        InvoiceEntity invoice = entityManager.find(InvoiceEntity.class, invoiceEntity.getInvoiceId());
        invoice.setActivate(invoiceEntity.getActivate());
        invoice.setCustomerOrderEntity(invoiceEntity.getCustomerOrderEntity());
        invoice.setTotalPrice(invoiceEntity.getTotalPrice());
    }

    @Transactional
    public void updateInvoice(List<InvoiceEntity> invoiceEntityList) throws Exception {
        for (InvoiceEntity invoiceEntity : invoiceEntityList) {
            updateInvoice(invoiceEntity);
        }
    }

    @Transactional
    public boolean existInvoice(InvoiceEntity invoiceEntity) throws Exception {
        return entityManager.find(InvoiceEntity.class, invoiceEntity.getInvoiceId()) != null;
    }
}