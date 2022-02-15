package lethbridge.controller;


import lethbridge.core.BaseController;
import org.primefaces.component.commandbutton.CommandButton;
import util.R;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class NavigationCtrl extends BaseController implements Serializable {

    public String getRedirect() {
        showPage(R.PageAddresses.HOME_PAGE);
        //showPage(R.PageAddresses.HOME_PAGE);
        return null;
    }


    public void navigateOthers(ActionEvent event) {
        CommandButton commandButton = (CommandButton) event.getSource();
        String id = commandButton.getId();
        String pageName = ((String) commandButton.getValue())
                .replaceAll(R.PublicText.SPACE, R.PublicText.EMPTY);

        if (id.startsWith("PFS")) {
            showPage(String.format(R.PageAddresses.FORMATED_PROFESSOR, "professor", pageName));

        } else if (id.startsWith("STU")) {
            showPage(String.format(R.PageAddresses.FORMATED_PROFESSOR, "student", pageName));
        } else {
            showPage(String.format(R.PageAddresses.FORMATED_OTHERS, "others", pageName));
        }
    }


    public void navigateSign(ActionEvent event) {
        String pageName = ((String) ((CommandButton) event.getSource()).getValue())
                .replaceAll(R.PublicText.SPACE, R.PublicText.EMPTY);
        showPage(String.format(R.PageAddresses.FORMATED_SIGN, pageName));
    }

    public void logout() {

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/logout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPage(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
