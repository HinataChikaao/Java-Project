package lethbridge.controller;

import lethbridge.core.BaseController;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class ThemeServiceCtrl extends BaseController implements Serializable {

    private String theme;

    @PostConstruct
    public void init() {
        theme = "afterdark";
    }

    public void saveThme(String thmeName) {
        setTheme(thmeName);
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}