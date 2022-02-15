package lethbridge.controller;

import lethbridge.core.BaseController;
import lethbridge.core.EntityValidator;
import lethbridge.core.ExceptionPack;
import lethbridge.exceptions.BeanCheckConstraintException;
import lethbridge.model.bl.*;
import lethbridge.model.entities.RoleEntity;
import lethbridge.model.entities.UserEntity;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConstantText;
import util.R;
import util.StatusContainer;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UserRegistrationCtrl extends BaseController implements Serializable {

    private static final String PROFESSOR = "professor";
    private static final String STUDENT = "student";
    private static final String ADMIN = "admin";
    private static final String ADD_USER = "Add User";
    private static final String CHANGE_PASSWORD = "Change Password";
    private static final String DELETE_OR_RESET_ACCOUNT = "Delete Admin or Professor";
    private static final String RESET_ALL_ACCOUNT = "Reset or Delete All Students";
    private static final String RESET_ALL_SELECTED_ACCOUNT = "Reset or Delete Selected Students";
    private static final String LYNDA = "lynda";

    private Logger univLogger;

    private String name;
    private String family;
    private String userID;
    private String password;
    private String confirm;
    private Double studentInitialCash;
    private String roleName;
    private String newPassword;
    private String adminUsername;
    private String adminPassword;

    private String[] selectedStudents;
    private List<String> studentList;

    private UserEntity userEntity;
    private UserEntity curUser;
    private RoleEntity roleEntity;

    @Inject
    private AccountValueMgr accountValueMgr;

    @Inject
    private RolesMgr rolesMgr;

    @Inject
    private TradingMgr tradingMgr;

    @Inject
    private UsersMgr usersMgr;

    @Inject
    private LogBookMgr logBookMgr;

    @Inject
    private ResetAllUsersMgr resetAllUsersMgr;

    @Inject
    private DeleteAllUsersMgr deleteAllUsersMgr;

    private StatusContainer addUser;
    private StatusContainer changePassword;
    private StatusContainer deleteUser;
    private StatusContainer resetAllAccount;
    private StatusContainer resetAllSelectedAccount;

    @PostConstruct
    private void init() {

        userEntity = new UserEntity();
        roleEntity = new RoleEntity();
        curUser = getUnivUser();

        univLogger = LoggerFactory.getLogger(UserRegistrationCtrl.class.getName());

        addUser = new StatusContainer(AddUser.class, StatusContainer.StatusType.Enable);
        resetAllAccount = new StatusContainer(ResetAllAccount.class, StatusContainer.StatusType.Disable);
        resetAllSelectedAccount = new StatusContainer(ResetAllSelectedAccount.class, StatusContainer.StatusType.Disable);
        changePassword = new StatusContainer(ChangePassword.class, StatusContainer.StatusType.Disable);
        deleteUser = new StatusContainer(DeleteUser.class, StatusContainer.StatusType.Disable);

        roleName = PROFESSOR;
        studentInitialCash = 0.0d;
        adminUsername = R.PublicText.EMPTY;
        adminPassword = R.PublicText.EMPTY;

        investmentStatus();
        univLogger.info(String.format("%s entered to the Student Page.", curUser.toString()));
    }

    private void loadStudents() {
        int index = 0;
        try {
            List<UserEntity> allUsers = usersMgr.getAllUsers(true);
            selectedStudents = new String[allUsers.size()];
            for (UserEntity userEntity : allUsers) {
                selectedStudents[index++] = userEntity.getUserID();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void investmentStatus() {
        if (roleName.equals(ADMIN) || roleName.equals(PROFESSOR)) {
            addUser.disable(AddUser.Investment);
        } else if (roleName.equals(STUDENT)) {
            addUser.enable(AddUser.Investment);
        }
    }

    public void OnTabChange(TabChangeEvent event) {

        String title = event.getTab().getTitle();
        addUser.disableAll();
        changePassword.disableAll();
        deleteUser.disableAll();
        resetAllAccount.disableAll();
        resetAllSelectedAccount.disableAll();

        switch (title) {
            case ADD_USER: {
                addUser.enableAll();
                break;
            }
            case CHANGE_PASSWORD: {
                changePassword.enableAll();
                break;
            }
            case DELETE_OR_RESET_ACCOUNT: {
                deleteUser.enableAll();
                break;
            }
            case RESET_ALL_ACCOUNT: {
                resetAllAccount.enableAll();
                break;
            }
            case RESET_ALL_SELECTED_ACCOUNT: {
                resetAllSelectedAccount.enableAll();
                loadStudents();
                break;
            }
        }
    }

    @Transactional
    public void register() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String validateMessage;

        if (password.equals(confirm)) {

            userEntity.setUserID(userID);
            userEntity.setPassword(password);
            userEntity.setFirstName(name);
            userEntity.setLastName(family);
            userEntity.setInitialInvestment(studentInitialCash);
            userEntity.setCurrentInvestment(studentInitialCash);
            userEntity.setUserActive(1);
            userEntity.setUserCount(0);
            userEntity.setUserOnline(0);

            roleEntity.setUserName(userID);
            roleEntity.setRoleName(roleName);

            try {
                validateMessage = EntityValidator.validate(userEntity);
                if (validateMessage.equals(ConstantText.NO_MESSAGE)) {
                    usersMgr.registerUser(userEntity);
                    rolesMgr.registerRole(roleEntity);

                    if (roleEntity.getRoleName().equals("student")) {
                        accountValueMgr.registerUser(userID);
                    }

                    showInfoMessage(userID + " is registered as " + roleName + " ...");
                    PrimeFaces.current().ajax().update("toolbarForm:message");
                } else {
                    throw new BeanCheckConstraintException(validateMessage);
                }
            } catch (Exception ex) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(), methodName,
                        "user cannot be created ..."));
                PrimeFaces.current().ajax().update("toolbarForm:message");
            } catch (BeanCheckConstraintException e) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(), methodName,
                        e.getMessage()));
                PrimeFaces.current().ajax().update("toolbarForm:message");
            }
        } else {
            showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(),
                    methodName, R.Messages.Error.INVALID_PASS_COMPARISON));
            PrimeFaces.current().ajax().update("toolbarForm:message");
        }

        univLogger.info(String.format("%s (Admin) registered a new user with %s user ID.",
                userEntity.toString(), userID));
    }

    @Transactional
    public void delete() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (userID.trim().length() > 0 && password.trim().length() > 0) {
            try {
                String role = rolesMgr.fetchUserRole(userID).trim();
                boolean validUser = (role.equals(ADMIN) || role.equals(PROFESSOR))
                        && (!userID.equals(LYNDA));
                if (usersMgr.isUserExist(userID, password) && validUser) {
                    if (usersMgr.getUserOnlineFlag(userID.trim()) == 0) {
                        deleteUser();
                    } else {
                        PrimeFaces.current().executeScript("PF('warnDeleteDialog').show();");
                    }
                } else {
                    showInfoMessage(R.Messages.Information.USER_NOT_EXIT);
                }
            } catch (Exception ex) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(),
                        methodName, ex.getMessage()));
            }
        } else {
            showWarnMessage("Enter Admin username and password ...");
        }

        PrimeFaces.current().ajax().update("toolbarForm:message");
        univLogger.info(String.format("%s (Admin) deleted a user with %s user ID.",
                userEntity.toString(), userID));
    }

    public void deleteUser() throws Exception {
        accountValueMgr.removeUser(userID);
        tradingMgr.removeTradingInfo(userID.trim());
        rolesMgr.removeUserRole(userID);
        logBookMgr.removeLog(userID);
        usersMgr.removeUser(userID.trim(), password.trim());
        showInfoMessage(R.Messages.Information.USER_DEL_SUCCESSFULLY);
    }

    @Transactional
    public void deleteAllUsers() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (adminUsername.trim().length() > 0 && adminPassword.trim().length() > 0) {
            try {
                if (usersMgr.isUserExist(adminUsername, adminPassword) &&
                        getUnivRequest().isUserInRole(R.PublicText.ADMIN) &&
                        rolesMgr.fetchUserRole(curUser.getUserID()).equals(R.PublicText.ADMIN)) {
                    deleteAllUsersMgr.removeAllUsers(R.PublicText.STUDENT);
                    showInfoMessage("All student users were delete successfully ...");
                } else {
                    showInfoMessage(R.Messages.Information.USER_NOT_EXIT_OR_NOT_ADMIN);
                }
            } catch (Exception ex) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(),
                        methodName, ex.getMessage()));
            }
        } else {
            showWarnMessage("Enter Admin username and password ...");
        }

        PrimeFaces.current().ajax().update("toolbarForm:message");
        univLogger.info(String.format("%s (Admin) reset a user with %s user ID.",
                userEntity.toString(), userID));
    }

    @Transactional
    public void deleteAllSelectedUsers() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (adminUsername.trim().length() > 0 && adminPassword.trim().length() > 0) {
            try {
                if (usersMgr.isUserExist(adminUsername, adminPassword) &&
                        getUnivRequest().isUserInRole(R.PublicText.ADMIN) &&
                        rolesMgr.fetchUserRole(curUser.getUserID()).equals(R.PublicText.ADMIN)) {
                    if (studentList != null && studentList.size() > 0) {
                        deleteSelectedUsers();
                        loadStudents();
                        showInfoMessage("All student users were delete successfully ...");
                    } else {
                        showWarnMessage("No student have selected ...");
                    }
                } else {
                    showInfoMessage(R.Messages.Information.USER_NOT_EXIT_OR_NOT_ADMIN);
                }
            } catch (Exception ex) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(),
                        methodName, ex.getMessage()));
            }
        } else {
            showWarnMessage("Enter Admin username and password ...");
        }

        PrimeFaces.current().ajax().update("toolbarForm:message");
        univLogger.info(String.format("%s (Admin) reset a user with %s user ID.",
                userEntity.toString(), userID));
    }

    private void deleteSelectedUsers() throws Exception {
        for (String uid : studentList) {
            tradingMgr.removeTradingInfo(uid.trim());
            accountValueMgr.removeUser(uid.trim());
            rolesMgr.removeUserRole(uid.trim());
            usersMgr.removeUserByID(uid.trim());
            logBookMgr.removeLog(uid.trim());
        }
    }

    @Transactional
    public void reset() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (userID.trim().length() > 0 && password.trim().length() > 0) {
            try {

                String role = rolesMgr.fetchUserRole(userID).trim();
                boolean validUser = (role.equals(ADMIN) || role.equals(PROFESSOR)) && (!userID.equals(LYNDA));

                if (usersMgr.isUserExist(userID, password) && validUser) {

                    if (usersMgr.getUserOnlineFlag(userID.trim()) == 0) {
                        resetUser();
                    } else {
                        PrimeFaces.current().executeScript("PF('warnRestDialog').show();");
                    }
                } else {
                    showInfoMessage(R.Messages.Information.USER_NOT_EXIT);

                }
            } catch (Exception ex) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(),
                        methodName, ex.getMessage()));
            }
        } else {
            showWarnMessage("Enter Admin username and password ...");
        }

        PrimeFaces.current().ajax().update("toolbarForm:message");
        univLogger.info(String.format("%s (Admin) reset a user with %s user ID.",
                userEntity.toString(), userID));
    }

    public void resetUser() throws Exception {
        tradingMgr.removeTradingInfo(userID);
        accountValueMgr.resetUser(userID);
        usersMgr.reinitializeCurrentInvestment(userID);
        logBookMgr.removeLog(userID);
        showInfoMessage(R.Messages.Information.USER_RESET_SUCCESSFULLY);
    }

    @Transactional
    public void resetAllUsers() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (adminUsername.trim().length() > 0 && adminPassword.trim().length() > 0) {
            try {
                if (usersMgr.isUserExist(adminUsername, adminPassword) &&
                        getUnivRequest().isUserInRole(R.PublicText.ADMIN) &&
                        rolesMgr.fetchUserRole(curUser.getUserID()).equals(R.PublicText.ADMIN)) {
                    resetAllUsersMgr.resetTotalUsers(R.PublicText.STUDENT);
                    showInfoMessage("All student users were reset successfully ...");
                } else {
                    showInfoMessage(R.Messages.Information.USER_NOT_EXIT_OR_NOT_ADMIN);
                }
            } catch (Exception ex) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(),
                        methodName, ex.getMessage()));
            }
        } else {
            showWarnMessage("Enter Admin username and password ...");
        }

        PrimeFaces.current().ajax().update("toolbarForm:message");
        univLogger.info(String.format("%s (Admin) reset a user with %s user ID.",
                userEntity.toString(), userID));
    }

    @Transactional
    public void resetALLSelectedUsers() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (adminUsername.trim().length() > 0 && adminPassword.trim().length() > 0) {
            try {
                if (usersMgr.isUserExist(adminUsername, adminPassword) &&
                        getUnivRequest().isUserInRole(R.PublicText.ADMIN) &&
                        rolesMgr.fetchUserRole(curUser.getUserID()).equals(R.PublicText.ADMIN)) {
                    if (studentList != null && studentList.size() > 0) {
                        resetSelectedUsers();
                        showInfoMessage("All selected students were reset successfully ...");
                    } else {
                        showWarnMessage("No student have selected ...");
                    }
                } else {
                    showInfoMessage(R.Messages.Information.USER_NOT_EXIT_OR_NOT_ADMIN);

                }
            } catch (Exception ex) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(),
                        methodName, ex.getMessage()));
            }
        } else {
            showWarnMessage("Enter Admin username and password ...");
        }

        PrimeFaces.current().ajax().update("toolbarForm:message");
        univLogger.info(String.format("%s (Admin) reset a user with %s user ID.",
                userEntity.toString(), userID));
    }

    private void resetSelectedUsers() throws Exception {
        for (String uid : studentList) {
            tradingMgr.removeTradingInfo(uid.trim());
            usersMgr.reinitializeCurrentInvestment(uid.trim());
            accountValueMgr.resetUser(uid.trim());
            logBookMgr.removeLog(uid.trim());
        }
    }

    @Transactional
    public void change() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        if (userID.trim().length() > 0 &&
                password.trim().length() > 0 &&
                newPassword.trim().length() > 0) {
            try {
                if (usersMgr.getUserOnlineFlag(userID.trim()) == 0) {
                    if (!confirm.equals(newPassword)) {
                        throw new Exception("New password and the confirmation is not same ....");
                    }
                    if (usersMgr.isUserExist(userID, password)) {
                        usersMgr.changePassword(userID, newPassword);
                        showInfoMessage(R.Messages.Information.PASS_CHANGE_SUCCESSFULLY);
                    } else {
                        showInfoMessage(R.Messages.Information.USER_NOT_EXIT);
                    }
                } else {
                    showWarnMessage(R.Messages.Warning.CHANGE_PASS_DENIED);
                }
            } catch (Exception ex) {
                showErrorMessage(new ExceptionPack(UserRegistrationCtrl.class.getName(),
                        methodName, ex.getMessage()));
            }
        } else {
            showWarnMessage("Enter Admin username and password ...");
        }

        PrimeFaces.current().ajax().update("toolbarForm:message");
        univLogger.info(String.format("%s (Admin) change password a user with %s user ID.",
                userEntity.toString(), userID));
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public Double getStudentInitialCash() {
        return studentInitialCash;
    }

    public void setStudentInitialCash(Double studentInitialCash) {
        this.studentInitialCash = studentInitialCash;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public StatusContainer getAddUser() {
        return addUser;
    }

    public void setAddUser(StatusContainer addUser) {
        this.addUser = addUser;
    }

    public StatusContainer getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(StatusContainer changePassword) {
        this.changePassword = changePassword;
    }

    public StatusContainer getDeleteUser() {
        return deleteUser;
    }

    public StatusContainer getResetAllAccount() {
        return resetAllAccount;
    }

    public void setResetAllAccount(StatusContainer resetAllAccount) {
        this.resetAllAccount = resetAllAccount;
    }

    public void setDeleteUser(StatusContainer deleteUser) {
        this.deleteUser = deleteUser;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public StatusContainer getResetAllSelectedAccount() {
        return resetAllSelectedAccount;
    }

    public void setResetAllSelectedAccount(StatusContainer resetAllSelectedAccount) {
        this.resetAllSelectedAccount = resetAllSelectedAccount;
    }

    public String[] getSelectedStudents() {
        return selectedStudents;
    }

    public void setSelectedStudents(String[] selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    public List<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<String> studentList) {
        this.studentList = studentList;
    }

    private enum AddUser {
        Name,
        Family,
        Username,
        Password,
        Confirm,
        Authorization,
        Investment,
        Save
    }

    private enum ChangePassword {
        UserName,
        CurrentPassword,
        NewPassword,
        Confirm,
        ChangePassword
    }

    private enum DeleteUser {
        Username,
        Password,
        DeleteUser,
        ResetAccount
    }

    private enum ResetAllAccount {
        AdminUsername,
        AdminPassword,
        DeleteAllAccount,
        ResetAllAccount
    }


    private enum ResetAllSelectedAccount {
        AdminUsernameSTD,
        AdminPasswordSTD,
        DeleteAllAccountSTD,
        ResetAllAccountSTD
    }

}
