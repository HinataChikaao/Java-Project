package lethbridge.controller;

import lethbridge.core.BaseController;
import lethbridge.core.ExceptionPack;
import lethbridge.core.LethUnReport;
import lethbridge.core.TradExcelParsing;
import lethbridge.model.bl.ReportMgr;
import lethbridge.model.bl.UserTradingSumMgr;
import lethbridge.model.entities.CurNews;
import lethbridge.model.entities.ReportEntity;
import lethbridge.model.entities.UserEntity;
import lethbridge.model.entities.UserTradingSumEntity;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.primefaces.PrimeFaces;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.R;
import util.StatusContainer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.ejb.Timer;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Named
@LocalBean
@Singleton
@ApplicationScoped
@TransactionManagement(value = TransactionManagementType.BEAN)
public class TradingCtrl extends BaseController implements Serializable {

    static TradExcelParsing tradExcelParsing;
    static List<LineChartModel> models;
    static Map<String, Integer> restrictionNumber;
    static boolean tick;
    static boolean point;
    static boolean pause;
    static int dataDynamicIndex;
    static boolean graphCleared;
    static String nextTimeout;

    private Logger univLogger;
    //private int yRow;
    private int xCol;
    private List<CurNews> filteredNews;
    private ArrayList<Timer> timers;
    private DateFormat dateFormat;

    //@EJB(name = "CurNews")
    private CurNews curNews;

    @Inject
    private ReportMgr reportMgr;

    @Inject
    private UserTradingSumMgr userTradingSumMgr;

    @Inject
    private LethUnReport lethUnReport;

    private String graphName;
    private int graphIndex;


    /* The text of the curNews store in the areaText */
    private String areaText;

    private int numChart;

    private StreamedContent excelFile;
    private StreamedContent reportExcelFile;
    private String excelFileName;
    private UploadedFile uploadedFile;

    private int dDivision;
    private double[] timeValues;
    private String[] companyNames;

    private UserEntity userEntity;

    private boolean terminate;
    private boolean showTheEnd;
    private boolean stopGraphs;

    private String[] selectedDays;
    private List<String> daysOfWeek;
    private boolean daySelected;

    @Resource
    private TimerService timerService;
    private TimerConfig timerConfig;

    private boolean running;
    private int[] seconds;
    private int second;

    private DecimalFormat df;
    private StatusContainer statusContainer;

    @PostConstruct
    public void init() {

        dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss", Locale.CANADA);
        userEntity = getUnivUser();

        univLogger = LoggerFactory.getLogger(TradingCtrl.class.getName());
        statusContainer = new StatusContainer(components.class);

        df = new DecimalFormat("#");

        second = 0;
        seconds = new int[61];
        for (int i = 1; i < 61; i++) {
            seconds[i] = i;
        }

        daysOfWeek = new ArrayList<>();
        daysOfWeek.add("Monday");
        daysOfWeek.add("Tuesday");
        daysOfWeek.add("Wednesday");
        daysOfWeek.add("Thursday");
        daysOfWeek.add("Friday");
        daysOfWeek.add("Saturday");
        daysOfWeek.add("Sunday");

        graphCleared = false;
        terminate = false;
        showTheEnd = false;
        running = false;
        stopGraphs = false;
        tick = false;
        point = false;
        pause = false;

        //Row = 10;
        xCol = 10;
        //dataDynamicIndex = -1;

        numChart = 0;
        models = new ArrayList<>(numChart);
        graphIndex = 1;

        timerConfig = new TimerConfig("Graphs", false);

        /* Initial Data Exporter */

        //-- newses = new LinkedList<>();
        filteredNews = new LinkedList<>();
        /*for (int i = 0; i < 100; i++) {
            newses.push(new NewsEntity(i, "Ticker" + i, "Head Line " + i));
        }*/

        statusContainer.disableExept(components.FileUploadButton);
        univLogger.info(String.format("%s (Admin) entered to the Trading Page.", userEntity.toString()));

    }

    /*private boolean checkDouble(double dblNum) {
        return (Double.isNaN(dblNum) || Double.isInfinite(dblNum));
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
    }*/

    private byte[] writeToExcelOne(int mode) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        List<ReportEntity> allUsers = reportMgr.getAllUsers();
        Map<String, Object[]> data = new LinkedHashMap<>();

        int index = 0;
        data.put(String.valueOf(++index), new Object[]{
                "User ID",
                "Name",
                "Family",
                "Last Login",
                "Last Trading",
                "Initial Investment",
                "Cash Balance",
                "Account Value",
                "Portfolio Value"
        });

        try {
            for (ReportEntity au : allUsers) {
                data.put(String.valueOf(++index), new Object[]{
                        au.getUserID(),
                        au.getFirstName(),
                        au.getLastName(),
                        au.getLastLogin(),
                        au.getLastUpdate(),
                        au.getInitialInvestment(),
                        au.getCurrentInvestment(),
                        au.getAccountValue(),
                        au.getPortfolioValue()
                });
            }
        } catch (Exception ex) {
            throw new Exception("Error in Input Data...");
        }

        excelFileName = userEntity.getFirstName() + "_" + userEntity.getLastName() + "_Student_Financial_Details_Info";

        if (mode == LethUnReport.MODE_2003) {
            return lethUnReport.generateExcelFile2003(data, userEntity,
                    new SimpleDateFormat("dd/MM/YYYY").format(new Date()), "Student Account Info");
        } else {
            return lethUnReport.generateExcelFile2003(data, userEntity,
                    new SimpleDateFormat("dd/MM/YYYY").format(new Date()), "Student Account Info");
        }
    }

    private byte[] writeToExcelTwo(int mode) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();


        List<UserTradingSumEntity> allUsers = userTradingSumMgr.getAllUsersTradingSum();
        Map<String, Object[]> data = new LinkedHashMap<>();

        int index = 0;
        data.put(String.valueOf(++index), new Object[]{
                "User ID",
                "Company Name",
                "Total Stock Number",
                "cost",
        });

        try {
            for (UserTradingSumEntity au : allUsers) {
                data.put(String.valueOf(++index), new Object[]{
                        au.getUserId(),
                        au.getCompanyName(),
                        au.getTotalStockNumber(),
                        au.getCost()
                });
            }
        } catch (Exception ex) {
            throw new Exception("Error in Input Data...");
        }

        excelFileName = userEntity.getFirstName() + "_" + userEntity.getLastName() + "_StudentTradingSummary";

        if (mode == LethUnReport.MODE_2003) {
            return lethUnReport.generateExcelFile2003(data, userEntity,
                    new SimpleDateFormat("dd/MM/YYYY").format(new Date()), "Student Trading Summary");
        } else {
            return lethUnReport.generateExcelFile2003(data, userEntity,
                    new SimpleDateFormat("dd/MM/YYYY").format(new Date()), "Student Trading Summary");
        }
    }

    public void downloadData2003(ActionEvent event) {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String reportName = event.getComponent().getId();

        try {
            if (reportName.trim().equals("btnDownload2003_1")) {
                reportExcelFile = new DefaultStreamedContent(
                        new ByteArrayInputStream(writeToExcelOne(LethUnReport.MODE_2003)),
                        "Excel/xls", excelFileName + ".xls", "UTF8");
            } else if (reportName.trim().equals("btnDownload2003_2")) {
                reportExcelFile = new DefaultStreamedContent(
                        new ByteArrayInputStream(writeToExcelTwo(LethUnReport.MODE_2003)),
                        "Excel/xls", excelFileName + ".xls", "UTF8");
            }
        } catch (Exception ex) {
            showErrorMessage(new ExceptionPack(getClass().getName(),
                    methodName, ex.getMessage(), ex.toString()));
        }
    }

    public void cancelTimeSetting() {
        second = 0;
        selectedDays = null;
    }

    public void daySelectedListener() {
        //RequestContext.getCurrentInstance().update("toolbarForm:btnConfirm");
        PrimeFaces.current().ajax().update("toolbarForm:btnConfirm");
    }

    private String getDaysInAbbrivation() {
        final List<String> daysAbb = new ArrayList<>();
        final String comma = ",";

        if (selectedDays != null && selectedDays.length > 0) {
            for (String day : selectedDays) {
                daysAbb.add(day.substring(0, 3));
            }
        } else {
            daysAbb.add("*");
        }
        return String.join(comma, daysAbb);
    }

    public void runTimer() {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (second < 1 && (selectedDays == null || selectedDays.length == 0)) {
            showErrorMessage(new ExceptionPack(TradingCtrl.class.getName(), methodName,
                    "Please select a valid time distance ..."));
            //RequestContext.getCurrentInstance().update("toolbarForm:message");
            PrimeFaces.current().ajax().update("toolbarForm:message");
            return;
        }

        statusContainer.disableExept(components.PauseButton, components.ClearButton,
                components.GraphSelection);

        if (!running) {
            //RequestContext.getCurrentInstance().execute("PF('showPointTime').start();");
            PrimeFaces.current().ajax().update("PF('showPointTime').start();");

            if (second > 0) {
                timerService.createCalendarTimer(new ScheduleExpression()
                                .year("*")
                                .month("*")
                                .dayOfMonth("*")
                                .dayOfWeek(getDaysInAbbrivation())
                                .hour("*")
                                .minute("*")
                                .second("*/" + second),
                        timerConfig);

            } else if (!getDaysInAbbrivation().equals("*") && second < 1) {
                timerService.createCalendarTimer(new ScheduleExpression()
                                .year("*")
                                .month("*")
                                .dayOfMonth("*")
                                .dayOfWeek(getDaysInAbbrivation())
                                .hour("0"),
                        timerConfig);
            }

            timers = new ArrayList<>(timerService.getTimers());
            nextTimeout = dateFormat.format(timers.get(0).getNextTimeout());


            running = true;
            terminate = false;
            graphCleared = false;
            univLogger.info(String.format("%s (Admin) started the timer.", userEntity.toString()));
        }
    }

    @PreDestroy
    public void stopTimer() {
        if (running) {
            timerService.getTimers().forEach(Timer::cancel);
            running = false;
            tick = false;
            univLogger.info(String.format("%s (Admin) stopped the timer.", userEntity.toString()));
        }

        if (stopGraphs && terminate && showTheEnd) {
            nextTimeout = "The End.";
        } else {
            nextTimeout = "Stop ...";
        }

    }

    @Timeout
    private void getDynamicPoint() {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        univLogger.info(String.format("%s (Admin): timer ticked (%d), Division is %d",
                userEntity.toString(), dataDynamicIndex, dDivision));

        for (int i = 0; i < numChart; i++) {
            if (dataDynamicIndex < dDivision) {
                models.get(i).getSeries().get(0).set(timeValues[dataDynamicIndex],
                        tradExcelParsing.getCompaniesValues().get(companyNames[i])[dataDynamicIndex]);
            } else {
                break;
            }
        }

        univLogger.info(String.format("%s (Admin): Dynamic Points: step one is passed.",
                userEntity.toString()));

        /*if ((dataDynamicIndex >= dDivision)) {
            stopGraphs = true;
            terminate = true;
            return;
        }*/

        if ((dataDynamicIndex >= dDivision)) {
            stopGraphs = true;
            terminate = true;
            showTheEnd = true;
            stopTimer();
            stopAllGraphEJB();
            stopGraphs = false;
            nextTimeout = "The End.";
            return;
        }

        univLogger.info(String.format("%s (Admin): Dynamic Points: step two is passed.", userEntity.toString()));
        filteredNews = tradExcelParsing.getCurrentNews(graphName, dataDynamicIndex);
        dataDynamicIndex++;
        tick = true;
        point = true;
        nextTimeout = dateFormat.format(timers.get(0).getNextTimeout());
    }

    public void update() {

        if (running) {

            //RequestContext.getCurrentInstance().update("toolbarForm:dynamicChart_" + graphIndex);
            //RequestContext.getCurrentInstance().update("toolbarForm:message");
            //RequestContext.getCurrentInstance().update("toolbarForm:tblNews");
            //RequestContext.getCurrentInstance().update("toolbarForm:textArea");
            PrimeFaces.current().ajax().update("toolbarForm:dynamicChart_" + graphIndex);
            PrimeFaces.current().ajax().update("toolbarForm:message");
            PrimeFaces.current().ajax().update("toolbarForm:tblNews");
            PrimeFaces.current().ajax().update("toolbarForm:textArea");
        }

        if (stopGraphs) {
            stopTimer();
            stopAllGraph();
            stopGraphs = false;
        }

        if (showTheEnd) {
            //RequestContext.getCurrentInstance().execute("PF('showPointTime').stop();");
            //RequestContext.getCurrentInstance().update("toolbarForm:tblNews");
            PrimeFaces.current().executeScript("PF('showPointTime').stop();");
            PrimeFaces.current().ajax().update("PF('showPointTime').stop();");

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "The End:",
                            "All points are showed..."));
            //RequestContext.getCurrentInstance().update("toolbarForm:message");
            PrimeFaces.current().ajax().update("toolbarForm:message");

            showTheEnd = false;
        }
    }

    public void stopGraph(ActionEvent event) {
        CommandButton button = null;
        if (event != null && event.getComponent().getClientId().equals("toolbarForm:btnPause")) {
            pause = !pause;
            button = (CommandButton) event.getSource();
        }
        if (pause) {
            if (button != null) {
                button.setValue("Restart");
            }
            stopAllGraph();
        } else {
            if (button != null) {
                button.setValue("Pause");
            }
            restart();
        }
        univLogger.info(String.format("%s (Admin): Graphs are paused.", userEntity.toString()));
    }

    private void stopAllGraphEJB() {
        if (running) stopTimer();
        univLogger.info(String.format("%s (Admin): Graphs are stopped. (E)", userEntity.toString()));

        if (pause) {
            statusContainer.enableExcept(components.StartButton, components.FileUploadButton);
        } else {
            statusContainer.disableExept(components.ClearButton, components.GraphSelection);
        }
    }

    private void stopAllGraph() {
        if (running) stopTimer();
        univLogger.info(String.format("%s (Admin): Graphs are stopped.", userEntity.toString()));

        if (pause) {
            statusContainer.enableExcept(components.StartButton, components.FileUploadButton);
        } else {
            statusContainer.disableExept(components.ClearButton, components.GraphSelection);
        }
        //RequestContext.getCurrentInstance().update("toolbarForm:buttons");
        PrimeFaces.current().ajax().update("toolbarForm:buttons");
    }

    private void restart() {
        runTimer();
        statusContainer.enable(components.PauseButton);
        //RequestContext.getCurrentInstance().update("toolbarForm:buttons");
        PrimeFaces.current().ajax().update("toolbarForm:buttons");
        univLogger.info(String.format("%s (Admin): Graphs are restarted.", userEntity.toString()));
    }

    public void clearGraphs() {
        stopAllGraph();
        models.clear();

        filteredNews.clear();
        timeValues = null;
        companyNames = null;
        dataDynamicIndex = 0;
        dDivision = 0;
        numChart = 0;
        second = 0;
        selectedDays = null;
        areaText = R.PublicText.EMPTY;
        graphName = R.PublicText.EMPTY;
        graphIndex = 1;
        statusContainer.disableExept(components.FileUploadButton);
        graphCleared = true;
        point = false;
        //RequestContext.getCurrentInstance().update("toolbarForm:tblNews");
        PrimeFaces.current().ajax().update("toolbarForm:tblNews");
        univLogger.info(String.format("%s (Admin): Graphs are cleared.", userEntity.toString()));
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            StreamedContent streamedContent = new DefaultStreamedContent(event.getFile().getInputstream(),
                    "Excel File/xlsx", event.getFile().getFileName(), "UTF8");
            tradExcelParsing = new TradExcelParsing(streamedContent.getStream());

            if (tradExcelParsing.fieldValidate()) {
                clearGraphs();
                FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);

                tradExcelParsing.initialize();
                numChart = tradExcelParsing.getCompaniesNames().length;
                dDivision = tradExcelParsing.getTimeValues().length;
                timeValues = tradExcelParsing.getTimeValues();
                companyNames = tradExcelParsing.getCompaniesNames();
                restrictionNumber = tradExcelParsing.getRestrictionNumber();

                for (int i = 0; i < numChart; i++) {
                    models.add(generateLineChartModel(i));
                }
            } else {
                throw new Exception("Invalid Excel file format...");
            }

            terminate = false;
            graphCleared = false;
            statusContainer.enableExcept(components.PauseButton, components.StartButton, components.FileUploadButton);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage()));
            clearGraphs();
        }

        univLogger.info(String.format("%s (Admin): Excel file (%s) is uploaded.",
                userEntity.toString(), event.getFile().getFileName()));
    }

    private LineChartModel generateLineChartModel(int index) throws IOException,
            InvalidFormatException, URISyntaxException {

        LineChartModel lineChartModel = new LineChartModel();
        lineChartModel.setTitle(tradExcelParsing.getCompaniesNames()[index]);
        lineChartModel.setShowPointLabels(true);
        lineChartModel.setResetAxesOnResize(true);
        lineChartModel.setAnimate(false);
        lineChartModel.setMouseoverHighlight(true);
        lineChartModel.setResetAxesOnResize(true);
        lineChartModel.setShadow(true);
        lineChartModel.setZoom(true);

        Axis xAxis = lineChartModel.getAxis(AxisType.X);
        xAxis.setMin(0);
        xAxis.setMax(dDivision);
        xAxis.setTickInterval(String.valueOf((dDivision % xCol) == 0 ? dDivision / xCol : (dDivision / xCol) + 1));

        Axis yAxis = lineChartModel.getAxis(AxisType.Y);
        double min = tradExcelParsing.getMinValueCompanyFor(tradExcelParsing.getCompaniesNames()[index]);
        double max = tradExcelParsing.getMaxValueCompanyFor(tradExcelParsing.getCompaniesNames()[index]);
        yAxis.setMin(min - 2);
        yAxis.setMax(max + 2);
        //yAxis.setTickInterval(String.valueOf((max % yRow) == 0 ? max / yRow : (max / yRow)));

        ChartSeries chartSeries = new ChartSeries();
        chartSeries.set("Graph", 0);
        lineChartModel.addSeries(chartSeries);

        return lineChartModel;
    }

    public void onRowSelect(SelectEvent event) {
        areaText = "";
        String headLine = ((CurNews) event.getObject()).getHeadLine();
        String story = ((CurNews) event.getObject()).getStory();
        areaText = headLine + "\n" + story;
        //RequestContext.getCurrentInstance().execute("PF('newsDialog').show()");
        //RequestContext.getCurrentInstance().update("toolbarForm:textArea");
        PrimeFaces.current().executeScript("PF('newsDialog').show()");
        PrimeFaces.current().ajax().update("PF('newsDialog').show()");
        univLogger.info(String.format("News (%s) is selected.", headLine));
    }

    public void onRowUnselected(UnselectEvent event) {
        curNews = null;
        //RequestContext.getCurrentInstance().update("toolbarForm:tblNews");
        //RequestContext.getCurrentInstance().update("toolbarForm:textArea");
        PrimeFaces.current().ajax().update("toolbarForm:tblNews");
        PrimeFaces.current().ajax().update("toolbarForm:textArea");
    }

    public void selectGraph() {
        if (graphName.equals("NoGraph")) {
            graphName = models.get(0).getTitle();
        }

        if (!running) {
            if (terminate) {
                statusContainer.disableExept(components.ClearButton, components.GraphSelection);
            } else {
                statusContainer.enableExcept(components.FileUploadButton, components.PauseButton);
            }
        }

        for (int i = 1; i <= models.size(); i++) {
            if (models.get(i - 1).getTitle().equals(graphName)) {
                graphIndex = i;
                break;
            }
        }

        System.out.println(graphName + "  " + graphIndex);
        //RequestContext.getCurrentInstance().update("toolbarForm:dg");
        PrimeFaces.current().ajax().update("toolbarForm:dg");
        if (running || terminate) {
            filteredNews = tradExcelParsing.getCurrentNews(graphName, dataDynamicIndex);
            //RequestContext.getCurrentInstance().update("toolbarForm:tblNews");
            PrimeFaces.current().ajax().update("toolbarForm:tblNews");
        }
        univLogger.info(String.format("Graph (%s) is selected.", graphName));
    }

    public int getNumChart() {
        return numChart;
    }

    public void setNumChart(int numChart) {
        this.numChart = numChart;
    }

    /* ------------------------------------------------------- */
    /* ------------------------------------------------------- */

    public List<LineChartModel> getModels() {
        return models;
    }

    public void setModels(List<LineChartModel> models) {
        TradingCtrl.models = models;
    }

    public StreamedContent getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(StreamedContent excelFile) {
        this.excelFile = excelFile;
    }


    public StreamedContent getReportExcelFile() {
        return reportExcelFile;
    }

    public void setReportExcelFile(StreamedContent reportExcelFile) {
        this.reportExcelFile = reportExcelFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public CurNews getCurNews() {
        return curNews;
    }

    public void setCurNews(CurNews curNews) {
        this.curNews = curNews;
    }

    public String getAreaText() {
        return areaText;
    }

    public void setAreaText(String areaText) {
        this.areaText = areaText;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int[] getSeconds() {
        return seconds;
    }

    public void setSeconds(int[] seconds) {
        this.seconds = seconds;
    }

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

    public int getGraphIndex() {
        return graphIndex;
    }

    public void setGraphIndex(int graphIndex) {
        this.graphIndex = graphIndex;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public StatusContainer getStatusContainer() {
        return statusContainer;
    }

    public void setStatusContainer(StatusContainer statusContainer) {
        this.statusContainer = statusContainer;
    }

    public double getProgress() {
        double result = 0.0d;
        double percent = 0.0d;
        if (dataDynamicIndex != 0 && dDivision != 0) {
            percent = (100 * dataDynamicIndex) / dDivision;
        }
        if (Double.isNaN(result)) {
            try {
                result = Double.parseDouble(df.format(percent));
            } catch (Exception ex) {
                result = 0;
            }
        } else {
            result = 0;
        }
        return result;
    }

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String[] getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(String[] selectedDays) {
        this.selectedDays = selectedDays;
    }

    public boolean isDaySelected() {
        daySelected = (selectedDays == null) || (selectedDays.length == 0);
        return daySelected;
    }

    public void setDaySelected(boolean daySelected) {
        this.daySelected = daySelected;
    }

    private enum components {
        StartButton,
        PauseButton,
        ClearButton,
        FileUploadButton,
        TimeInterval,
        GraphSelection,
    }
}
