package lethbridge.model.bl;


import lethbridge.model.entities.UserEntity;
import lethbridge.model.pattern.CDIBaseModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class UsersMgr extends CDIBaseModel implements Serializable {

    @Inject
    private RolesMgr rolesMgr;

    public void registerUser(UserEntity userEntity) throws Exception {
        entityManager.persist(userEntity);
    }

    public void editUserOnlineFlag(String userID, int value) throws Exception {
        UserEntity userEntity = entityManager.find(UserEntity.class, userID);
        if (userEntity != null) {
            userEntity.setUserOnline(value);
        }
    }

    public int getUserOnlineFlag(String userID) throws Exception {
        UserEntity userEntity = entityManager.find(UserEntity.class, userID);
        if (userEntity != null) {
            return userEntity.getUserOnline();
        } else {
            return 0;
        }
    }

    public UserEntity findUser(String userName, String password) throws Exception {
        return entityManager.createQuery(
                "select ue from UserEntity ue where ue.userID =:uid and ue.password = :pass",
                UserEntity.class)
                .setParameter("uid", userName)
                .setParameter("pass", password)
                .getSingleResult();
    }

    public void removeUser(String userID, String password) throws Exception {
        UserEntity userEntity = entityManager.createQuery(
                "select ue from UserEntity ue where trim(ue.userID) = :uid and trim(ue.password) = :pass",
                UserEntity.class).setParameter("uid", userID)
                .setParameter("pass", password)
                .getSingleResult();
        if (userEntity != null) {
            entityManager.remove(userEntity);
        }
    }

    public void removeUserByID(String userID) throws Exception {
        UserEntity userEntity = entityManager.find(UserEntity.class, userID);
        if (userEntity != null) {
            entityManager.remove(userEntity);
        }
    }

    public boolean isUserExist(UserEntity userEntity) throws Exception {
        UserEntity existUser = entityManager.find(UserEntity.class, userEntity.getUserID());
        return existUser != null;
    }

    public boolean isUserExist(String userID, String password) throws Exception {
        UserEntity existUser = entityManager.createQuery(
                "select ue from UserEntity ue where ue.userID =:uid and ue.password = :pass", UserEntity.class)
                .setParameter("uid", userID)
                .setParameter("pass", password)
                .getSingleResult();
        return existUser != null;
    }

    public void changePassword(String userID, String password) throws Exception {
        UserEntity userEntity = entityManager.find(UserEntity.class, userID);
        if (userEntity != null) {
            userEntity.setPassword(password);
        } else {
            throw new Exception("User not found ...");
        }
    }

    public double getInitialInvestment(String userID) throws Exception {
        UserEntity userEntity = entityManager.find(UserEntity.class, userID);
        if (userEntity != null) {
            return userEntity.getInitialInvestment();
        } else {
            throw new Exception("User not found ...");
        }
    }

    public double getCurrentInvestment(String userID) throws Exception {
        UserEntity userEntity = entityManager.find(UserEntity.class, userID);
        if (userEntity != null) {
            return userEntity.getCurrentInvestment();
        } else {
            throw new Exception("User not found ...");
        }
    }

    void editCurrentInvestment(String userID, double investment) throws Exception {
        UserEntity userEntity = entityManager.find(UserEntity.class, userID);
        if (userEntity != null) {
            userEntity.setCurrentInvestment(investment);
        }
    }

    public void reinitializeCurrentInvestment(String userID) throws Exception {
        UserEntity userEntity = entityManager.find(UserEntity.class, userID);
        if (userEntity != null) {
            userEntity.setCurrentInvestment(userEntity.getInitialInvestment());
        }
    }

    public List<UserEntity> getAllUsers(boolean onlyStudent) throws Exception {
        if (onlyStudent) {
            List<UserEntity> students = new ArrayList<>();
            List<UserEntity> allUsers = entityManager.createQuery("select ue from UserEntity ue",
                    UserEntity.class).getResultList();
            for (UserEntity user : allUsers) {
                if (rolesMgr.fetchUserRole(user.getUserID()).equals("student")) {
                    students.add(user);
                }
            }
            return students;
        } else {
            return entityManager.createQuery("select ue from UserEntity ue",
                    UserEntity.class).getResultList();
        }
    }
}
