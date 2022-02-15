package controller;

import core.Activation;
import core.BaseController;
import core.PizzaCatalog;
import core.Validation;
import model.bl.PizzaMgr;
import model.bl.PizzaModelMgr;
import model.bl.PizzaSizeMgr;
import model.entity.PizzaEntity;
import model.entity.PizzaModelEntity;
import model.entity.PizzaSizeEntity;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PizzaCtrl extends BaseController implements Serializable {


    private Logger logger = LoggerFactory.getLogger(PizzaCtrl.class);


    @EJB(name = "PizzaImages")
    private PizzaCatalog pizzaCatalog;

    @EJB(name = "PizzaMgr")
    private PizzaMgr pizzaMgr;

    @EJB(name = "PizzaModelMgr")
    private PizzaModelMgr pizzaModelMgr;

    @EJB(name = "PizzaSizeMgr")
    private PizzaSizeMgr pizzaSizeMgr;

    @EJB(name = "PizzaEntity")
    private PizzaEntity pizzaEntity;

    @EJB(name = "PizzaModelEntity")
    private PizzaModelEntity pizzaModelEntity;

    @EJB(name = "Validation")
    private Validation validation;


    private String picFileName;
    private String pizzaName;
    private String pizzaDesc;
    private Integer pizzaCode;
    private Integer pizzaModelCode;
    private byte[] pizzaImage;
    private Long pizzaSizeId;
    private List<PizzaSizeEntity> pizzaSizeEntityList;
    private List<PizzaModelEntity> pizzaModelEntityList;
    private Double pizzaPrice;

    @PostConstruct
    private void init() {
        reset();
        loadPizzaSize();
        loadPizzaModel();
    }

    private void reset() {
        this.pizzaEntity = new PizzaEntity();
        this.pizzaModelEntity = new PizzaModelEntity();
        this.picFileName = null;
        this.pizzaName = null;
        this.pizzaDesc = null;
        this.pizzaImage = null;
        this.pizzaPrice = null;
        this.pizzaCode = null;
        this.pizzaModelCode = null;
    }

    private void loadPizzaModel() {
        try {
            this.pizzaModelEntityList = pizzaModelMgr.selectPizzaModel();
        } catch (Exception e) {
            showErrMessage("Error to load Pizza Model list ...", e);
        }
    }

    private void loadPizzaSize() {
        try {
            pizzaSizeEntityList = pizzaSizeMgr.selectPizzaSize(Activation.Active);
        } catch (Exception e) {
            showErrMessage("Error to load Pizza Size list ...", e);
        }
    }


    public void loadPizzaModelByCode() {
        try {
            pizzaModelEntity = pizzaModelMgr.selectPizzaByCode(this.pizzaModelCode);
        } catch (Exception e) {
            showErrMessage("Error to load Pizza Code ...", e);
        }
    }


    public void save() {
        PizzaSizeEntity pizzaSizeEntity =
                pizzaSizeEntityList.stream()
                        .filter(item -> item.getPizzaSizeId().equals(this.pizzaSizeId))
                        .findAny().orElse(null);

        String validator = validation.validator(pizzaSizeEntity);
        if (!StringUtils.isBlank(validator)) {
            showErrMessage(validator);
            return;
        }

        pizzaEntity.setPizzaDesc(this.pizzaDesc);
        pizzaEntity.setPizzaName(this.pizzaModelEntity.getPizzaModelName());
        pizzaEntity.setPizzaCode(this.pizzaModelEntity.getPizzaModelCode());
        pizzaEntity.setPizzaPic(this.pizzaImage);
        pizzaEntity.setPizzaPrice(this.pizzaPrice);
        pizzaEntity.setPizzaSize(pizzaSizeEntity);

        String validator2 = validation.validator(pizzaEntity);
        if (!StringUtils.isBlank(validator2)) {
            showErrMessage(validator);
            return;
        }

        try {
            pizzaMgr.insertPizza(pizzaEntity);
            pizzaCatalog.update();
            showInfoMessage("Pizza Info is saved ...");
            reset();
        } catch (Exception e) {
            showErrMessage("Pizza Info is not saved ...", e);
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        picFileName = event.getFile().getFileName();
        this.pizzaImage = event.getFile().getContents();
        showInfoMessage(event.getFile().getFileName() + " is uploaded ...");
    }


    /* ------------------------------------- */


    public String getPicFileName() {
        return picFileName;
    }

    public void setPicFileName(String picFileName) {
        this.picFileName = picFileName;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaDesc() {
        return pizzaDesc;
    }

    public void setPizzaDesc(String pizzaDesc) {
        this.pizzaDesc = pizzaDesc;
    }


    public Integer getPizzaCode() {
        return pizzaCode;
    }

    public void setPizzaCode(Integer pizzaCode) {
        this.pizzaCode = pizzaCode;
    }

    public byte[] getPizzaImage() {
        return pizzaImage;
    }

    public void setPizzaImage(byte[] pizzaImage) {
        this.pizzaImage = pizzaImage;
    }


    public Long getPizzaSizeId() {
        return pizzaSizeId;
    }

    public void setPizzaSizeId(Long pizzaSizeId) {
        this.pizzaSizeId = pizzaSizeId;
    }

    public List<PizzaSizeEntity> getPizzaSizeEntityList() {
        return pizzaSizeEntityList;
    }

    public void setPizzaSizeEntityList(List<PizzaSizeEntity> pizzaSizeEntityList) {
        this.pizzaSizeEntityList = pizzaSizeEntityList;
    }

    public Double getPizzaPrice() {
        return pizzaPrice;
    }

    public void setPizzaPrice(Double pizzaPrice) {
        this.pizzaPrice = pizzaPrice;
    }

    public List<PizzaModelEntity> getPizzaModelEntityList() {
        return pizzaModelEntityList;
    }

    public void setPizzaModelEntityList(List<PizzaModelEntity> pizzaModelEntityList) {
        this.pizzaModelEntityList = pizzaModelEntityList;
    }

    public Integer getPizzaModelCode() {
        return pizzaModelCode;
    }

    public void setPizzaModelCode(Integer pizzaModelCode) {
        this.pizzaModelCode = pizzaModelCode;
    }
}
