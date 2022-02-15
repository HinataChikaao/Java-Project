package controller;

import core.BaseController;
import core.DrinkCatalog;
import core.Validation;
import model.bl.DrinkMgr;
import model.entity.DrinkEntity;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class DrinkCtrl extends BaseController implements Serializable {

    private Logger logger = LoggerFactory.getLogger(DrinkCtrl.class);

    @EJB(name = "DrinkImages")
    private DrinkCatalog drinkCatalog;

    @EJB(name = "DrinkMgr")
    private DrinkMgr drinkMgr;

    @EJB(name = "DrinkEntity")
    private DrinkEntity drinkEntity;

    @EJB(name = "Validation")
    private Validation validation;


    private String picFileName;
    private String drinkName;
    private String drinkDesc;
    private byte[] drinkImage;
    private Double drinkPrice;


    @PostConstruct
    private void init() {
        reset();
    }


    private void reset() {
        drinkEntity = new DrinkEntity();
        picFileName = null;
        drinkName = null;
        drinkDesc = null;
        drinkImage = null;
        drinkPrice = null;
    }


    public void save() {

        drinkEntity.setDrinkDesc(this.drinkDesc);
        drinkEntity.setDrinkName(this.drinkName);
        drinkEntity.setDrinkPic(this.drinkImage);
        drinkEntity.setDrinkPrice(this.drinkPrice);

        String validator = validation.validator(drinkEntity);
        if (!StringUtils.isBlank(validator)) {
            showErrMessage(validator);
            return;
        }

        try {
            drinkMgr.insertDrink(drinkEntity);
            drinkCatalog.update();
            showInfoMessage("Drink Info is saved ...");
            reset();
        } catch (Exception e) {
            showErrMessage("Drink Info is not saved ...",e);
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        picFileName = event.getFile().getFileName();
        this.drinkImage = event.getFile().getContents();
        showInfoMessage(event.getFile().getFileName() + " is uploaded ...");
    }


    public String getPicFileName() {
        return picFileName;
    }

    public void setPicFileName(String picFileName) {
        this.picFileName = picFileName;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getDrinkDesc() {
        return drinkDesc;
    }

    public void setDrinkDesc(String drinkDesc) {
        this.drinkDesc = drinkDesc;
    }

    public byte[] getDrinkImage() {
        return drinkImage;
    }

    public void setDrinkImage(byte[] drinkImage) {
        this.drinkImage = drinkImage;
    }

    public Double getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(Double drinkPrice) {
        this.drinkPrice = drinkPrice;
    }
}
