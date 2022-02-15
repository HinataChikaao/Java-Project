package servlets;

import lethbridge.core.EntityValidator;
import lethbridge.model.bl.LogBookMgr;
import lethbridge.model.bl.UsersMgr;
import lethbridge.model.entities.LogBookEntity;
import lethbridge.model.entities.UserEntity;
import org.slf4j.LoggerFactory;
import util.ConstantText;
import util.R;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "Logout User", urlPatterns = "/logout")
public class Logout extends HttpServlet {

    @Inject
    private UsersMgr usersMgr;

    @Inject
    private LogBookMgr logBookMgr;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        try {
            HttpSession session = request.getSession();

            UserEntity user = (UserEntity) session.getAttribute("user");
            if (user != null) {
                try {
                    usersMgr.editUserOnlineFlag(user.getUserID(), 0);
                } catch (Exception ex) {
                    LoggerFactory.getLogger(Logout.class.getName()).info(ex.getMessage());
                }
            }

            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                session.removeAttribute(attributeNames.nextElement());
            }

            request.getSession().invalidate();
            request.logout();
            if (user != null) {
                LoggerFactory.getLogger(Logout.class.getName()).info(user.getUserID() + " logged out ...");
                LogBookEntity logBookEntity = new LogBookEntity(user.getUserID(), R.ActionNames.LOG_OUT);
                String validate = EntityValidator.validate(logBookEntity);
                if (validate.equals(ConstantText.NO_MESSAGE)) {
                    logBookMgr.registerLog(logBookEntity);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", validate));
                }
            } else {
                LoggerFactory.getLogger(Logout.class.getName()).info(" logged out ...");
            }

            response.sendRedirect(R.PageAddresses.HOME_PAGE);

        } catch (Exception e) {
            LoggerFactory.getLogger(Logout.class.getName()).info(e.getMessage());
        }
    }
}
