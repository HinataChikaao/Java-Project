package lethbridge.core;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@ViewScoped
public class TabPageName implements Serializable {

    private Map<String, String> pageNames;

    @PostConstruct
    private void init() {
        pageNames = new HashMap<>();

        pageNames.put("ImpliedVolatility.xhtml", "Implied Volatility");
        pageNames.put("PersonalizedGraph.xhtml", "Personalized Graph");
        pageNames.put("PricingAndGreeks.xhtml", "Pricing and Greeks");
        pageNames.put("StandardGraphs.xhtml", "Standard Graph");

        pageNames.put("BinomialTree.xhtml", "Binomial Tree");
        pageNames.put("Exercise.xhtml", "Exercise");
        pageNames.put("Hedging.xhtml", "Hedging");
        pageNames.put("Portfolio.xhtml", "Portfolio");
        pageNames.put("Strategy.xhtml", "Strategy");
        pageNames.put("Theory.xhtml", "Theory");

        pageNames.put("allAll.xhtml", "Standard Graph");
        pageNames.put("allDelta.xhtml", "Standard Graph");
        pageNames.put("allGamma.xhtml", "Standard Graph");
        pageNames.put("allOptionPrice.xhtml", "Standard Graph");
        pageNames.put("allRHO.xhtml", "Standard Graph");
        pageNames.put("allTheta.xhtml", "Standard Graph");
        pageNames.put("allVega.xhtml", "Standard Graph");
        pageNames.put("callAll.xhtml", "Standard Graph");
        pageNames.put("callDelta.xhtml", "Standard Graph");
        pageNames.put("callGamma.xhtml", "Standard Graph");
        pageNames.put("callOptionPrice.xhtml", "Standard Graph");
        pageNames.put("callRHO.xhtml", "Standard Graph");
        pageNames.put("callTheta.xhtml", "Standard Graph");
        pageNames.put("callVega.xhtml", "Standard Graph");
        pageNames.put("putAll.xhtml", "Standard Graph");
        pageNames.put("putDelta.xhtml", "Standard Graph");
        pageNames.put("putGamma.xhtml", "Standard Graph");
        pageNames.put("putOptionPrice.xhtml", "Standard Graph");
        pageNames.put("putRHO.xhtml", "Standard Graph");
        pageNames.put("putTheta.xhtml", "Standard Graph");
        pageNames.put("putVega.xhtml", "Standard Graph");

        pageNames.put("AdminTools.xhtml", "Ticker Graph");
        pageNames.put("Trading.xhtml", "Trading");
        pageNames.put("UserRegistration.xhtml", "User Register");
        pageNames.put("Login.xhtml", "Login");
        pageNames.put("MyAccount.xhtml", "Student Account");
        pageNames.put("Registration.xhtml", "Registration");
    }

    public String getPageName() {
        String pagePath = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestServletPath();
        int lastIndex = pagePath.lastIndexOf("/");
        String fileName = pagePath.substring(++lastIndex);
        if (fileName.trim().length() > 0) {
            return pageNames.get(fileName);
        } else {
            return pagePath;
        }
    }
}
