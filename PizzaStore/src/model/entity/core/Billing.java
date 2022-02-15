package model.entity.core;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.io.Serializable;

@LocalBean
@Stateful
public class Billing implements Serializable {

    private String productName;
    private int number;
    private Double priceForOne;

    public Billing() {
    }

    public Billing(String productName, int number, Double priceForOne) {
        this.productName = productName;
        this.number = number;
        this.priceForOne = priceForOne;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Double getPriceForOne() {
        return priceForOne;
    }

    public void setPriceForOne(Double priceForOne) {
        this.priceForOne = priceForOne;
    }

    public Double getTotalPrice() {
        return priceForOne * (number * 1.0);
    }
}
