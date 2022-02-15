package model.entity;


import core.Activation;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

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
@javax.persistence.Table(name = "PIZZA_SIZE", schema = "PIZZA")
public class PizzaSizeEntity implements Serializable {

    private Long pizzaSizeId;
    @NotNull
    @Length(min = 3, max = 64, message = "invalid size name length(3 - 64)")
    private String pizzaSizeName;
    @NotNull
    @Length(min = 3, max = 1024, message = "invalid size name length(3 - 1024)")
    private String pizzaSizeDesc;
    private Activation activate;
    private Timestamp timeStamp;

    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "pizza_size_seq", sequenceName = "PIZZA_SIZE_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pizza_size_seq")
    @javax.persistence.Column(name = "PIZZA_SIZE_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getPizzaSizeId() {
        return pizzaSizeId;
    }

    public void setPizzaSizeId(Long pizzaSizeId) {
        this.pizzaSizeId = pizzaSizeId;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_SIZE_NAME", columnDefinition = "VARCHAR2(64)", nullable = false)
    public String getPizzaSizeName() {
        return pizzaSizeName;
    }

    public void setPizzaSizeName(String pizzaSizeName) {
        this.pizzaSizeName = pizzaSizeName;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "PIZZA_SIZE_DESC", columnDefinition = "VARCHAR2(1024)", nullable = false)
    public String getPizzaSizeDesc() {
        return pizzaSizeDesc;
    }

    public void setPizzaSizeDesc(String pizzaSizeDesc) {
        this.pizzaSizeDesc = pizzaSizeDesc;
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
