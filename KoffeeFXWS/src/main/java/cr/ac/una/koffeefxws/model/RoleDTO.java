/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author quesadx
 */
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private Boolean modified;
    private List<AppUserDTO> appUsers;
    private List<AppUserDTO> deletedAppUsers;

    public RoleDTO() {
        this.appUsers = new ArrayList<>();
        this.deletedAppUsers = new ArrayList<>();
        this.modified = false;
    }

    public RoleDTO(Role role) {
        this();
        this.id = role.getId();
        this.code = role.getCode();
        this.name = role.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public List<AppUserDTO> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(List<AppUserDTO> appUsers) {
        this.appUsers = appUsers;
    }

    public List<AppUserDTO> getDeletedAppUsers() {
        return deletedAppUsers;
    }

    public void setDeletedAppUsers(List<AppUserDTO> deletedAppUsers) {
        this.deletedAppUsers = deletedAppUsers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RoleDTO other = (RoleDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "RoleDTO{" + "code=" + code + ", name=" + name + '}';
    }
}
