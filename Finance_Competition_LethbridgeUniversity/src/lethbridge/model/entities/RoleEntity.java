package lethbridge.model.entities;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.io.Serializable;
import java.util.Date;

@Stateful
@LocalBean

@javax.persistence.Entity
@javax.persistence.Table(name = "ROLESDATA", schema = "LETHBRIDGE")
public class RoleEntity implements Serializable {

    private String userName;
    private String roleName;
    private Date timeStamp;

    public RoleEntity() {
    }

    public RoleEntity(String userName, String roleName) {
        this.userName = userName;
        this.roleName = roleName;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "User username number not entered")
    @javax.validation.constraints.NotNull(message = "User username number not entered")
    @javax.validation.constraints.Size(min = 3, max = 256, message = "Minimum username characters numbers is 3 and the maximum is 256")
    @javax.persistence.Id
    @javax.persistence.Column(name = "USER_ID", columnDefinition = "varchar2(256)")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @org.hibernate.validator.constraints.NotEmpty(message = "Role name not entered")
    @javax.validation.constraints.NotNull(message = "Role name not entered")
    @javax.validation.constraints.Size(min = 3, max = 256, message = "Minimum role name characters numbers is 3 and the maximum is 256")
    @javax.persistence.Basic()
    @javax.persistence.Column(name = "ROLE_NAME", columnDefinition = "varchar2(256)")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String role_name) {
        this.roleName = role_name;
    }

    @javax.persistence.Version
    @javax.persistence.Column(name = "TIME_STAMP", columnDefinition = "Date")
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
