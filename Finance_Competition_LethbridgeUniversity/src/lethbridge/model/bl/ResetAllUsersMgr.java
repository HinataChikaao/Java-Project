package lethbridge.model.bl;


import lethbridge.model.pattern.CDIBaseModel;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;

@Named
@ViewScoped
public class ResetAllUsersMgr extends CDIBaseModel implements Serializable {

    public void resetTotalUsers(String roleName) throws Exception {
        StoredProcedureQuery restAllUsers =
                entityManager.createStoredProcedureQuery("REST_ALL_USERS");
        restAllUsers.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        restAllUsers.setParameter(1, roleName);
        restAllUsers.execute();
    }
}
