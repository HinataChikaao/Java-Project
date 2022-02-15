package lethbridge.model.entities;


import javax.ejb.LocalBean;
import javax.ejb.Stateful;



import java.io.Serializable;
import java.util.Locale;

@Stateful
@LocalBean
public class AnalyzeEntity implements Serializable {

    private String description;
    private int quantity;
    private double price;
    private double yPrice;
    private double marketValue;
    private double cost;
    private double gainOrLoss;
    private double positionDailyChange;
    private double positionValueChange;

    private String gainOrLoseColor;
    private String positionDailyChangeColor;
    private String positionValueChangeColor;

    public AnalyzeEntity() {
    }

    public AnalyzeEntity(String description,
                         int quantity,
                         double cost,
                         double price,
                         double yPrice) {

        this.description = description;
        this.quantity = quantity;
        this.cost = cost;
        this.price = price;
        this.yPrice = yPrice;
    }

    private boolean checkDouble(double dblNum) {
        return Double.isNaN(dblNum) || Double.isInfinite(dblNum);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        double cleanPrice = (checkDouble(this.price) ? 0.0d : this.price);
        return String.format(Locale.CANADA, "%,.2f", cleanPrice);
    }

    public String getMarketValue() {

        double cleanPrice = (checkDouble(this.price) ? 0.0d : this.price);

        String format = String.format(Locale.CANADA, "%,.2f", this.quantity * cleanPrice);
        int index = format.lastIndexOf(".");
        if (index != -1 && format.substring(index).length() == 2) {
            format += "0";
        }
        return format;
    }

    public String getCost() {

        double cleanCost = checkDouble(this.cost) ? 0.0d : this.cost;

        String format = String.format(Locale.CANADA, "%,.2f", cleanCost);
        int index = format.lastIndexOf(".");
        if (index != -1 && format.substring(index).length() == 2) {
            format += "0";
        }
        return format;
    }

    public String getGainOrLoss() {
        double cleanCost = checkDouble(this.cost) ? 0.0d : this.cost;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;
        return String.format(Locale.CANADA, "%,.2f", (this.quantity * cleanPrice) - (cleanCost));
    }

    public String getCheckGainOrLoss() {

        double cleanCost = checkDouble(this.cost) ? 0.0d : this.cost;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;

        if (((this.quantity * cleanPrice) - cleanCost) > 0) {
            gainOrLoseColor = "green_color_font";
            return "/resources/images/arrays/ag.png";
        } else if (((this.quantity * cleanPrice) - cleanCost) == 0) {
            gainOrLoseColor = "white_color_font";
            return "/resources/images/dot/space.png";
        } else {
            gainOrLoseColor = "red_color_font";
            return "/resources/images/arrays/ar.png";
        }
    }

    public String getPositionDailyChange() {
        double cleanYPrice = checkDouble(this.yPrice) ? 0.0d : this.yPrice;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;
        return String.format(Locale.CANADA, "%,.2f", cleanPrice - cleanYPrice);
    }

    public String getCheckPositionDailyChange() {

        double cleanYPrice = checkDouble(this.yPrice) ? 0.0d : this.yPrice;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;

        if ((cleanPrice - cleanYPrice) > 0) {
            positionDailyChangeColor = "green_color_font";
            return "/resources/images/arrays/ag.png";
        } else if ((cleanPrice - cleanYPrice) == 0) {
            positionDailyChangeColor = "white_color_font";
            return "/resources/images/dot/space.png";
        } else {
            positionDailyChangeColor = "red_color_font";
            return "/resources/images/arrays/ar.png";
        }
    }

    public String getPositionValueChange() {

        double cleanYPrice = checkDouble(this.yPrice) ? 0.0d : this.yPrice;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;

        return String.format(Locale.CANADA, "%,.2f", (cleanPrice - cleanYPrice) * this.quantity);
    }

    public String getCheckPositionValueChange() {

        double cleanYPrice = checkDouble(this.yPrice) ? 0.0d : this.yPrice;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;

        if ((cleanPrice - cleanYPrice) * this.quantity > 0) {
            positionValueChangeColor = "green_color_font";
            return "/resources/images/arrays/ag.png";
        } else if ((cleanPrice - cleanYPrice) * this.quantity == 0) {
            positionValueChangeColor = "white_color_font";
            return "/resources/images/dot/space.png";
        } else {
            positionValueChangeColor = "red_color_font";
            return "/resources/images/arrays/ar.png";
        }
    }


    public String getGainOrLoseColor() {

        double cleanCost = checkDouble(this.cost) ? 0.0d : this.cost;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;

        if (((this.quantity * cleanPrice) - cleanCost) > 0) {
            return gainOrLoseColor = "green_color_font";
        } else if (((this.quantity * cleanPrice) - cleanCost) == 0) {
            return gainOrLoseColor = "white_color_font";
        } else {
            return gainOrLoseColor = "red_color_font";
        }

    }

    public String getPositionDailyChangeColor() {

        double cleanYPrice = checkDouble(this.yPrice) ? 0.0d : this.yPrice;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;

        if ((cleanPrice - cleanYPrice) > 0) {
            return positionDailyChangeColor = "green_color_font";
        } else if ((cleanPrice - cleanYPrice) == 0) {
            return positionDailyChangeColor = "white_color_font";
        } else {
            return positionDailyChangeColor = "red_color_font";
        }
    }

    public String getPositionValueChangeColor() {

        double cleanYPrice = checkDouble(this.yPrice) ? 0.0d : this.yPrice;
        double cleanPrice = checkDouble(this.price) ? 0.0d : this.price;

        if ((cleanPrice - cleanYPrice) * this.quantity > 0) {
            return positionValueChangeColor = "green_color_font";
        } else if ((cleanPrice - cleanYPrice) * this.quantity == 0) {
            return positionValueChangeColor = "white_color_font";
        } else {
            return positionValueChangeColor = "red_color_font";
        }
    }

}
