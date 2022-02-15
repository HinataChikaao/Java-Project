package model.entity;

import core.Activation;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EnumType;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.sql.Timestamp;

@LocalBean
@Stateful

@javax.persistence.Entity
@javax.persistence.Table(name = "ROLES", schema = "PIZZA")
public class RolesEntity implements Serializable {

    private Long roleId;
    private String username;
    private String roleName;
    private Activation activate;
    private Timestamp timeStamp;


    @javax.persistence.Id
    @javax.persistence.SequenceGenerator(name = "roles_seq", sequenceName = "ROLES_SEQ", allocationSize = 20)
    @javax.persistence.GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
    @javax.persistence.Column(name = "ROLE_ID", columnDefinition = "NUMBER(10,0)", nullable = false, unique = true)
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "USERNAME", columnDefinition = "VARCHAR2(128)", nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @javax.persistence.Basic
    @javax.persistence.Column(name = "ROLE_NAME", columnDefinition = "VARCHAR2(128)", nullable = false)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @javax.persistence.Enumerated(EnumType.ORDINAL)
    public Activation getActivate() {
        return activate;
    }

    public void setActivate(Activation activate) {
        this.activate = activate;
    }

    @javax.persistence.Version
    @javax.persistence.Column(name = "TIME_STAMP", columnDefinition = "TIMESTAMP(6)", nullable = false)
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    @PrePersist
    private void preInsert() {
        if (this.activate == null) {
            this.activate = Activation.Active;
        }
    }
}
