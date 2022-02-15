package lethbridge.model.bl;


import lethbridge.model.pattern.CDIBaseModel;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;

@Named
@ViewScoped
public class DeleteAllUsersMgr extends CDIBaseModel implements Serializable {

    public void removeAllUsers(String roleName) throws Exception {

        StoredProcedureQuery deleteAllUsers =
                entityManager.createStoredProcedureQuery("DELETE_ALL_USERS");
        deleteAllUsers.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        deleteAllUsers.setParameter(1, roleName);
        deleteAllUsers.execute();
    }

}
