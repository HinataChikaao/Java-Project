package lethbridge.controller;

import lethbridge.core.BaseController;
import lethbridge.core.BlackScholesFormula;
import lethbridge.core.ExceptionPack;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Named
@SessionScoped
public class StandardGraphsCtrl extends BaseController implements Serializable {

    private String graphCategory;
    private String graphType;
    private String graphObject;

    private double exercisePrice;
    private double underlyingPrice;
    private double timeToExpiration;
    private double riskFreeRate;
    private double volatility;

    private double callOptionPrice;
    private double putOptionPrice;

    private double callOptionDelta;
    private double putOptionDelta;

    private double callOptionGamma;
    private double putOptionGamma;

    private double callOptionTheta;
    private double putOptionTheta;

    private double callOptionVega;
    private double putOptionVega;

    private double callOptionRHO;
    private double putOptionRHO;

    private LineChartModel priceCallOptionLineModel;
    private LineChartModel pricePutCallLineModel;
    private LineChartModel r_PriceCallOptionLineModel;
    private LineChartModel r_PricePutCallLineModel;

    private LineChartModel deltaCallOptionLineModel;
    private LineChartModel deltaPutCallLineModel;
    private LineChartModel r_DeltaCallOptionLineModel;
    private LineChartModel r_DeltaPutCallLineModel;

    private LineChartModel gammaCallOptionLineModel;
    private LineChartModel gammaPutCallLineModel;
    private LineChartModel r_GammaCallOptionLineModel;
    private LineChartModel r_GammaPutCallLineModel;

    private LineChartModel thetaCallOptionLineModel;
    private LineChartModel thetaPutCallLineModel;
    private LineChartModel r_ThetaCallOptionLineModel;
    private LineChartModel r_ThetaPutCallLineModel;

    private LineChartModel rhoCallOptionLineModel;
    private LineChartModel rhoPutCallLineModel;
    private LineChartModel r_RhoCallOptionLineModel;
    private LineChartModel r_RhoPutCallLineModel;

    private LineChartModel vegaCallOptionLineModel;
    private LineChartModel vegaPutCallLineModel;
    private LineChartModel r_VegaCallOptionLineModel;
    private LineChartModel r_VegaPutCallLineModel;

    @PostConstruct
    public void init(){

    }

    public void calculate() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            double[] p = BlackScholesFormula.calculate(exercisePrice,
                    underlyingPrice, riskFreeRate, timeToExpiration, volatility);
            DecimalFormat df = new DecimalFormat();
            df.setGroupingUsed(false);
            df.setMaximumFractionDigits(8);

            callOptionPrice = new BigDecimal(df.format(p[0])).doubleValue();
            callOptionDelta = new BigDecimal(df.format(p[4])).doubleValue();
            callOptionGamma = new BigDecimal(df.format(p[2])).doubleValue();
            callOptionTheta = new BigDecimal(df.format(p[6])).doubleValue();
            callOptionVega = new BigDecimal(df.format(p[3])).doubleValue();
            callOptionRHO = new BigDecimal(df.format(p[8])).doubleValue();

            putOptionPrice = new BigDecimal(df.format(p[1])).doubleValue();
            putOptionDelta = new BigDecimal(df.format(p[5])).doubleValue();
            putOptionGamma = new BigDecimal(df.format(p[2])).doubleValue();
            putOptionTheta = new BigDecimal(df.format(p[7])).doubleValue();
            putOptionVega = new BigDecimal(df.format(p[3])).doubleValue();
            putOptionRHO = new BigDecimal(df.format(p[9])).doubleValue();
        } catch (Exception ex) {
            showErrorMessage(new ExceptionPack(StandardGraphsCtrl.class.getName(),
                    methodName, "Invalid Input Data ..."));
        }
    }

    public void drawChart() {

        priceCallOptionLineModel = new LineChartModel();
        pricePutCallLineModel = new LineChartModel();
        priceCallOptionLineModel.getAxis(AxisType.Y).setLabel("Option Price");
        pricePutCallLineModel.getAxis(AxisType.Y).setLabel("Option Price");
        priceCallOptionLineModel.setTitle("Call Option");
        pricePutCallLineModel.setTitle("Put Call");
        /*------------------------------------------------*/
        r_PriceCallOptionLineModel = new LineChartModel();
        r_PricePutCallLineModel = new LineChartModel();
        r_PriceCallOptionLineModel.getAxis(AxisType.Y).setLabel("Option Price");
        r_PricePutCallLineModel.getAxis(AxisType.Y).setLabel("Option Price");
        r_PriceCallOptionLineModel.setTitle("Call Option");
        r_PricePutCallLineModel.setTitle("Put Call");

        deltaCallOptionLineModel = new LineChartModel();
        deltaPutCallLineModel = new LineChartModel();
        deltaCallOptionLineModel.getAxis(AxisType.Y).setLabel("Delta");
        deltaPutCallLineModel.getAxis(AxisType.Y).setLabel("Delta");
        deltaCallOptionLineModel.setTitle("Call Option");
        deltaPutCallLineModel.setTitle("Put Call");
        /*------------------------------------------------*/
        r_DeltaCallOptionLineModel = new LineChartModel();
        r_DeltaPutCallLineModel = new LineChartModel();
        r_DeltaCallOptionLineModel.getAxis(AxisType.Y).setLabel("Delta");
        r_DeltaPutCallLineModel.getAxis(AxisType.Y).setLabel("Delta");
        r_DeltaCallOptionLineModel.setTitle("Call Option");
        r_DeltaPutCallLineModel.setTitle("Put Call");

        gammaCallOptionLineModel = new LineChartModel();
        gammaPutCallLineModel = new LineChartModel();
        gammaCallOptionLineModel.getAxis(AxisType.Y).setLabel("Gamma");
        gammaPutCallLineModel.getAxis(AxisType.Y).setLabel("Gamma");
        gammaCallOptionLineModel.setTitle("Call Option");
        gammaPutCallLineModel.setTitle("Put Call");
        /*------------------------------------------------*/
        r_GammaCallOptionLineModel = new LineChartModel();
        r_GammaPutCallLineModel = new LineChartModel();
        r_GammaCallOptionLineModel.getAxis(AxisType.Y).setLabel("Gamma");
        r_GammaPutCallLineModel.getAxis(AxisType.Y).setLabel("Gamma");
        r_GammaCallOptionLineModel.setTitle("Call Option");
        r_GammaPutCallLineModel.setTitle("Put Call");

        thetaCallOptionLineModel = new LineChartModel();
        thetaPutCallLineModel = new LineChartModel();
        thetaCallOptionLineModel.getAxis(AxisType.Y).setLabel("Theta");
        thetaPutCallLineModel.getAxis(AxisType.Y).setLabel("Theta");
        thetaCallOptionLineModel.setTitle("Call Option");
        thetaPutCallLineModel.setTitle("Put Call");
        /*------------------------------------------------*/
        r_ThetaCallOptionLineModel = new LineChartModel();
        r_ThetaPutCallLineModel = new LineChartModel();
        r_ThetaCallOptionLineModel.getAxis(AxisType.Y).setLabel("Theta");
        r_ThetaPutCallLineModel.getAxis(AxisType.Y).setLabel("Theta");
        r_ThetaCallOptionLineModel.setTitle("Call Option");
        r_ThetaPutCallLineModel.setTitle("Put Call");

        rhoCallOptionLineModel = new LineChartModel();
        rhoPutCallLineModel = new LineChartModel();
        rhoCallOptionLineModel.getAxis(AxisType.Y).setLabel("RHO");
        rhoPutCallLineModel.getAxis(AxisType.Y).setLabel("RHO");
        rhoCallOptionLineModel.setTitle("Call Option");
        rhoPutCallLineModel.setTitle("Put Call");
        /*------------------------------------------------*/
        r_RhoCallOptionLineModel = new LineChartModel();
        r_RhoPutCallLineModel = new LineChartModel();
        r_RhoCallOptionLineModel.getAxis(AxisType.Y).setLabel("RHO");
        r_RhoPutCallLineModel.getAxis(AxisType.Y).setLabel("RHO");
        r_RhoCallOptionLineModel.setTitle("Call Option");
        r_RhoPutCallLineModel.setTitle("Put Call");

        vegaCallOptionLineModel = new LineChartModel();
        vegaPutCallLineModel = new LineChartModel();
        vegaCallOptionLineModel.getAxis(AxisType.Y).setLabel("Vega");
        vegaPutCallLineModel.getAxis(AxisType.Y).setLabel("Vega");
        vegaCallOptionLineModel.setTitle("Call Option");
        vegaPutCallLineModel.setTitle("Put Call");
        /*------------------------------------------------*/
        r_VegaCallOptionLineModel = new LineChartModel();
        r_VegaPutCallLineModel = new LineChartModel();
        r_VegaCallOptionLineModel.getAxis(AxisType.Y).setLabel("Vega");
        r_VegaPutCallLineModel.getAxis(AxisType.Y).setLabel("Vega");
        r_VegaCallOptionLineModel.setTitle("Call Option");
        r_VegaPutCallLineModel.setTitle("Put call");

        ChartSeries priceCall = new ChartSeries();
        ChartSeries pricePut = new ChartSeries();
        ChartSeries r_priceCall = new ChartSeries();
        ChartSeries r_pricePut = new ChartSeries();

        ChartSeries deltaCall = new ChartSeries();
        ChartSeries deltaPut = new ChartSeries();
        ChartSeries r_deltaCall = new ChartSeries();
        ChartSeries r_deltaPut = new ChartSeries();

        ChartSeries gammaCall = new ChartSeries();
        ChartSeries gammaPut = new ChartSeries();
        ChartSeries r_gammaCall = new ChartSeries();
        ChartSeries r_gammaPut = new ChartSeries();

        ChartSeries thetaCall = new ChartSeries();
        ChartSeries thetaPut = new ChartSeries();
        ChartSeries r_thetaCall = new ChartSeries();
        ChartSeries r_thetaPut = new ChartSeries();

        ChartSeries rhoCall = new ChartSeries();
        ChartSeries rhoPut = new ChartSeries();
        ChartSeries r_rhoCall = new ChartSeries();
        ChartSeries r_rhoPut = new ChartSeries();

        ChartSeries vegaCall = new ChartSeries();
        ChartSeries vegaPut = new ChartSeries();
        ChartSeries r_vegaCall = new ChartSeries();
        ChartSeries r_vegaPut = new ChartSeries();

        double i;
        double endLoop = 91;
        int eLoop = (int) endLoop;
        double[] p;

        double[] rp0 = new double[eLoop];
        double[] rp1 = new double[eLoop];
        double[] rp2 = new double[eLoop];
        double[] rp3 = new double[eLoop];
        double[] rp4 = new double[eLoop];
        double[] rp5 = new double[eLoop];

        double[] rp6 = new double[eLoop];
        double[] rp7 = new double[eLoop];
        double[] rp8 = new double[eLoop];
        double[] rp9 = new double[eLoop];
        double[] rp10 = new double[eLoop];
        double[] rp11 = new double[eLoop];

        for (i = 0.0d; i < endLoop - 1; i++) {

            p = BlackScholesFormula.calculate(exercisePrice,
                    underlyingPrice, riskFreeRate, i, volatility);

            rp0[(int) i] = Double.isNaN(p[0]) ? 0 : p[0];
            rp1[(int) i] = Double.isNaN(p[4]) ? 0 : p[4];
            rp2[(int) i] = Double.isNaN(p[2]) ? 0 : p[2];
            rp3[(int) i] = Double.isNaN(p[3]) ? 0 : p[3];
            rp4[(int) i] = Double.isNaN(p[6]) ? 0 : p[6];
            rp5[(int) i] = Double.isNaN(p[8]) ? 0 : p[8];

            rp6[(int) i] = Double.isNaN(p[1]) ? 0 : p[1];
            rp7[(int) i] = Double.isNaN(p[5]) ? 0 : p[5];
            rp8[(int) i] = Double.isNaN(p[2]) ? 0 : p[2];
            rp9[(int) i] = Double.isNaN(p[3]) ? 0 : p[3];
            rp10[(int) i] = Double.isNaN(p[7]) ? 0 : p[7];
            rp11[(int) i] = Double.isNaN(p[9]) ? 0 : p[9];

            priceCall.set(i, rp0[(int) i]);
            deltaCall.set(i, rp1[(int) i]);
            gammaCall.set(i, rp2[(int) i]);
            vegaCall.set(i, rp3[(int) i]);
            thetaCall.set(i, rp4[(int) i]);
            rhoCall.set(i, rp5[(int) i]);

            pricePut.set(i, rp6[(int) i]);
            deltaPut.set(i, rp7[(int) i]);
            gammaPut.set(i, rp8[(int) i]);
            vegaPut.set(i, rp9[(int) i]);
            thetaPut.set(i, rp10[(int) i]);
            rhoPut.set(i, rp11[(int) i]);
        }

        p = BlackScholesFormula.calculate(exercisePrice,
                underlyingPrice, riskFreeRate, i, volatility);

        rp0[(int) i] = Double.isNaN(p[0]) ? 0 : p[0];
        rp1[(int) i] = Double.isNaN(p[4]) ? 0 : p[4];
        rp2[(int) i] = Double.isNaN(p[2]) ? 0 : p[2];
        rp3[(int) i] = Double.isNaN(p[3]) ? 0 : p[3];
        rp4[(int) i] = Double.isNaN(p[6]) ? 0 : p[6];
        rp5[(int) i] = Double.isNaN(p[8]) ? 0 : p[8];

        rp6[(int) i] = Double.isNaN(p[1]) ? 0 : p[1];
        rp7[(int) i] = Double.isNaN(p[5]) ? 0 : p[5];
        rp8[(int) i] = Double.isNaN(p[2]) ? 0 : p[2];
        rp9[(int) i] = Double.isNaN(p[3]) ? 0 : p[3];
        rp10[(int) i] = Double.isNaN(p[7]) ? 0 : p[7];
        rp11[(int) i] = Double.isNaN(p[9]) ? 0 : p[9];

        priceCall.set(i, rp0[(int) i]);
        deltaCall.set(i, rp1[(int) i]);
        gammaCall.set(i, rp2[(int) i]);
        vegaCall.set(i, rp3[(int) i]);
        thetaCall.set(i, rp4[(int) i]);
        rhoCall.set(i, rp5[(int) i]);

        pricePut.set(i, rp6[(int) i]);
        deltaPut.set(i, rp7[(int) i]);
        gammaPut.set(i, rp8[(int) i]);
        vegaPut.set(i, rp9[(int) i]);
        thetaPut.set(i, rp10[(int) i]);
        rhoPut.set(i, rp11[(int) i]);

        //------------------------------------------------------------

        for (int d = 0; d < endLoop; d++) {
            rp0[d] *= -1;
            rp1[d] *= -1;
            rp2[d] *= -1;
            rp3[d] *= -1;
            rp4[d] *= -1;
            rp5[d] *= -1;

            rp6[d] *= -1;
            rp7[d] *= -1;
            rp8[d] *= -1;
            rp9[d] *= -1;
            rp10[d] *= -1;
            rp11[d] *= -1;
        }

        //--------------------------------------------------------

        for (i = 0.0d; i < endLoop - 1; i++) {

            r_priceCall.set(i, rp0[(int) i]);
            r_deltaCall.set(i, rp1[(int) i]);
            r_gammaCall.set(i, rp2[(int) i]);
            r_vegaCall.set(i, rp3[(int) i]);
            r_thetaCall.set(i, rp4[(int) i]);
            r_rhoCall.set(i, rp5[(int) i]);

            r_pricePut.set(i, rp6[(int) i]);
            r_deltaPut.set(i, rp7[(int) i]);
            r_gammaPut.set(i, rp8[(int) i]);
            r_vegaPut.set(i, rp9[(int) i]);
            r_thetaPut.set(i, rp10[(int) i]);
            r_rhoPut.set(i, rp11[(int) i]);
        }

        r_priceCall.set(i, rp0[(int) i]);
        r_deltaCall.set(i, rp1[(int) i]);
        r_gammaCall.set(i, rp2[(int) i]);
        r_vegaCall.set(i, rp3[(int) i]);
        r_thetaCall.set(i, rp4[(int) i]);
        r_rhoCall.set(i, rp5[(int) i]);

        r_pricePut.set(i, rp6[(int) i]);
        r_deltaPut.set(i, rp7[(int) i]);
        r_gammaPut.set(i, rp8[(int) i]);
        r_vegaPut.set(i, rp9[(int) i]);
        r_thetaPut.set(i, rp10[(int) i]);
        r_rhoPut.set(i, rp11[(int) i]);

        priceCallOptionLineModel.addSeries(priceCall);
        pricePutCallLineModel.addSeries(pricePut);
        r_PriceCallOptionLineModel.addSeries(r_priceCall);
        r_PricePutCallLineModel.addSeries(r_pricePut);

        deltaCallOptionLineModel.addSeries(deltaCall);
        deltaPutCallLineModel.addSeries(deltaPut);
        r_DeltaCallOptionLineModel.addSeries(r_deltaCall);
        r_DeltaPutCallLineModel.addSeries(r_deltaPut);

        gammaCallOptionLineModel.addSeries(gammaCall);
        gammaPutCallLineModel.addSeries(gammaPut);
        r_GammaCallOptionLineModel.addSeries(r_gammaCall);
        r_GammaPutCallLineModel.addSeries(r_gammaPut);

        thetaCallOptionLineModel.addSeries(thetaCall);
        thetaPutCallLineModel.addSeries(thetaPut);
        r_ThetaCallOptionLineModel.addSeries(r_thetaCall);
        r_ThetaPutCallLineModel.addSeries(r_thetaPut);

        rhoCallOptionLineModel.addSeries(rhoCall);
        rhoPutCallLineModel.addSeries(rhoPut);
        r_RhoCallOptionLineModel.addSeries(r_rhoCall);
        r_RhoPutCallLineModel.addSeries(r_rhoPut);

        vegaCallOptionLineModel.addSeries(vegaCall);
        vegaPutCallLineModel.addSeries(vegaPut);
        r_VegaCallOptionLineModel.addSeries(r_vegaCall);
        r_VegaPutCallLineModel.addSeries(r_vegaPut);

        try {

            if (graphType.equals("All") && graphCategory.equals("All")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/allAll.xhtml");
            } else if (graphType.equals("All") && graphCategory.equals("Option_Price")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/allOptionPrice.xhtml");
            } else if (graphType.equals("All") && graphCategory.equals("Delta")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/allDelta.xhtml");
            } else if (graphType.equals("All") && graphCategory.equals("Gamma")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/allGamma.xhtml");
            } else if (graphType.equals("All") && graphCategory.equals("Vega")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/allVega.xhtml");
            } else if (graphType.equals("All") && graphCategory.equals("Theta")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/allTheta.xhtml");
            } else if (graphType.equals("All") && graphCategory.equals("RHO")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/allRHO.xhtml");
            } else if (graphType.equals("Put") && graphCategory.equals("All")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/putAll.xhtml");
            } else if (graphType.equals("Put") && graphCategory.equals("Option_Price")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/putOptionPrice.xhtml");
            } else if (graphType.equals("Put") && graphCategory.equals("Delta")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/putDelta.xhtml");
            } else if (graphType.equals("Put") && graphCategory.equals("Gamma")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/putGamma.xhtml");
            } else if (graphType.equals("Put") && graphCategory.equals("Vega")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/putVega.xhtml");
            } else if (graphType.equals("Put") && graphCategory.equals("Theta")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/putTheta.xhtml");
            } else if (graphType.equals("Put") && graphCategory.equals("RHO")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/putRHO.xhtml");
            } else if (graphType.equals("Call") && graphCategory.equals("All")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/callAll.xhtml");
            } else if (graphType.equals("Call") && graphCategory.equals("Option_Price")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/callOptionPrice.xhtml");
            } else if (graphType.equals("Call") && graphCategory.equals("Delta")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/callDelta.xhtml");
            } else if (graphType.equals("Call") && graphCategory.equals("Gamma")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/callGamma.xhtml");
            } else if (graphType.equals("Call") && graphCategory.equals("Vega")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/callVega.xhtml");
            } else if (graphType.equals("Call") && graphCategory.equals("Theta")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/callTheta.xhtml");
            } else if (graphType.equals("Call") && graphCategory.equals("RHO")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/financial/basic/standardgraph/callRHO.xhtml");
            }


        } catch (IOException e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
        }
    }

    public String getGraphCategory() {
        return graphCategory;
    }

    public void setGraphCategory(String graphCategory) {
        this.graphCategory = graphCategory;
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public String getGraphObject() {
        return graphObject;
    }

    public void setGraphObject(String graphObject) {
        this.graphObject = graphObject;
    }

    public double getExercisePrice() {
        return exercisePrice;
    }

    public void setExercisePrice(double exercisePrice) {
        this.exercisePrice = exercisePrice;
    }

    public double getUnderlyingPrice() {
        return underlyingPrice;
    }

    public void setUnderlyingPrice(double underlyingPrice) {
        this.underlyingPrice = underlyingPrice;
    }

    public double getTimeToExpiration() {
        return timeToExpiration;
    }

    public void setTimeToExpiration(double timeToExpiration) {
        this.timeToExpiration = timeToExpiration;
    }

    public double getRiskFreeRate() {
        return riskFreeRate;
    }

    public void setRiskFreeRate(double riskFreeRate) {
        this.riskFreeRate = riskFreeRate;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public double getCallOptionPrice() {
        return callOptionPrice;
    }

    public void setCallOptionPrice(double callOptionPrice) {
        this.callOptionPrice = callOptionPrice;
    }

    public double getPutOptionPrice() {
        return putOptionPrice;
    }

    public void setPutOptionPrice(double putOptionPrice) {
        this.putOptionPrice = putOptionPrice;
    }

    public double getCallOptionDelta() {
        return callOptionDelta;
    }

    public void setCallOptionDelta(double callOptionDelta) {
        this.callOptionDelta = callOptionDelta;
    }

    public double getPutOptionDelta() {
        return putOptionDelta;
    }

    public void setPutOptionDelta(double putOptionDelta) {
        this.putOptionDelta = putOptionDelta;
    }

    public double getCallOptionGamma() {
        return callOptionGamma;
    }

    public void setCallOptionGamma(double callOptionGamma) {
        this.callOptionGamma = callOptionGamma;
    }

    public double getPutOptionGamma() {
        return putOptionGamma;
    }

    public void setPutOptionGamma(double putOptionGamma) {
        this.putOptionGamma = putOptionGamma;
    }

    public double getCallOptionTheta() {
        return callOptionTheta;
    }

    public void setCallOptionTheta(double callOptionTheta) {
        this.callOptionTheta = callOptionTheta;
    }

    public double getPutOptionTheta() {
        return putOptionTheta;
    }

    public void setPutOptionTheta(double putOptionTheta) {
        this.putOptionTheta = putOptionTheta;
    }

    public double getCallOptionVega() {
        return callOptionVega;
    }

    public void setCallOptionVega(double callOptionVega) {
        this.callOptionVega = callOptionVega;
    }

    public double getPutOptionVega() {
        return putOptionVega;
    }

    public void setPutOptionVega(double putOptionVega) {
        this.putOptionVega = putOptionVega;
    }

    public double getCallOptionRHO() {
        return callOptionRHO;
    }

    public void setCallOptionRHO(double callOptionRHO) {
        this.callOptionRHO = callOptionRHO;
    }

    public double getPutOptionRHO() {
        return putOptionRHO;
    }

    public void setPutOptionRHO(double putOptionRHO) {
        this.putOptionRHO = putOptionRHO;
    }

    public LineChartModel getPriceCallOptionLineModel() {
        return priceCallOptionLineModel;
    }

    public void setPriceCallOptionLineModel(LineChartModel priceCallOptionLineModel) {
        this.priceCallOptionLineModel = priceCallOptionLineModel;
    }

    public LineChartModel getPricePutCallLineModel() {
        return pricePutCallLineModel;
    }

    public void setPricePutCallLineModel(LineChartModel pricePutCallLineModel) {
        this.pricePutCallLineModel = pricePutCallLineModel;
    }

    public LineChartModel getR_PriceCallOptionLineModel() {
        return r_PriceCallOptionLineModel;
    }

    public void setR_PriceCallOptionLineModel(LineChartModel r_PriceCallOptionLineModel) {
        this.r_PriceCallOptionLineModel = r_PriceCallOptionLineModel;
    }

    public LineChartModel getR_PricePutCallLineModel() {
        return r_PricePutCallLineModel;
    }

    public void setR_PricePutCallLineModel(LineChartModel r_PricePutCallLineModel) {
        this.r_PricePutCallLineModel = r_PricePutCallLineModel;
    }

    public LineChartModel getDeltaCallOptionLineModel() {
        return deltaCallOptionLineModel;
    }

    public void setDeltaCallOptionLineModel(LineChartModel deltaCallOptionLineModel) {
        this.deltaCallOptionLineModel = deltaCallOptionLineModel;
    }

    public LineChartModel getDeltaPutCallLineModel() {
        return deltaPutCallLineModel;
    }

    public void setDeltaPutCallLineModel(LineChartModel deltaPutCallLineModel) {
        this.deltaPutCallLineModel = deltaPutCallLineModel;
    }

    public LineChartModel getR_DeltaCallOptionLineModel() {
        return r_DeltaCallOptionLineModel;
    }

    public void setR_DeltaCallOptionLineModel(LineChartModel r_DeltaCallOptionLineModel) {
        this.r_DeltaCallOptionLineModel = r_DeltaCallOptionLineModel;
    }

    public LineChartModel getR_DeltaPutCallLineModel() {
        return r_DeltaPutCallLineModel;
    }

    public void setR_DeltaPutCallLineModel(LineChartModel r_DeltaPutCallLineModel) {
        this.r_DeltaPutCallLineModel = r_DeltaPutCallLineModel;
    }

    public LineChartModel getGammaCallOptionLineModel() {
        return gammaCallOptionLineModel;
    }

    public void setGammaCallOptionLineModel(LineChartModel gammaCallOptionLineModel) {
        this.gammaCallOptionLineModel = gammaCallOptionLineModel;
    }

    public LineChartModel getGammaPutCallLineModel() {
        return gammaPutCallLineModel;
    }

    public void setGammaPutCallLineModel(LineChartModel gammaPutCallLineModel) {
        this.gammaPutCallLineModel = gammaPutCallLineModel;
    }

    public LineChartModel getR_GammaCallOptionLineModel() {
        return r_GammaCallOptionLineModel;
    }

    public void setR_GammaCallOptionLineModel(LineChartModel r_GammaCallOptionLineModel) {
        this.r_GammaCallOptionLineModel = r_GammaCallOptionLineModel;
    }

    public LineChartModel getR_GammaPutCallLineModel() {
        return r_GammaPutCallLineModel;
    }

    public void setR_GammaPutCallLineModel(LineChartModel r_GammaPutCallLineModel) {
        this.r_GammaPutCallLineModel = r_GammaPutCallLineModel;
    }

    public LineChartModel getThetaCallOptionLineModel() {
        return thetaCallOptionLineModel;
    }

    public void setThetaCallOptionLineModel(LineChartModel thetaCallOptionLineModel) {
        this.thetaCallOptionLineModel = thetaCallOptionLineModel;
    }

    public LineChartModel getThetaPutCallLineModel() {
        return thetaPutCallLineModel;
    }

    public void setThetaPutCallLineModel(LineChartModel thetaPutCallLineModel) {
        this.thetaPutCallLineModel = thetaPutCallLineModel;
    }

    public LineChartModel getR_ThetaCallOptionLineModel() {
        return r_ThetaCallOptionLineModel;
    }

    public void setR_ThetaCallOptionLineModel(LineChartModel r_ThetaCallOptionLineModel) {
        this.r_ThetaCallOptionLineModel = r_ThetaCallOptionLineModel;
    }

    public LineChartModel getR_ThetaPutCallLineModel() {
        return r_ThetaPutCallLineModel;
    }

    public void setR_ThetaPutCallLineModel(LineChartModel r_ThetaPutCallLineModel) {
        this.r_ThetaPutCallLineModel = r_ThetaPutCallLineModel;
    }

    public LineChartModel getRhoCallOptionLineModel() {
        return rhoCallOptionLineModel;
    }

    public void setRhoCallOptionLineModel(LineChartModel rhoCallOptionLineModel) {
        this.rhoCallOptionLineModel = rhoCallOptionLineModel;
    }

    public LineChartModel getRhoPutCallLineModel() {
        return rhoPutCallLineModel;
    }

    public void setRhoPutCallLineModel(LineChartModel rhoPutCallLineModel) {
        this.rhoPutCallLineModel = rhoPutCallLineModel;
    }

    public LineChartModel getR_RhoCallOptionLineModel() {
        return r_RhoCallOptionLineModel;
    }

    public void setR_RhoCallOptionLineModel(LineChartModel r_RhoCallOptionLineModel) {
        this.r_RhoCallOptionLineModel = r_RhoCallOptionLineModel;
    }

    public LineChartModel getR_RhoPutCallLineModel() {
        return r_RhoPutCallLineModel;
    }

    public void setR_RhoPutCallLineModel(LineChartModel r_RhoPutCallLineModel) {
        this.r_RhoPutCallLineModel = r_RhoPutCallLineModel;
    }

    public LineChartModel getVegaCallOptionLineModel() {
        return vegaCallOptionLineModel;
    }

    public void setVegaCallOptionLineModel(LineChartModel vegaCallOptionLineModel) {
        this.vegaCallOptionLineModel = vegaCallOptionLineModel;
    }

    public LineChartModel getVegaPutCallLineModel() {
        return vegaPutCallLineModel;
    }

    public void setVegaPutCallLineModel(LineChartModel vegaPutCallLineModel) {
        this.vegaPutCallLineModel = vegaPutCallLineModel;
    }

    public LineChartModel getR_VegaCallOptionLineModel() {
        return r_VegaCallOptionLineModel;
    }

    public void setR_VegaCallOptionLineModel(LineChartModel r_VegaCallOptionLineModel) {
        this.r_VegaCallOptionLineModel = r_VegaCallOptionLineModel;
    }

    public LineChartModel getR_VegaPutCallLineModel() {
        return r_VegaPutCallLineModel;
    }

    public void setR_VegaPutCallLineModel(LineChartModel r_VegaPutCallLineModel) {
        this.r_VegaPutCallLineModel = r_VegaPutCallLineModel;
    }
}
