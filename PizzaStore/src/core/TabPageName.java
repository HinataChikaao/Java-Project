package core;

import model.entity.CustomerEntity;
import util.PizzaProps;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@ViewScoped
public class TabPageName extends BaseController implements Serializable {

    private Map<String, String> pageNames;

    @PostConstruct
    private void init() {
        pageNames = new HashMap<>();

        pageNames.put("signup.xhtml", "Sign Up");
        pageNames.put("login.xhtml", "Sign In");
        pageNames.put("drink.xhtml", "Drink");
        pageNames.put("index.xhtml", "Home");
        pageNames.put("order.xhtml", "Order");
        pageNames.put("payment.xhtml", "Payment");
        pageNames.put("pizza.xhtml", "Pizza");
        pageNames.put("403.xhtml", "Forbidden");
        pageNames.put("404.xhtml", "Page Not Found");
        pageNames.put("error.xhtml", "error");
        pageNames.put("login-error.xhtml", "Login Error");
        pageNames.put("viewExpire.xhtml", "View Expire  ");
    }

    public String getPageName() {
        String pagePath = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestServletPath();
        int lastIndex = pagePath.lastIndexOf("/");
        String fileName = pagePath.substring(++lastIndex);
        if (fileName.trim().length() > 0) {

            return pageNames.get(fileName);
        } else {
            return pagePath;
        }
    }

    public String getBackground() {
        return getPageName().equals("Home") ? "hero-image" : "hero-bg";
    }

    public String getFullName() {
        CustomerEntity pizzaUser = getPizzaUser();
        return pizzaUser == null ? PizzaProps.PublicText.EMPTY : "Hello: " + pizzaUser.fullName();
    }

}
