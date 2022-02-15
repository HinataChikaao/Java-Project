package lethbridge.controller;

import lethbridge.core.BaseController;
import lethbridge.core.EntityValidator;
import lethbridge.core.ExceptionPack;
import lethbridge.exceptions.BeanCheckConstraintException;
import lethbridge.model.bl.AccountValueMgr;
import lethbridge.model.bl.TradingMgr;
import lethbridge.model.bl.UsersMgr;
import lethbridge.model.entities.AnalyzeEntity;
import lethbridge.model.entities.CurNews;
import lethbridge.model.entities.TradingEntity;
import lethbridge.model.entities.UserEntity;
import org.primefaces.PrimeFaces;
import org.primefaces.model.chart.LineChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConstantText;
import util.R;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class MyAccountCtrl extends BaseController implements Serializable {

    private String className = new Object() {
    }.getClass().getEnclosingClass().getName();

    private String nextTimeout;

    private double before;
    private double lastPrice;
    private double change;
    private double percent;

    private boolean validTransaction;
    private Logger univLogger;
    private String numberOfUnit;
    private String margin;
    private String graphName;
    private String tradingType;
    private List<Double> accountValueNumbers;
    private List<CurNews> filteredNews;
    private List<Double> numbers;

    //@EJB(name = "UserEntity")
    private UserEntity userEntity;

    //@EJB(name = "AnalyzeEntity")
    private AnalyzeEntity analyzeEntity;

    @Inject
    private AccountValueMgr accountValueMgr;

    @Inject
    private TradingMgr tradingMgr;

    @Inject
    private UsersMgr usersMgr;

    private List<AnalyzeEntity> analyzeEntities;
    private List<AnalyzeEntity> analyzeEntitiesFiltered;
    private List<TradingEntity> tradingItems;

    private String companyName;
    private int quantity;
    private double purchaseCost;
    private double currentCost;
    private double previousCost;
    private double accountValue;
    private double portfolioValue;
    private double dailyPL;
    private double plToDate;
    private double cashBalance;
    private double perShareCost;
    private double studentInitialCash;
    private double studentCashBalance;
    private boolean graphNameCollection;
    private boolean timerTick;

    @PostConstruct
    public void init() {

        userEntity = new UserEntity();
        //TradingEntity tradingEntity = new TradingEntity();
        analyzeEntity = new AnalyzeEntity();

        Locale.setDefault(Locale.CANADA);

        univLogger = LoggerFactory.getLogger(MyAccountCtrl.class.getName());

        validTransaction = false;
        filteredNews = new ArrayList<>();

        userEntity = getUnivUser();
        analyzeEntities = new ArrayList<>();

        companyName = R.PublicText.EMPTY;

        numberOfUnit = "";
        lastPrice = 0.0d;
        change = 0.0d;
        percent = 0.0d;
        before = 0.0d;

        quantity = 0;
        purchaseCost = 0.0d;
        currentCost = 0.0d;
        previousCost = 0.0d;
        accountValue = 0.0d;
        portfolioValue = 0.0d;
        dailyPL = 0.0d;
        plToDate = 0.0d;
        cashBalance = 0.0d;

        studentCashBalance = loadCurrentInvestment();
        studentInitialCash = loadInitialInvestment();

        margin = "high";
        tradingType = R.PublicText.BUY;

        graphNameCollection = false;
        timerTick = false;
        univLogger.info(String.format("%s entered to the Student Page.", userEntity.toString()));
    }

    private double loadCurrentInvestment() {
        try {
            return usersMgr.getCurrentInvestment(userEntity.getUserID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double loadInitialInvestment() {
        try {
            return usersMgr.getInitialInvestment(userEntity.getUserID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void selectGraph() {
        if (graphName == null || graphName.equals("Select a Graph")) {
            graphName = TradingCtrl.tradExcelParsing.getCompaniesNames()[0];
        }
        filteredNews.clear();
        filteredNews = TradingCtrl.tradExcelParsing.getCurrentNews(graphName, TradingCtrl.dataDynamicIndex - 1);
        //RequestContext.getCurrentInstance().update("toolbarForm:stdTblNews");

        PrimeFaces.current().ajax().update("toolbarForm:stdTblNews");
        calculate();
        univLogger.info(String.format("%s selected the graph of %s company.", userEntity.toString(), graphName));
    }

    public void updateInfo() {

        timerTick = TradingCtrl.tick;

        if (!TradingCtrl.graphCleared) {

            if (graphName != null && !graphName.equals("Select a Graph")) {
                filteredNews.clear();
                filteredNews = TradingCtrl.tradExcelParsing.getCurrentNews(graphName, TradingCtrl.dataDynamicIndex - 1);
            }

            if (!graphNameCollection) {
                graphNameCollection = true;
                //RequestContext.getCurrentInstance().update("toolbarForm:cmbGraphSelection");
                //RequestContext.getCurrentInstance().update("toolbarForm:stdTblNews");

                PrimeFaces.current().ajax().update("toolbarForm:cmbGraphSelection");
                PrimeFaces.current().ajax().update("toolbarForm:stdTblNews");
            }

            calculate();
            tradingCalc();

            if (!timerTick) return;

            nextTimeout = TradingCtrl.nextTimeout;

            try {
                accountValueMgr.updateUser(userEntity.getUserID(), accountValue,
                        portfolioValue, dailyPL, plToDate, cashBalance);
            } catch (Exception e) {
                showErrorMessage(new ExceptionPack("MyAccountCtrl",
                        "calculate", e.getMessage()));
            }

            TradingCtrl.tick = timerTick = false;

        } else {
            if (graphNameCollection) {
                filteredNews.clear();
                numberOfUnit = "";
                lastPrice = 0.0d;
                change = 0.0d;
                percent = 0.0d;
                before = 0.0d;
                graphNameCollection = false;
                //RequestContext.getCurrentInstance().update("toolbarForm:cmbGraphSelection");
                //RequestContext.getCurrentInstance().update("toolbarForm:stdTblNews");
                //RequestContext.getCurrentInstance().execute("window.location.reload(false);");

                PrimeFaces.current().ajax().update("toolbarForm:cmbGraphSelection");
                PrimeFaces.current().ajax().update("toolbarForm:stdTblNews");
                PrimeFaces.current().executeScript("window.location.reload(false);");
            }
        }
    }

    private void saveAccountValue() {
        calculate();
        tradingCalc();
        try {
            accountValueMgr.updateUser(userEntity.getUserID(), accountValue,
                    portfolioValue, dailyPL, plToDate, cashBalance);
        } catch (Exception e) {
            showErrorMessage(new ExceptionPack("MyAccountCtrl",
                    "calculate", e.getMessage()));
        }
    }

    private List<Double> loadGraphData(String nameOfGraph) {
        Map<Object, Number> data;
        List<Double> numbers = new ArrayList<>();
        List<LineChartModel> result = TradingCtrl.models.stream()
                .filter(item -> item.getTitle().equals(nameOfGraph))
                .collect(Collectors.toList());

        if (result != null && result.size() != 0) {
            data = result.get(0).getSeries().get(0).getData();
            for (Map.Entry<Object, Number> map : data.entrySet()) {
                numbers.add(map.getValue().doubleValue());
            }
        }
        return numbers;
    }

    private void tradingCalc() {

        if (TradingCtrl.models.size() == 0) return;

        numbers = loadGraphData(graphName);

        if (numbers.size() == 1) {
            lastPrice = numbers.get(numbers.size() - 1);
            before = 0.0d;
        } else if (numbers.size() > 1) {
            lastPrice = numbers.get(numbers.size() - 1);
            before = numbers.get(numbers.size() - 2);
        } else if (numbers.size() == 0) {
            lastPrice = 0.0d;
            before = 0.0d;
        }

        change = Math.abs(lastPrice - before);
        percent = 0.0d;

        if (lastPrice > before) {
            percent = (change / before) * 100.0d;
            margin = "high";
        } else if (lastPrice == before) {
            percent = (change / before) * 100.0d;
            margin = "equal";
        } else {
            percent = (change / lastPrice) * 100.0d;
            margin = "low";
        }
    }

    private void calculate() {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (!TradingCtrl.point) {
            return;
        }

        if (graphName == null ||
                graphName.equals("Select a Graph") ||
                graphName.trim().length() == 0) {
            return;
        }

        if (TradingCtrl.models.size() == 0) return;

        Map<Object, Number> dataItems;
        List<Double> numberItems = new ArrayList<>();

        quantity = 0;
        purchaseCost = 0.0d;
        currentCost = 0.0d;
        previousCost = 0.0d;

        accountValue = 0.0d;
        portfolioValue = 0.0d;
        dailyPL = 0.0d;
        plToDate = 0.0d;

        cashBalance = studentCashBalance;

        /* -------------------------------------------------------------------------------- */

        try {
            if (analyzeEntities != null) {
                analyzeEntities.clear();
            }
            if (tradingItems != null) {
                tradingItems.clear();
            }

            tradingItems = tradingMgr.getTradingItems(userEntity);
            List<String> companyNames = tradingMgr.getTradingCompanyNames(userEntity);
            //List<TradingEntity> companyNames = tradingItems.stream().distinct().collect(Collectors.toList());

            if (companyNames.size() == 0) {
                studentCashBalance = loadCurrentInvestment();
                studentInitialCash = loadInitialInvestment();
            }

            for (String cmpName : companyNames) {

                accountValueNumbers = loadGraphData(cmpName);
                quantity = 0;
                purchaseCost = 0.0d;
                currentCost = 0.0d;
                previousCost = 0.0d;

                validTransaction = false;

                companyName = cmpName;
                List<TradingEntity> tradingEntityStream = tradingItems.stream()
                        .filter(item -> item.getCompanyName().equals(cmpName))
                        .collect(Collectors.toList());

                tradingEntityStream.forEach(item -> {
                    if (!item.getStatus().equals(R.PublicText.CANCELED)) {
                        switch (item.getTradingType()) {
                            case R.PublicText.BUY: {
                                if (item.getStatus().equals(R.PublicText.FILLED)) {
                                    validTransaction = true;
                                    quantity += item.getNumberOfUnit();
                                    previousCost += (accountValueNumbers.size() > 1 ? accountValueNumbers.get(accountValueNumbers.size() - 2) * item.getNumberOfUnit() : 0);
                                    if (checkDouble(previousCost)) previousCost = 0.0d;
                                    currentCost += (accountValueNumbers.size() > 0 ? accountValueNumbers.get(accountValueNumbers.size() - 1) * item.getNumberOfUnit() : 0);
                                    if (checkDouble(currentCost)) currentCost = 0.0d;
                                    purchaseCost += (item.getLastPrice() * item.getNumberOfUnit());
                                    if (checkDouble(purchaseCost)) purchaseCost = 0.0d;
                                }
                                break;
                            }
                            case R.PublicText.SELL: {
                                if (item.getStatus().equals(R.PublicText.FILLED)) {
                                    perShareCost = purchaseCost / quantity;
                                    if (checkDouble(perShareCost)) perShareCost = 0.0d;
                                    purchaseCost = (quantity - item.getNumberOfUnit()) * perShareCost;
                                    if (checkDouble(purchaseCost)) purchaseCost = 0.0d;
                                    quantity -= item.getNumberOfUnit();
                                    previousCost -= (accountValueNumbers.size() > 1 ? accountValueNumbers.get(accountValueNumbers.size() - 2) * item.getNumberOfUnit() : 0);
                                    if (checkDouble(previousCost)) previousCost = 0.0d;
                                    currentCost -= (accountValueNumbers.size() > 0 ? accountValueNumbers.get(accountValueNumbers.size() - 1) * item.getNumberOfUnit() : 0);
                                    if (checkDouble(currentCost)) currentCost = 0.0d;
                                }
                                break;
                            }
                        }
                    } else {
                        if (!validTransaction) {
                            validTransaction = false;
                        }
                    }
                });

                /* Account Value Initializing ------------------------------------- */
                accountValue += currentCost;
                portfolioValue = accountValue - cashBalance;
                dailyPL += (currentCost - previousCost);
                plToDate += (currentCost - purchaseCost);

                List<LineChartModel> companyItems = TradingCtrl.models.stream()
                        .filter(item -> item.getTitle().equals(cmpName))
                        .collect(Collectors.toList());

                numberItems.clear();
                if (companyItems != null && companyItems.size() != 0) {
                    dataItems = companyItems.get(0).getSeries().get(0).getData();

                    for (Map.Entry<Object, Number> map : dataItems.entrySet()) {
                        numberItems.add(map.getValue().doubleValue());
                    }

                    if (validTransaction && numbers.size() > 0) {
                        analyzeEntities.add(new AnalyzeEntity(companyName, quantity, purchaseCost,
                                numberItems.get(numbers.size() - 1),
                                numberItems.size() > 1 ? numberItems.get(numbers.size() - 2) : 0.0d));
                    }
                } else {
                    if (validTransaction) {
                        analyzeEntities.add(new AnalyzeEntity(companyName, quantity, purchaseCost,
                                0.0d, 0.0d));
                    }
                }
            }
            /* Account Value Initializing -------------------------------------------- */
            accountValue += studentCashBalance;
            portfolioValue = accountValue - cashBalance;
        } catch (Exception e) {
            showErrorMessage(new ExceptionPack(className, methodName, e.toString() + " &&&&&&&&&&&"));

        }

        /* -------------------------------------------------------------------------------- */

        before = lastPrice;
    }

    @Transactional
    public void submitOrder() {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (!TradingCtrl.point) {
            showWarnMessage("Be patient please, " +
                    "till the first point appear on the graph. ...");
            return;
        }

        long numberOfStock;
        int restriction;
        int submittedUnit;
        try {
            submittedUnit = Integer.parseInt(numberOfUnit);
        } catch (Exception ex) {
            numberOfUnit = "";
            showWarnMessage("Please input a valid number ...");
            return;
        }

        if (submittedUnit <= 0) {
            numberOfUnit = "";
            showWarnMessage("Please input a valid number ...");
            return;
        }

        switch (tradingType) {
            case R.PublicText.BUY: {
                try {
                    restriction = TradingCtrl.restrictionNumber.get(graphName);
                    numberOfStock = tradingMgr.getNumberOfStocks(userEntity.getUserID(), graphName);
                    if (submittedUnit <= (restriction - numberOfStock)) {
                        submitRequest();
                        showInfoMessage(R.Messages.Information.BUY_REQUEST_ACCEPTED);

                        univLogger.info(String.format("%s bought %d number share from %s company.",
                                userEntity.toString(), submittedUnit, graphName));
                    } else {
                        submitCancelRequest();
                        showWarnMessage(R.Messages.Warning.BUY_REQUEST_DENIED);
                    }
                } catch (Exception e) {
                    showErrorMessage(new ExceptionPack(className, methodName, e.getMessage()));
                } catch (BeanCheckConstraintException e) {
                    showWarnMessage(e.getMessage());
                }
                break;
            }

            case R.PublicText.SELL: {
                try {
                    submitRequest();
                    showInfoMessage(R.Messages.Information.SELL_REQUEST_ACCEPTED);
                    univLogger.info(String.format("%s Sold %d number share from %s company.",
                            userEntity.toString(), submittedUnit, graphName));
                } catch (Exception e) {
                    showErrorMessage(new ExceptionPack(className, methodName, e.getMessage()));
                } catch (BeanCheckConstraintException e) {
                    showWarnMessage(e.getMessage());
                }
                break;
            }
        }

        studentCashBalance = loadCurrentInvestment();
        calculate();
        numberOfUnit = "";
        //RequestContext.getCurrentInstance().update("toolbarForm:messages");

        PrimeFaces.current().ajax().update("toolbarForm:messages");
    }

    private void submitRequest() throws Exception, BeanCheckConstraintException {
        TradingEntity tradingEntity = new TradingEntity();
        tradingEntity.setUserID(userEntity.getUserID());
        tradingEntity.setCompanyName(graphName);
        tradingEntity.setLastUpdate(new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss").format(new Date()));
        tradingEntity.setLastPrice(lastPrice);
        tradingEntity.setTradingType(tradingType);
        tradingEntity.setNumberOfUnit(Long.parseLong(numberOfUnit));
        tradingEntity.setStatus(R.PublicText.UNKNOWN);

        String validate = EntityValidator.validate(tradingEntity);
        if (validate.equals(ConstantText.NO_MESSAGE)) {
            tradingMgr.registerRequest(tradingEntity);
            saveAccountValue();
        } else {
            throw new BeanCheckConstraintException(validate);
        }
    }

    private void submitCancelRequest() throws Exception, BeanCheckConstraintException {
        TradingEntity tradingEntity = new TradingEntity();
        tradingEntity.setUserID(userEntity.getUserID());
        tradingEntity.setCompanyName(graphName);
        tradingEntity.setLastUpdate(new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss").format(new Date()));
        tradingEntity.setLastPrice(lastPrice);
        tradingEntity.setTradingType(tradingType);
        tradingEntity.setNumberOfUnit(Long.parseLong(numberOfUnit));
        tradingEntity.setStatus(R.PublicText.CANCELED);

        String validate = EntityValidator.validate(tradingEntity);
        if (validate.equals(ConstantText.NO_MESSAGE)) {

            tradingMgr.registerCancelRequest(tradingEntity);
            saveAccountValue();
        } else {
            throw new BeanCheckConstraintException(validate);
        }

    }

    private boolean checkDouble(double dblNum) {
        return (Double.isNaN(dblNum) || Double.isInfinite(dblNum));
    }

    /* ---------------------------------------------------------------------------------- */
    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public List<CurNews> getFilteredNews() {
        return filteredNews;
    }

    public void setFilteredNews(List<CurNews> filteredNews) {
        this.filteredNews = filteredNews;
    }

    public String getLastPrice() {

        return String.format(Locale.CANADA, "%,.2f", checkDouble(lastPrice) ? 0.0d : lastPrice);
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getNumberOfUnit() {
        return numberOfUnit;
    }

    public void setNumberOfUnit(String numberOfUnit) {
        this.numberOfUnit = numberOfUnit;
    }

    public String getChange() {
        return String.format(Locale.CANADA, "%,.2f", checkDouble(change) ? 0.0d : change);
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getPercent() {
        try {
            double value = Double.parseDouble(String.format(Locale.CANADA, "%,.2f",
                    checkDouble(percent) ? 0.0d : percent));
            if (checkDouble(value)) value = 0.0d;
            return (value);
        } catch (Exception ex) {
            return 0.0d;
        }
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getTradingType() {
        return tradingType;
    }

    public void setTradingType(String tradingType) {
        this.tradingType = tradingType;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public List<AnalyzeEntity> getAnalyzeEntities() {
        return analyzeEntities;
    }

    public void setAnalyzeEntities(List<AnalyzeEntity> analyzeEntities) {
        this.analyzeEntities = analyzeEntities;
    }

    public List<AnalyzeEntity> getAnalyzeEntitiesFiltered() {
        return analyzeEntitiesFiltered;
    }

    public void setAnalyzeEntitiesFiltered(List<AnalyzeEntity> analyzeEntitiesFiltered) {
        this.analyzeEntitiesFiltered = analyzeEntitiesFiltered;
    }

    public AnalyzeEntity getAnalyzeEntity() {
        return analyzeEntity;
    }

    public void setAnalyzeEntity(AnalyzeEntity analyzeEntity) {
        this.analyzeEntity = analyzeEntity;
    }

    public List<TradingEntity> getTradingItems() {
        return tradingItems;
    }

    public void setTradingItems(List<TradingEntity> tradingItems) {
        this.tradingItems = tradingItems;
    }

    public String getStudentInitialCash() {
        return String.format(Locale.CANADA, "%,.2f", studentInitialCash);
    }

    public void setStudentInitialCash(double studentInitialCash) {
        this.studentInitialCash = studentInitialCash;
    }

    public String getStudentCashBalance() {
        return String.format(Locale.CANADA, "%,.2f", studentCashBalance);
    }

    public void setStudentCashBalance(double studentCashBalance) {
        this.studentCashBalance = studentCashBalance;
    }

    public String getAccountValue() {
        try {
            return String.format(Locale.CANADA, "%,.2f", accountValue);
        } catch (Exception ex) {
            return "0.0";
        }
    }

    public void setAccountValue(double accountValue) {

        this.accountValue = accountValue;
    }

    public String getPortfolioValue() {
        try {
            return String.format(Locale.CANADA, "%,.2f", portfolioValue);
        } catch (Exception ex) {
            return "0.0";
        }
    }

    public void setPortfolioValue(double portfolioValue) {
        this.portfolioValue = portfolioValue;
    }

    public String getCheckAccountValueColor() {
        return checkColor(getAccountValue());
    }

    public String getCheckPortfolioValueColor() {
        return checkColor(getPortfolioValue());
    }

    public String getDailyPL() {
        try {
            return String.format(Locale.CANADA, "%,.2f",
                    checkDouble(dailyPL) ? 0.0d : dailyPL);
        } catch (Exception ex) {
            return "0.0";
        }
    }

    public void setDailyPL(double dailyPL) {
        this.dailyPL = dailyPL;
    }

    public String getCheckDailyPLColor() {
        return checkColor(getDailyPL());
    }

    public String getPlToDate() {
        try {
            return String.format(Locale.CANADA, "%,.2f",
                    checkDouble(plToDate) ? 0.0d : plToDate);
        } catch (Exception ex) {
            return "0.0";
        }
    }

    public void setPlToDate(double plToDate) {
        this.plToDate = plToDate;
    }

    public String getCheckPlToDateColor() {
        return checkColor(getPlToDate());
    }

    public String getCashBalance() {
        try {
            return String.format(Locale.CANADA, "%,.2f",
                    checkDouble(cashBalance) ? 0.0 : cashBalance);
        } catch (Exception ex) {
            return "0.0";
        }
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public String getCheckCashBalanceColor() {
        return checkColor(getCashBalance());
    }

    private String checkColor(String value) {
        try {
            double dblValue = Double.parseDouble(value.replaceAll(",", ""));
            double cleanValue = checkDouble(dblValue) ? 0.0d : dblValue;
            if (cleanValue > 0) {
                return "green_color_font";
            } else if (cleanValue == 0) {
                return "white_color_font";
            } else {
                return "red_color_font";
            }
        } catch (Exception ex) {
            return "violet_color_font";
        }
    }

    public String getInitialInvestment() {

        try {
            AnalyzeEntity analyzeEntity = analyzeEntities.stream().filter(item ->
                    item.getDescription().trim()
                            .equals(graphName.trim())).collect(Collectors.toList()).get(0);
            return analyzeEntity.getCost();
        } catch (Exception ex) {
            return "0.00";
        }
    }

    public String getCurrentInvestment() {
        try {
            AnalyzeEntity analyzeEntity = analyzeEntities.stream().filter(item ->
                    item.getDescription().trim()
                            .equals(graphName.trim())).collect(Collectors.toList()).get(0);
            return analyzeEntity.getMarketValue();
        } catch (Exception ex) {
            return "0.00";
        }
    }

    public String getNextTimeout() {
        return nextTimeout;
    }

    public void setNextTimeout(String nextTimeout) {
        this.nextTimeout = nextTimeout;
    }

    public boolean isTimerTick() {
        return TradingCtrl.pause;
    }
}
