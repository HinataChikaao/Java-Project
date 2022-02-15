package core;


import model.entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PizzaProps;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class BaseController {

    private final Logger PizzaLogger;

    public BaseController() {
        this.PizzaLogger = LoggerFactory.getLogger(BaseController.class.getName());
    }

    public String getContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public void redirect(String pageName) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(getContextPath() + pageName);
    }


    public ServletContext getPizzaServletContext() {

        return ((HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true)).getServletContext();
    }

    protected HttpSession getPizzaSession() {
        HttpServletRequest request = (HttpServletRequest)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();
        return request.getSession();
    }


    protected void setSessionObject(String name, Object object) {
        HttpServletRequest request = (HttpServletRequest)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();
        request.getSession().setAttribute(name, object);
    }

    protected Object getSessionObject(String name) {
        HttpServletRequest request = (HttpServletRequest)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();
        return request.getSession().getAttribute(name);
    }

    protected void clearSessionObject(String name) {
        HttpServletRequest request = (HttpServletRequest)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();
        request.getSession().removeAttribute(name);
    }


    protected HttpServletRequest getPizzaRequest() {
        return (HttpServletRequest)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();
    }

    protected HttpServletResponse getPizzaResponse() {
        return (HttpServletResponse)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getResponse();
    }


    protected void pizzaRedirect(String path) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(getPizzaRequest().getContextPath() + path);
    }

    protected void PizzaUserLogin(String username, String password) throws ServletException {
        getPizzaRequest().login(username, password);
    }

    protected boolean hasUserRoleOf(String roleName) {
        return getPizzaRequest().isUserInRole(roleName);
    }

    protected String getPizzaRemoteAddr() {
        return getPizzaRequest().getRemoteAddr();
    }

    protected String getPizzaHostName() {
        return getPizzaRequest().getRemoteHost();
    }

    protected CustomerEntity getPizzaUser() {
        return (CustomerEntity) getPizzaSession().getAttribute(PizzaProps.variables.CUSTOMER);
    }


    /* Message Method ---------------------------------------------------------------------------------------------- */

    protected void showInfoMessage(String detail) {

        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.INFO, detail);

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, PizzaProps.Messages.INFO, detail);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.info(logMsg);
    }

    protected void showInfoMessage(String detail, Throwable ex) {

        String msg = String.join(PizzaProps.PublicText.SPACE, detail, ex.getMessage());
        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.INFO, msg);

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, PizzaProps.Messages.INFO, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.info(logMsg);
    }

    protected void showInfoMessage(Exception ex) {

        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.INFO, ex.getMessage());

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, PizzaProps.Messages.INFO, ex.getMessage());
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.info(logMsg);
    }


    protected void showWarnMessage(String detail) {

        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.WARNING, detail);

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, PizzaProps.Messages.WARNING, detail);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.warn(logMsg);

    }

    protected void showWarnMessage(String detail, Throwable ex) {

        String msg = String.join(PizzaProps.PublicText.SPACE, detail, ex.getMessage());
        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.WARNING, msg);

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, PizzaProps.Messages.WARNING, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.warn(logMsg);
    }

    protected void showWarnMessage(Throwable ex) {

        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.WARNING, ex.getMessage());

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, PizzaProps.Messages.WARNING, ex.getMessage());
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.warn(logMsg);
    }


    protected void showErrMessage(String detail) {

        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.ERROR, detail);

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, PizzaProps.Messages.ERROR, detail);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.error(logMsg);
    }

    protected void showErrMessage(String detail, Throwable ex) {

        String msg = String.join(PizzaProps.PublicText.SPACE, detail, ex.getMessage());
        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.ERROR, msg);

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, PizzaProps.Messages.ERROR, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.error(logMsg);
    }

    protected void showErrMessage(Throwable ex) {

        String logMsg = String.join(PizzaProps.PublicText.SPACE, PizzaProps.Messages.ERROR, ex.getMessage());

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, PizzaProps.Messages.ERROR, ex.getMessage());
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        PizzaLogger.error(logMsg);
    }

}
