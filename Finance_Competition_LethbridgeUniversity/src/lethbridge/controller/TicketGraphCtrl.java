package lethbridge.controller;

import lethbridge.core.BaseController;
import lethbridge.core.ExcelParsing;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import util.ConstantText;
import util.R;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class TicketGraphCtrl extends BaseController implements Serializable {


    @Inject
    private ExcelParsing excelParsing;

    private Date date;
    private Double startPoint;
    private Double endPoint;
    private Double minPoint;
    private Double maxPoint;
    private Double division;
    private Double tolerance;
    private int time;
    private int dataIndex;
    private double excelIndex;
    private int indexSheet = 0;
    private int sheetNum;
    private int pointSize;

    private LineChartModel lineModel;
    private ChartSeries series;
    private Map<Object, Number> allPoint;
    private Axis xAxis;
    private Axis yAxis;

    private Random rnd;

    private StreamedContent streamedContent;

    private LinkedHashMap<String, TreeMap<String, Object>> dataGraphs;
    private String[] selectedSheets;
    private List<String> allSheets;

    private BitSet buttonsStatus;

    //private Object[] kes;

    public TicketGraphCtrl() {
        initComponent();
    }


    private void initComponent() {
        rnd = new Random();
        allPoint = new HashMap<>();

        buttonsStatus = new BitSet(7);
        buttonsStatus.set(0, 7, true);

        lineModel = new LineChartModel();
        lineModel.setTitle("Ticket Graph");
        lineModel.setLegendPosition("ne");
        lineModel.setShowPointLabels(true);
        lineModel.setAnimate(false);
        lineModel.setBreakOnNull(true);
        lineModel.setMouseoverHighlight(true);
        lineModel.setShadow(true);
        lineModel.setShowPointLabels(true);
        lineModel.setShowDatatip(true);
        lineModel.setZoom(false);
        lineModel.setResetAxesOnResize(true);

        xAxis = lineModel.getAxis(AxisType.X);
        xAxis.setLabel("Division");

        yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setLabel("From Min To Max");

        series = new ChartSeries();
        series.setLabel("Stock Price");
        series.set("Graph", 0);
        lineModel.addSeries(series);

        date = new Date();
        startPoint = 0.0;
        endPoint = 0.0;
        minPoint = 0.0;
        maxPoint = 0.0;
        division = 0.0;
        tolerance = 0.0;
        time = 1;

        setButtonsDisable(false, false, true,
                false, true, true, true);
    }

    @PostConstruct
    public void init(){

    }

    public void createGraph() {
        try {
            checkValues();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, R.Messages.Error.ERROR, e.getMessage()));
        }
        createLineModels();
    }

    public void reinitialize() {
        dataIndex = 0;
        series.getData().clear();
        series.set("Graph", 0);
        xAxis.setMin(0.0);
        xAxis.setMax(division);
        yAxis.setMin(minPoint);
        yAxis.setMax(maxPoint);
        lineModel.setTitle("Ticket Graph");
    }

    public void createPoints() {

        try {
            checkValues();
            setButtonsDisable(true, true, true, true,
                    false, true, true);

            int index = 0;
            double result = startPoint;
            allPoint.put(index, result);
            index++;

            while (index < division) {
                result = rnd.nextInt((int) ((result + tolerance) - (result - tolerance) + 1)) + (result - tolerance);
                if (((index < division) && (result < endPoint && result >= startPoint))
                        || ((index == division) && (result <= endPoint) && result >= startPoint)) {
                    allPoint.put(index, result);
                    index++;
                } else {
                    index = 0;
                    allPoint.clear();
                    result = startPoint;
                    allPoint.put(index, result);
                    index++;
                }
            }
            reinitialize();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(R.Messages.Error.ERROR, e.getMessage()));
            stopGraph();
        }

        //RequestContext.getCurrentInstance().scrollTo("toolbarForm:GraphPanel");
        PrimeFaces.current().scrollTo("toolbarForm:GraphPanel");
    }

    public void createLineModels() {

        if (dataIndex == division) {
            stopGraph();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, R.Messages.Information.THE_END,
                            R.Messages.Information.ALL_POINTS_SHOWED));
            return;
        }

        series.set(dataIndex, allPoint.get(dataIndex));
        dataIndex++;

    }

    public void createExcelLineModels() {

        if (indexSheet < sheetNum) {
            if (excelIndex == 0) {

                createGraphs();
            }

            if ((int) excelIndex == pointSize) {
                excelIndex = 0;
                indexSheet++;
            } else {
                series.set(excelIndex, allPoint.get(excelIndex));
                excelIndex++;
            }
        } else {
            //RequestContext.getCurrentInstance().execute("PF('excelPoint').stop();");
            PrimeFaces.current().executeScript("PF('excelPoint').stop();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, R.Messages.Information.THE_END,
                            R.Messages.Information.ALL_GRAPHS_SHOWED));
            stopGraph();
        }
    }


    public void stopGraph() {
        FacesContext.getCurrentInstance().getPartialViewContext().getExecuteIds().add("interval");
        //RequestContext.getCurrentInstance().execute("PF('poll').stop();");
        PrimeFaces.current().executeScript("PF('poll').stop();");
        FacesContext.getCurrentInstance().getPartialViewContext().getExecuteIds().add("excelPoint");
        //RequestContext.getCurrentInstance().execute("PF('excelPoint').stop();");
        PrimeFaces.current().executeScript("PF('excelPoint').stop();");
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("message, lineChart, interval, excelPoint");
        setButtonsDisable(false, false, true, false, true, false, false);
    }


    private void checkValues() throws Exception {

        StringBuilder sb = new StringBuilder();

        if (date == null) {
            sb.append("Most Specify a date ...").append(System.lineSeparator());
        }

        if (minPoint < 0) {
            sb.append("Min Should be greater than 0 ...").append(System.lineSeparator());
        }

        if (minPoint >= maxPoint) {
            sb.append("Min Should be less than Max ...").append(System.lineSeparator());
        }

        if (startPoint < minPoint) {
            sb.append("Start Point Should be equal or greater than Min ...").append(System.lineSeparator());
        }

        if (startPoint >= endPoint) {
            sb.append("Start Point Should be less than End Point ...").append(System.lineSeparator());
        }

        if (startPoint >= maxPoint) {
            sb.append("Start Point Should be less than Max ...").append(System.lineSeparator());
        }


        if (endPoint > maxPoint) {
            sb.append("End point should be equal or less than Max ...").append(System.lineSeparator());
        }

        if (division <= 0) {
            sb.append("Division should be greater than 0 ...").append(System.lineSeparator());
        }

        if (sb.length() > 0) {
            throw new Exception(sb.toString());
        }
    }

    public void readExcelFile(FileUploadEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, R.Messages.Information.INFO,
                event.getFile().getFileName() + R.Messages.Information.UPLOAD_SUCCESSFULLY);
        FacesContext.getCurrentInstance().addMessage(null, message);
        //ExcelParsing excelParsing = new ExcelParsing();

        try {
            streamedContent = new DefaultStreamedContent(event.getFile().getInputstream(),
                    "Excel File/xlsx", event.getFile().getFileName(), "UTF8");

            excelParsing.readFromExcel(streamedContent.getStream());

            dataGraphs = excelParsing.getDataGraphs();
            allSheets = new ArrayList<>();
            allSheets.addAll(dataGraphs.keySet().stream().collect(Collectors.toList()));

            selectedSheets = null;
            //RequestContext.getCurrentInstance().execute("showSheetSelectionDialog();");
            //RequestContext.getCurrentInstance().update("toolbarForm:selectDialog");
            PrimeFaces.current().executeScript("showSheetSelectionDialog();");
            PrimeFaces.current().ajax().update("showSheetSelectionDialog();");

        } catch (Exception e) {
            FacesMessage messageEx = new FacesMessage(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, messageEx);
        }
    }

    public void resetSheetIndex() {
        indexSheet = 0;
        sheetNum = selectedSheets.length;
        setButtonsDisable(true, false, true, true,
                true, true, true);
        //RequestContext.getCurrentInstance().scrollTo("toolbarForm:GraphPanel");
        PrimeFaces.current().scrollTo("toolbarForm:GraphPanel");
    }

    public void createGraphs() {
        try {
            if (indexSheet < sheetNum) {
                TreeMap<String, Object> dataGraph = dataGraphs.get(selectedSheets[indexSheet]);

                pointSize = dataGraph.size() - 7;
                if (pointSize % 2 != 0) {
                    throw new Exception(R.Messages.Error.INVALID_EXCEL_FILE_DATA);
                } else {
                    pointSize /= 2;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.CANADA);
                date = sdf.parse(dataGraph.get(ConstantText.DATE).toString());
                startPoint = (double) dataGraph.get(ConstantText.START_POINT);
                endPoint = (double) dataGraph.get(ConstantText.END_POINT);
                minPoint = (double) dataGraph.get(ConstantText.MIN);
                maxPoint = (double) dataGraph.get(ConstantText.MAX);
                division = (double) dataGraph.get(ConstantText.DIVISION);
                tolerance = (double) dataGraph.get(ConstantText.TOLERANCE);

                allPoint.clear();
                long index = 0;
                if (pointSize > 0) {
                    while (index < pointSize) {
                        allPoint.put(dataGraph.get(ConstantText.X + index),
                                (double) dataGraph.get(ConstantText.Y + index));
                        index++;
                    }
                } else {
                    createPoints();
                }

                xAxis.setMin(0.0);
                xAxis.setMax(division);
                yAxis.setMin(minPoint);
                yAxis.setMax(maxPoint);
                series.getData().clear();
                lineModel.setTitle(selectedSheets[indexSheet]);

                //allPoint.forEach((o, num) -> series.set(o, num));

                //RequestContext.getCurrentInstance().update("toolbarForm:ticketGraphTable");
                //RequestContext.getCurrentInstance().update("toolbarForm:GraphPanel");
                /*RequestContext.getCurrentInstance().execute("PF('excelSheet').stop();");*/
                //RequestContext.getCurrentInstance().execute("PF('excelPoint').start();");

                PrimeFaces.current().ajax().update("toolbarForm:ticketGraphTable");
                PrimeFaces.current().ajax().update("toolbarForm:GraphPanel");
                PrimeFaces.current().executeScript("PF('excelPoint').start();");



            } else {
                selectedSheets = null;
                //allSheets = new ArrayList<>();
                stopGraph();
            }
        } catch (Exception e) {
            FacesMessage messageEx = new FacesMessage(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, messageEx);
        }
        //indexSheet++;

    }

    private void setButtonsDisable(boolean fileUpload, boolean spinners, boolean createGraph,
                                   boolean startGraph, boolean stopGraph, boolean clearGraph,
                                   boolean download) {
        buttonsStatus.set(0, fileUpload);
        buttonsStatus.set(1, spinners);
        buttonsStatus.set(2, createGraph);
        buttonsStatus.set(3, startGraph);
        buttonsStatus.set(4, stopGraph);
        buttonsStatus.set(5, clearGraph);
        buttonsStatus.set(6, download);
        //RequestContext.getCurrentInstance().update("toolbarForm:ticketGraphTable");
        PrimeFaces.current().ajax().update("toolbarForm:ticketGraphTable");
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Double getTolerance() {
        return tolerance;
    }

    public void setTolerance(Double tolerance) {
        this.tolerance = tolerance;
    }

    public StreamedContent getStreamedContent() {

        long index = 7;
        //ExcelParsing excelParsing = new ExcelParsing();
        Map<String, Object[]> data = new LinkedHashMap<>();

        data.put("0", new Object[]{ConstantText.DATE, date});
        data.put("1", new Object[]{ConstantText.START_POINT, startPoint});
        data.put("2", new Object[]{ConstantText.END_POINT, endPoint});
        data.put("3", new Object[]{ConstantText.MIN, minPoint});
        data.put("4", new Object[]{ConstantText.MAX, maxPoint});
        data.put("5", new Object[]{ConstantText.DIVISION, division});
        data.put("6", new Object[]{ConstantText.TOLERANCE, tolerance});
        data.put(String.valueOf(index), new Object[]{ConstantText.X, ConstantText.Y});

        for (Map.Entry<Object, Number> point : series.getData().entrySet()) {
            if (point.getKey().equals("Graph")) continue;
            data.put(String.valueOf(++index), new Object[]{point.getKey(), point.getValue()});
        }
        try {
            return new DefaultStreamedContent(new ByteArrayInputStream(excelParsing.writeToExcel(data,
                    ConstantText.SHEET_NAME)),
                    "Excel/xlsx", "Downloaded_ExcelDataGraph.xlsx", "UTF8");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage()));
            return null;
        }
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

    public String[] getSelectedSheets() {
        return selectedSheets;
    }

    public void setSelectedSheets(String[] selectedSheets) {
        this.selectedSheets = selectedSheets;
    }

    public List<String> getAllSheets() {
        return allSheets;
    }

    public void setAllSheets(List<String> allSheets) {
        this.allSheets = allSheets;
    }

    public BitSet getButtonsStatus() {
        return buttonsStatus;
    }

    public void setButtonsStatus(BitSet buttonsStatus) {
        this.buttonsStatus = buttonsStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Double startPoint) {
        this.startPoint = startPoint;
    }

    public Double getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Double endPoint) {
        this.endPoint = endPoint;
    }

    public Double getMinPoint() {
        return minPoint;
    }

    public void setMinPoint(Double minPoint) {
        this.minPoint = minPoint;
    }

    public Double getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(Double maxPoint) {
        this.maxPoint = maxPoint;
    }

    public Double getDivision() {
        return division;
    }

    public void setDivision(Double division) {
        this.division = division;
    }

}
