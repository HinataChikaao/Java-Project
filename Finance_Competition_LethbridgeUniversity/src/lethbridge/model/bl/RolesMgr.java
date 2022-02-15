package lethbridge.model.bl;

import lethbridge.model.entities.RoleEntity;
import lethbridge.model.pattern.CDIBaseModel;
import util.R;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class RolesMgr extends CDIBaseModel implements Serializable {

    public void registerRole(RoleEntity roleEntity) throws Exception {
        entityManager.persist(roleEntity);
    }

    public void removeUserRole(String userID) throws Exception {
        RoleEntity roleEntity = entityManager.find(RoleEntity.class, userID);
        if (roleEntity != null) {
            entityManager.remove(roleEntity);
        }
    }

    public String fetchUserRole(String userID) throws Exception {

        RoleEntity roleEntity = entityManager.find(RoleEntity.class, userID);
        if (roleEntity != null) {
            return roleEntity.getRoleName();
        } else {
            return R.PublicText.NO_ROLE_NAME;
        }
    }
}
