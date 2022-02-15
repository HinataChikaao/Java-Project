package controller;

import core.Activation;
import core.BaseController;
import model.bl.OrderDetailsMgr;
import model.entity.CustomerOrderEntity;
import model.entity.DrinkEntity;
import model.entity.OrderDetailsEntity;
import model.entity.PizzaEntity;
import model.entity.core.Billing;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class PaymentCtrl extends BaseController implements Serializable {


    @EJB(name = "OrderDetailsMgr")
    private OrderDetailsMgr orderDetailsMgr;

    @EJB(name = "OrderDetailsEntity")
    private OrderDetailsEntity orderDetailsEntity;

    @EJB(name = "CustomerOrderEntity")
    private CustomerOrderEntity customerOrderEntity;


    private List<Billing> billings;


    @PostConstruct
    public void init() {

        customerOrderEntity =
                (CustomerOrderEntity) getSessionObject("customerOrder");
        billings = new ArrayList<>();
        loadOrderDetails();

    }


    private void loadOrderDetails() {
        try {
            List<OrderDetailsEntity> orderDetailsList = orderDetailsMgr
                    .selectOrderDetailsOrderId(customerOrderEntity, Activation.Active);

            List<Long> pizzaIds = orderDetailsList.stream().filter(item -> item.getPizzaEntity() != null)
                    .map(OrderDetailsEntity::getPizzaEntity)
                    .distinct().map(PizzaEntity::getPizzaId).collect(Collectors.toList());

            List<Long> drinkIds = orderDetailsList.stream().filter(item -> item.getDrinkEntity() != null)
                    .map(OrderDetailsEntity::getDrinkEntity)
                    .distinct().map(DrinkEntity::getDrinkId).collect(Collectors.toList());

            if (pizzaIds.size() != 0) {
                for (Long pid : pizzaIds) {
                    List<OrderDetailsEntity> collect = orderDetailsList.stream()
                            .filter(item -> item.getPizzaEntity() != null)
                            .filter(item -> item.getPizzaEntity().getPizzaId().equals(pid))
                            .collect(Collectors.toList());
                    int number = collect.stream().mapToInt(OrderDetailsEntity::getPizzaNumber).sum();
                    PizzaEntity pe = collect.get(0).getPizzaEntity();
                    billings.add(new Billing(pe.getPizzaName(), number, pe.getPizzaPrice()));
                }
            }

            if (drinkIds.size() != 0) {
                for (Long did : drinkIds) {
                    List<OrderDetailsEntity> collect = orderDetailsList.stream()
                            .filter(item -> item.getDrinkEntity() != null)
                            .filter(item -> item.getDrinkEntity().getDrinkId().equals(did))
                            .collect(Collectors.toList());
                    int number = collect.stream().mapToInt(OrderDetailsEntity::getDrinkNumber).sum();
                    DrinkEntity de = collect.get(0).getDrinkEntity();
                    billings.add(new Billing(de.getDrinkName(), number, de.getDrinkPrice()));
                }
            }

        } catch (Exception e) {
            showErrMessage("Error to load Orders:", e);
        }
    }

    public void payment() {
        showInfoMessage("Payment is over ...");
    }

    /* ------------------------------------------------------------ */

    public List<Billing> getBillings() {
        return billings;
    }

    public void setBillings(List<Billing> billings) {
        this.billings = billings;
    }
}
