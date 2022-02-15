package model.entity;

import core.Activation;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotNull;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Timestamp;

@LocalBean
@Stateful

@javax.persistence.Entity
@javax.persistence.Table(name = "PIZZA", schema = "PIZZA")
public class PizzaEntity implements Serializable {

    private Long pizzaId;
    @NotNull
    @Length(min = 3, max = 128, message = "invalid pizza name. length(3 - 128)")
    private String pizzaName;
    @NotNull
    @Length(min = 3, max = 1024, message = "invalid pizza name. length(3 - 1024)")
    private String pizzaDesc;
    @NotNull
    @Min(value = 1000, message = "Min number is 1000")
    @Max(value = 9999, message = "max number is 9999")
    private Integer pizzaCode;
    @NotNull
    private byte[] pizzaPic;
    @NotNull
    private PizzaSizeEntity pizzaSizeEntity;
    @NotNull
    private Double pizzaPrice;
    private Activation activate;
    private Timestamp timeStamp;

    @javax.persistence.Transient
    private StreamedContent streamedContent;

    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "pizza_seq", sequenceName = "PIZZA_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pizza_seq")
    @javax.persistence.Column(name = "PIZZA_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_NAME", columnDefinition = "VARCHAR2(128)", nullable = false)
    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_DESC", columnDefinition = "VARCHAR2(1024)", nullable = false)
    public String getPizzaDesc() {
        return pizzaDesc;
    }

    public void setPizzaDesc(String pizzaDesc) {
        this.pizzaDesc = pizzaDesc;
    }


    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_CODE", columnDefinition = "NUMBER(4.0)", nullable = false)
    public Integer getPizzaCode() {
        return pizzaCode;
    }

    public void setPizzaCode(Integer pizzaCode) {
        this.pizzaCode = pizzaCode;
    }

    @javax.persistence.Lob
    @javax.persistence.Basic(fetch = FetchType.LAZY)
    @javax.persistence.Column(name = "PIZZA_IMAGE", columnDefinition = "BLOB", nullable = false)
    public byte[] getPizzaPic() {
        return pizzaPic;
    }

    public void setPizzaPic(byte[] pizzaPic) {
        this.pizzaPic = pizzaPic;
    }

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "PIZZA_SIZE_ID")
    public PizzaSizeEntity getPizzaSize() {
        return pizzaSizeEntity;
    }

    public void setPizzaSize(PizzaSizeEntity pizzaSizeEntity) {
        this.pizzaSizeEntity = pizzaSizeEntity;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_PRICE", columnDefinition = "NUMBER(5,2)", nullable = false)
    public Double getPizzaPrice() {
        return pizzaPrice;
    }

    public void setPizzaPrice(Double pizzaPrice) {
        this.pizzaPrice = pizzaPrice;
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

    @javax.persistence.Transient
    public StreamedContent getStreamedContent() {
        return new DefaultStreamedContent(new ByteArrayInputStream(this.getPizzaPic()),"image/jpeg");
    }

    @PrePersist
    private void preInsert() {
        if (this.activate == null) {
            this.activate = Activation.Active;
        }
    }


}
