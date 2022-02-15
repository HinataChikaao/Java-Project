package controller;

import core.Activation;
import core.BaseController;
import model.bl.CustomerMgr;
import model.entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PizzaProps;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class SignInCtrl extends BaseController implements Serializable {

    private Logger logger = LoggerFactory.getLogger(SignInCtrl.class);

    @EJB(name = "CustomerMgr")
    private CustomerMgr customerMgr;

    @EJB(name = "CustomerEntity")
    private CustomerEntity customerEntity;

    private String username;
    private String password;

    @PostConstruct
    private void init() {
        username = "";
        password = "";
        customerEntity = new CustomerEntity();
    }


    public void confirm() {
        System.out.println(this.username);
        System.out.println(this.password);

        try {
            PizzaUserLogin(username, password);
            customerEntity = customerMgr.selectCustomerByUsername(username, Activation.Active);
            setSessionObject(PizzaProps.variables.CUSTOMER, customerEntity);
            pizzaRedirect("/nr/index.xhtml");

            showInfoMessage("you Login Successfully");
        } catch (Exception e) {
            showErrMessage("Login failed ...", e);
        }

    }

    /* ----------------------------------- */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
