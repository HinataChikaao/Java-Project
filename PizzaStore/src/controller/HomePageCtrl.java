package controller;

import core.BaseController;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class HomePageCtrl extends BaseController implements Serializable {

    private Logger logger = LoggerFactory.getLogger(DrinkCtrl.class);

    private String menuButtonName;

    private String btnOpen;
    private String btnClose;

    @PostConstruct
    private void init() {
        reset();
    }

    private void reset() {
        btnOpen = "Menu Open";
        btnClose = "Menu Close";
        menuButtonName = btnOpen;
    }


    public void menuButton() {
        if (menuButtonName.equals(btnOpen)) {
            menuButtonName = btnClose;
            PrimeFaces.current().executeScript("w3_open();");
        } else {
            menuButtonName = btnOpen;
            PrimeFaces.current().executeScript("w3_close();");
        }
    }


    public String getMenuButtonName() {
        return menuButtonName;
    }

    public void setMenuButtonName(String menuButtonName) {
        this.menuButtonName = menuButtonName;
    }

    public String getMenuButtonAction(){
        menuButton();
        return "";
    }
}
