package controller;

import core.BaseController;
import core.Encryption;
import core.Validation;
import model.bl.CustomerMgr;
import model.entity.CustomerEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PizzaProps;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

@Named
@RequestScoped
public class SignUpCtrl extends BaseController implements Serializable {

    private Logger pizzaLogger = LoggerFactory.getLogger(SignUpCtrl.class);

    @EJB(name = "CustomerMgr")
    private CustomerMgr customerMgr;

    @EJB(name = "CustomerEntity")
    private CustomerEntity customerEntity;

    @EJB(name = "Validation")
    private Validation validation;

    private String firstName;
    private String lastName;
    private String address;
    private String mobileNumber;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private String passConfirm;

    @PostConstruct
    private void init() {
        reset();
    }

    private void reset() {
        customerEntity = new CustomerEntity();
    }

    public void saveCustomer() {

        if (!password.equals(passConfirm)) {
            showErrMessage("Passwords is not same ...");
            return;
        }

        String encryptedPass = PizzaProps.PublicText.EMPTY;
        try {
            encryptedPass = Encryption.encryptBySHA256(this.password);
        } catch (NoSuchAlgorithmException e) {
            showErrMessage(e.getMessage());
            return;
        }

        customerEntity.setEmail(this.email);
        customerEntity.setFirstName(this.firstName);
        customerEntity.setLastName(this.lastName);
        customerEntity.setMobileNumber(this.mobileNumber);
        customerEntity.setPhoneNumber(this.phoneNumber);
        customerEntity.setPassword(encryptedPass);
        customerEntity.setUsername(this.username);
        customerEntity.setAddress(this.address);


        String validator = validation.validator(customerEntity);
        if (!StringUtils.isBlank(validator)) {
            showErrMessage(validator);
            return;
        }

        try {
            if (!customerMgr.existUsername(customerEntity.getUsername())) {
                customerMgr.insertCustomer(customerEntity);
                reset();
                redirect("/nr/index.xhtml");
                showInfoMessage("This is your subscribe number: " + String.valueOf(customerEntity.getSubsNumber()));
            } else {
                showErrMessage("Some one has this username already!");
            }
        } catch (Exception e) {
            showErrMessage("Save is Failed, an error in occur ...", e);
        }

    }


    /* ---------------------------------------------- */


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }
}
