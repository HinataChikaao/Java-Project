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
@javax.persistence.Table(name = "INVOICE_DETAILS", schema = "PIZZA")
public class InvoiceDetailsEntity implements Serializable {

    private Long invoiceDetailsId;
    private InvoiceEntity invoiceEntity;
    private String orderDesc;
    private Integer orderNumber;
    private Double unitPrice;
    private Double totalPrice;
    private Activation activate;
    private Timestamp timeStamp;

    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "_seq", sequenceName = "ORDER_DETAILS_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_seq")
    @javax.persistence.Column(name = "INVOICE_DETAILS_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getInvoiceDetailsId() {
        return invoiceDetailsId;
    }

    public void setInvoiceDetailsId(Long invoiceDetailsId) {
        this.invoiceDetailsId = invoiceDetailsId;
    }


    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "INVOICE_ID")
    public InvoiceEntity getInvoiceEntity() {
        return invoiceEntity;
    }

    public void setInvoiceEntity(InvoiceEntity invoiceEntity) {
        this.invoiceEntity = invoiceEntity;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "ORDER_DESC", columnDefinition = "VARCHAR2(1024)", nullable = false)
    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "ORDER_NUMBER", columnDefinition = "NUMBER(4,0)", nullable = false)
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "UNIT_PRICE", columnDefinition = "NUMBER(5,2)", nullable = false)
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "TOTAL_PRICE", columnDefinition = "NUMBER(10,2)", nullable = false)
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
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
