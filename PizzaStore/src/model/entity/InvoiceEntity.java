package model.entity;

import core.Activation;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EnumType;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.sql.Timestamp;

@LocalBean
@Stateful

@javax.persistence.Entity
@javax.persistence.Table(name = "INVOICE", schema = "PIZZA")
public class InvoiceEntity implements Serializable {


    private String invoiceId;
    private CustomerOrderEntity customerOrderEntity;
    private String totalPrice;
    private Activation activate;
    private Timestamp timeStamp;

    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "invoice_seq", sequenceName = "INVOICE_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_seq")
    @javax.persistence.Column(name = "INVOICE_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "ORDER_ID")
    public CustomerOrderEntity getCustomerOrderEntity() {
        return customerOrderEntity;
    }

    public void setCustomerOrderEntity(CustomerOrderEntity customerOrderEntity) {
        this.customerOrderEntity = customerOrderEntity;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "TOTAL_PRICE", columnDefinition = "NUMBER(10,2)", nullable = false)
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @javax.persistence.Enumerated(EnumType.ORDINAL)
    public Activation getActivate() {
        return activate;
    }

    public void setActivate(Activation activate) {
        this.activate = activate;
    }

    @javax.persistence.Version
    @javax.persistence.Column(name = "TIME_STAMP", columnDefinition = "TIMESTAMP(6)", nullable = false)
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    @PrePersist
    private void preInsert() {
        if (this.activate == null) {
            this.activate = Activation.Active;
        }
    }

}
