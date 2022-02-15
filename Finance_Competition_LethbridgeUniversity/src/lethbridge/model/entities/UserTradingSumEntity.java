package lethbridge.model.entities;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.io.Serializable;

@Stateful
@LocalBean

@SqlResultSetMapping(
        name = "UsersTradingSummary",
        classes = {
                @ConstructorResult(
                        targetClass = UserTradingSumEntity.class,
                        columns = {
                                @ColumnResult(name = "USER_ID", type = String.class),
                                @ColumnResult(name = "COMPANY_NAME", type = String.class),
                                @ColumnResult(name = "TOTAL_STOCK_NUMBER", type = Integer.class),
                                @ColumnResult(name = "COST", type = Double.class),
                        }
                )
        }
)

@javax.persistence.Entity
public class UserTradingSumEntity implements Serializable {

    private String userId;
    private String companyName;
    private int totalStockNumber;
    private double cost;

    public UserTradingSumEntity() {
    }

    public UserTradingSumEntity(String userId,
                                String companyName,
                                int stockNumber,
                                double cost) {
        this.userId = userId;
        this.companyName = companyName;
        this.totalStockNumber = stockNumber;
        this.cost = cost;
    }

    @javax.persistence.Id
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getTotalStockNumber() {
        return totalStockNumber;
    }

    public void setTotalStockNumber(int stockNumber) {
        this.totalStockNumber = stockNumber;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
