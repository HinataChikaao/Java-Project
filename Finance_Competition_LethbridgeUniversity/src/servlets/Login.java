package servlets;

import lethbridge.core.EntityValidator;
import lethbridge.model.bl.LogBookMgr;
import lethbridge.model.bl.UsersMgr;
import lethbridge.model.entities.LogBookEntity;
import lethbridge.model.entities.UserEntity;
import org.jboss.logging.Logger;
import util.ConstantText;
import util.R;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestScoped
@WebServlet(name = "Login User", urlPatterns = "/login")
public class Login extends HttpServlet {

    @Inject
    private LogBookMgr logBookMgr;

    @Inject
    private UsersMgr usersMgr;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserEntity user;
        String userName = "";
        String password = "";

        try {
            userName = request.getParameter(R.PublicText.USERNAME);
            password = request.getParameter(R.PublicText.PASSWORD);

            request.login(userName, password);
            user = usersMgr.findUser(userName, password);
            usersMgr.editUserOnlineFlag(user.getUserID(), 1);

            request.getSession().setAttribute("user", user);

            LogBookEntity logBookEntity = new LogBookEntity(user.getUserID(), R.ActionNames.LOG_IN);
            String validate = EntityValidator.validate(logBookEntity);
            if (validate.equals(ConstantText.NO_MESSAGE)) {
                logBookMgr.registerLog(logBookEntity);
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", validate));
            }

            if (request.isUserInRole(R.PublicText.STUDENT)) {
                response.sendRedirect(R.PageAddresses.STUDENT_START_PAGE);
            } else if (request.isUserInRole(R.PublicText.PROFESSOR)) {
                response.sendRedirect(R.PageAddresses.PROF_START_PAGE);
            } else if (request.isUserInRole(R.PublicText.ADMIN)) {
                response.sendRedirect(R.PageAddresses.ADMIN);
            } else {
                response.sendRedirect(R.PageAddresses.ERROR_PAGE);
            }

        } catch (Exception ex) {
            Logger.getLogger("Login Servlet").log(Logger.Level.ERROR,
                    "Exception: " + ex.getMessage() + " [" + userName + "  " + password + "]");
            if (ex.getMessage().equals(R.Messages.Error.ALREADY_AUTHENTICATED)) {
                response.sendRedirect(R.PageAddresses.INDEX);
            } else {
                response.sendRedirect(R.PageAddresses.ERROR_PAGE);
            }
        }
    }


}
