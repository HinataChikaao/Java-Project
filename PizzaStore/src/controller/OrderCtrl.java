package controller;


import core.*;
import model.bl.*;
import model.entity.*;
import model.entity.core.DrinkOrder;
import model.entity.core.PizzaOrder;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class OrderCtrl extends BaseController implements Serializable {

    @EJB(name = "PizzaMgr")
    private PizzaMgr pizzaMgr;

    @EJB(name = "DrinkMgr")
    private DrinkMgr drinkMgr;

    @EJB(name = "PizzaSizeMgr")
    private PizzaSizeMgr pizzaSizeMgr;

    @EJB(name = "CustomerOrderMgr")
    private CustomerOrderMgr customerOrderMgr;

    @EJB(name = "OrderDetailsMgr")
    private OrderDetailsMgr orderDetailsMgr;

    @EJB(name = "PizzaImages")
    private PizzaCatalog pizzaCatalog;

    @EJB(name = "DrinkCatalog")
    private DrinkCatalog drinkCatalog;

    @EJB(name = "PizzaSizeCatalog")
    private PizzaSizeCatalog pizzaSizeCatalog;

    @EJB(name = "PizzaOrder")
    private PizzaOrder orderPizzaEntity;

    @EJB(name = "DrinkOrder")
    private DrinkOrder orderDrinkEntity;

    @EJB(name = "OrderDetailsEntity")
    private OrderDetailsEntity orderDetailsEntity;

    @EJB(name = "CustomerOrderEntity")
    private CustomerOrderEntity customerOrderEntity;

    private List<PizzaOrder> pizzaOrderList;
    private List<DrinkOrder> drinkOrderList;
    private List<PizzaSizeEntity> pizzaSizeCollect;
    private Long pizzaId;
    private Long drinkId;
    private Integer pizzaCode;
    private Long pizzaSizeId;
    private Integer pizzaNumber;
    private Integer drinkNumber;


    @PostConstruct
    private void init() {
        reset();
    }


    private void reset() {
        orderPizzaEntity = new PizzaOrder();
        pizzaOrderList = new ArrayList<>();

        orderDrinkEntity = new DrinkOrder();
        drinkOrderList = new ArrayList<>();

        customerOrderEntity = new CustomerOrderEntity();

        pizzaNumber = 0;
        drinkNumber = 0;
    }


    public void pizzaSizeExistence() {
        //PizzaEntity pizzaEntity = pizzaCatalog.getPizzaEntityMap().get(pizzaId);
        List<PizzaEntity> psCollect = pizzaCatalog.getPizzaEntityList().stream()
                .filter(item -> item.getPizzaCode().equals(this.pizzaCode))
                .collect(Collectors.toList());

        pizzaSizeCollect = psCollect.stream().map(PizzaEntity::getPizzaSize)
                .distinct().collect(Collectors.toList());

    }


    public String initPizzaOrder() {

        try {
            if (this.pizzaCode == null || pizzaSizeId == null) {
                return null;
            }

            PizzaEntity pizzaEntity =
                    pizzaMgr.selectPizzaByCodeAndSizeId(this.pizzaCode, this.pizzaSizeId, Activation.Active);
            orderPizzaEntity = new PizzaOrder();
            orderPizzaEntity.setPizzaId(pizzaEntity.getPizzaId());
            orderPizzaEntity.setPizzaName(pizzaEntity.getPizzaName());
            orderPizzaEntity.setPizzaSizeId(pizzaEntity.getPizzaId());
            orderPizzaEntity.setPizzaSizeName(pizzaEntity.getPizzaSize().getPizzaSizeName());
            orderPizzaEntity.setPizzaNumber(this.pizzaNumber);
            pizzaOrderList.add(orderPizzaEntity);
            resetPizzaOrder();
        } catch (Exception e) {
            showErrMessage("Error to Find Pizza:", e);
        }
        return null;
    }

    public void resetPizzaOrder() {
        orderPizzaEntity = null;
        this.pizzaCode = null;
        this.pizzaSizeId = null;
        this.pizzaNumber = 1;

    }


    public String initDrinkOrder() {

        try {
            if (drinkId == null) {
                return null;
            }
            DrinkEntity drinkEntity = drinkMgr.selectDrinkById(this.drinkId, Activation.Active);
            orderDrinkEntity = new DrinkOrder();
            orderDrinkEntity.setDrinkId(this.drinkId);
            orderDrinkEntity.setDrinkName(drinkEntity.getDrinkName());
            orderDrinkEntity.setDrinkNumber(this.drinkNumber);
            drinkOrderList.add(orderDrinkEntity);
            resetDrinkOrder();
        } catch (Exception e) {
            showErrMessage("Error to Find Drink:", e);
        }
        return null;
    }


    public void resetDrinkOrder() {
        orderDrinkEntity = null;
        drinkId = null;
        drinkNumber = 1;
    }


    public void handleToggle() {

    }

    public void save() {

        try {
            customerOrderEntity.setCustomer(getPizzaUser());
            customerOrderMgr.insertCustomerOrder(customerOrderEntity);

            for (PizzaOrder pizzaOrder : pizzaOrderList) {
                orderDetailsEntity = new OrderDetailsEntity();
                orderDetailsEntity.setCustomerOrder(customerOrderEntity);
                PizzaEntity pizzaEntity = pizzaMgr.selectPizzaById(pizzaOrder.getPizzaId(), Activation.Active);
                orderDetailsEntity.setPizzaEntity(pizzaEntity);
                orderDetailsEntity.setPizzaNumber(pizzaOrder.getPizzaNumber());
                orderDetailsMgr.insertOrderDetails(orderDetailsEntity);
            }

            for (DrinkOrder drinkOrder : drinkOrderList) {
                orderDetailsEntity = new OrderDetailsEntity();
                orderDetailsEntity.setCustomerOrder(customerOrderEntity);
                DrinkEntity drinkEntity = drinkMgr.selectDrinkById(drinkOrder.getDrinkId(), Activation.Active);
                orderDetailsEntity.setDrinkEntity(drinkEntity);
                orderDetailsEntity.setDrinkNumber(drinkOrder.getDrinkNumber());
                orderDetailsMgr.insertOrderDetails(orderDetailsEntity);
            }

            setSessionObject("customerOrder", customerOrderEntity);
            pizzaRedirect("/nr/customer/payment.xhtml");

        } catch (Exception e) {
            showErrMessage("Error to save order:", e);
        }

    }

    /* ----------------------------------------------------- */

    public PizzaCatalog getPizzaCatalog() {
        return pizzaCatalog;
    }

    public StreamedContent getPizzaDynamicImage() {
        String imageId = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("pizzaId");
        StreamedContent sc;
        if (imageId == null) {
            sc = this.pizzaCatalog.getPizzaEntityList().get(0).getStreamedContent();
        } else {
            sc = this.pizzaCatalog.getPizzaEntityMap().get(Long.parseLong(imageId)).getStreamedContent();
        }
        return sc;
    }

    public StreamedContent getDrinkDynamicImage() {
        String imageId = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("drinkId");
        StreamedContent sc;
        if (imageId == null) {
            sc = this.drinkCatalog.getDrinkEntityList().get(0).getStreamedContent();
        } else {
            sc = this.drinkCatalog.getDrinkEntityMap().get(Long.parseLong(imageId)).getStreamedContent();
        }
        return sc;
    }

    public PizzaOrder getOrderPizzaEntity() {
        return orderPizzaEntity;
    }

    public void setOrderPizzaEntity(PizzaOrder pizzaOrder) {
        this.orderPizzaEntity = pizzaOrder;
    }

    public List<PizzaOrder> getPizzaOrderList() {
        return pizzaOrderList;
    }

    public void setPizzaOrderList(List<PizzaOrder> pizzaOrderList) {
        this.pizzaOrderList = pizzaOrderList;
    }

    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public Integer getPizzaCode() {
        return pizzaCode;
    }

    public void setPizzaCode(Integer pizzaCode) {
        this.pizzaCode = pizzaCode;
    }

    public List<PizzaSizeEntity> getPizzaSizeCollect() {
        return pizzaSizeCollect;
    }

    public void setPizzaSizeCollect(List<PizzaSizeEntity> pizzaSizeCollect) {
        this.pizzaSizeCollect = pizzaSizeCollect;
    }

    public Long getPizzaSizeId() {
        return pizzaSizeId;
    }

    public void setPizzaSizeId(Long pizzaSizeId) {
        this.pizzaSizeId = pizzaSizeId;
    }

    public Integer getPizzaNumber() {
        return pizzaNumber;
    }

    public void setPizzaNumber(Integer pizzaNumber) {
        this.pizzaNumber = pizzaNumber;
    }


    public DrinkOrder getOrderDrinkEntity() {
        return orderDrinkEntity;
    }

    public void setOrderDrinkEntity(DrinkOrder orderDrinkEntity) {
        this.orderDrinkEntity = orderDrinkEntity;
    }

    public List<DrinkOrder> getDrinkOrderList() {
        return drinkOrderList;
    }

    public void setDrinkOrderList(List<DrinkOrder> drinkOrderList) {
        this.drinkOrderList = drinkOrderList;
    }

    public Integer getDrinkNumber() {
        return drinkNumber;
    }

    public void setDrinkNumber(Integer drinkNumber) {
        this.drinkNumber = drinkNumber;
    }

    public DrinkCatalog getDrinkCatalog() {
        return drinkCatalog;
    }

    public void setDrinkCatalog(DrinkCatalog drinkCatalog) {
        this.drinkCatalog = drinkCatalog;
    }

    public Long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(Long drinkId) {
        this.drinkId = drinkId;
    }
}
