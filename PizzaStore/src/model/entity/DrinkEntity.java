package model.entity;


import core.Activation;
import net.sf.oval.constraint.Length;
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
@javax.persistence.Table(name = "DRINK", schema = "PIZZA")
public class DrinkEntity implements Serializable {

    private Long drinkId;
    @NotNull
    @Length(min = 3, max = 128, message = "invalid pizza name. length(3 - 128)")
    private String drinkName;
    @NotNull
    @Length(min = 3, max = 1024, message = "invalid pizza name. length(3 - 1024)")
    private String drinkDesc;
    @NotNull
    private byte[] drinkPic;
    @NotNull
    private Double drinkPrice;
    private Activation activate;
    private Timestamp timeStamp;

    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "drink_seq", sequenceName = "DRINK_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drink_seq")
    @javax.persistence.Column(name = "DRINK_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(Long drinkId) {
        this.drinkId = drinkId;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "DRINK_NAME", columnDefinition = "VARCHAR2(128)", nullable = false)
    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "DRINK_DESC", columnDefinition = "VARCHAR2(1024)", nullable = false)
    public String getDrinkDesc() {
        return drinkDesc;
    }

    public void setDrinkDesc(String drinkDesc) {
        this.drinkDesc = drinkDesc;
    }

    @javax.persistence.Lob
    @javax.persistence.Basic(fetch = FetchType.LAZY)
    @javax.persistence.Column(name = "DRINK_IMAGE", columnDefinition = "BLOB", nullable = false)
    public byte[] getDrinkPic() {
        return drinkPic;
    }

    public void setDrinkPic(byte[] drinkPic) {
        this.drinkPic = drinkPic;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "DRINK_PRICE", columnDefinition = "NUMBER(5,2)", nullable = false)
    public Double getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(Double drinkPrice) {
        this.drinkPrice = drinkPrice;
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
        return new DefaultStreamedContent(new ByteArrayInputStream(this.getDrinkPic()),"image/jpeg");
    }

    @PrePersist
    private void preInsert() {
        if (this.activate == null) {
            this.activate = Activation.Active;
        }
    }

}
