package servlets;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "Register User",urlPatterns = "/Registeration")
@RequestScoped
public class Registration extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("usernamesignup").trim();
        String email = request.getParameter("emailsignup").trim();
        String password = request.getParameter("passwordsignup").trim();
        String conf_pass = request.getParameter("passwordsignup_confirm").trim();

        String regPattern = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern pattern = Pattern.compile(regPattern);

        try {
            Matcher matcher = pattern.matcher(email);
            if (matcher.find()) {
                if (password.equals(conf_pass)) {
                    /*UsersMgr usersManager = (UsersMgr) BeanProvider.getBean("usersManager");
                    RolesMgr rolesManager = (RolesMgr) BeanProvider.getBean("rolesManager");
                    UserEntity userTo = (UserEntity) BeanProvider.getBean("userTo");
                    RoleEntity roleTo = (RoleEntity) BeanProvider.getBean("roleTo");

                    userTo.setUserID(username);
                    userTo.setEmail(email);
                    userTo.setPassword(EncoderUtil.getMD5(password));
                    usersManager.registerUser(userTo);

                    roleTo.setUserID(username);
                    rolesManager.registerRole(roleTo);*/

                    response.sendRedirect("/login/login.jsp#login");
                } else {
                    throw new Exception("Passwords are different ...");
                }
            } else {
                throw new Exception("Invalid email Address ...");
            }
        } catch (Exception ex) {
            System.out.println("ex = " + ex);
            response.sendRedirect("/errors/error.jsp?message="+ ex.getMessage());
        }
    }
}
