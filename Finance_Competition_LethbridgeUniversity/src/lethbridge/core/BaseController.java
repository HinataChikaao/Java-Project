package lethbridge.core;


import lethbridge.exceptions.BeanCheckConstraintException;
import lethbridge.model.bl.LogBookMgr;
import lethbridge.model.entities.LogBookEntity;
import lethbridge.model.entities.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConstantText;
import util.R;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public abstract class BaseController {

    @Inject
    private LogBookMgr logBookMgr;

    private Logger univLogger;

    public BaseController() {
        this.univLogger = LoggerFactory.getLogger(BaseController.class.getName());
    }

    protected HttpSession getUnivSession() {
        HttpServletRequest request = (HttpServletRequest)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();
        return request.getSession();
    }

    protected String getUnivID() {
        return getUnivSession().getId();
    }

    protected HttpServletRequest getUnivRequest() {
        return (HttpServletRequest)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();
    }

    protected HttpServletResponse getUnivResponse() {
        return (HttpServletResponse)
                FacesContext.getCurrentInstance()
                        .getExternalContext().getResponse();
    }

    protected void redirect(String path) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(getUnivRequest().getContextPath() + path);
    }

    protected void userLogin(String userID, String password) throws ServletException {
        getUnivRequest().login(userID, password);
    }

    protected boolean hasUserRoleOf(String roleName) {
        return getUnivRequest().isUserInRole(roleName);
    }

    protected UserEntity getUnivUser() {
        return (UserEntity) getUnivSession().getAttribute("user");
    }

    protected void setUnivUser(UserEntity user) {
        getUnivSession().setAttribute("user", user);
    }

    protected String getUnivRemoteAddr() {
        return getUnivRequest().getRemoteAddr();
    }

    protected String getUnivHostName() {
        return getUnivRequest().getRemoteHost();
    }

    protected void showInfoMessage(String msg) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, R.Messages.Information.INFO, msg);
        FacesContext.getCurrentInstance().addMessage("", facesMessage);
        univLogger.info(msg + " ... [" + getUnivUser().getUserID() + "]");
    }

    protected void showWarnMessage(String msg) {

        FacesMessage facesMessage = new FacesMessage(
                FacesMessage.SEVERITY_WARN,
                R.Messages.Warning.WARN,
                msg);
        FacesContext.getCurrentInstance().addMessage(R.PublicText.EMPTY, facesMessage);
        univLogger.warn(msg + " ... [" + getUnivUser().getUserID() + "]");
    }

    protected void showErrorMessage(ExceptionPack exceptionPack) {
        UserEntity univUser = getUnivUser();
        String user;

        if (univUser == null) {
            user = R.PublicText.EMPTY;
        } else {
            user = univUser.getUserID();
        }

        FacesContext.getCurrentInstance().addMessage(R.PublicText.EMPTY,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, R.Messages.Error.ERROR,
                        exceptionPack.getErrorMessage()));
        univLogger.error(exceptionPack.toString() + " ... [" + user + "]");
    }

    protected void saveLog(String userID, String actionName, String message) throws Exception {

        if ((userID == null || userID.trim().length() == 0) ||
                (actionName == null || actionName.trim().length() == 0)) {
            throw new Exception("Invalid User ID or Action name ...");
        }

        LogBookEntity logBookEntity = new LogBookEntity(userID.trim(), actionName.trim(),
                Optional.ofNullable(message).map(String::trim).orElse("NO MESSAGE"));
        String validate = EntityValidator.validate(logBookEntity);
        if (validate.equals(ConstantText.NO_MESSAGE)) {
            logBookMgr.registerLog(logBookEntity);
        } else {
            showWarnMessage(validate);
        }
    }

    protected void saveLog(LogBookEntity logBookEntity) throws Exception, BeanCheckConstraintException {
        if (logBookEntity.getUserID().equals("0") || logBookEntity.getActionName().equals("0")) {
            throw new Exception("Invalid User ID or Action name ...");
        }
        String validate = EntityValidator.validate(logBookEntity);
        if (validate.equals(ConstantText.NO_MESSAGE)) {
            logBookMgr.registerLog(logBookEntity);
        } else {
            throw new BeanCheckConstraintException(validate);
        }
    }

}
