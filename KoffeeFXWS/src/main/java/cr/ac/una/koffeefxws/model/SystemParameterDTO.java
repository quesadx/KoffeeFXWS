/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Data Transfer Object for SystemParameter entity
 * 
 * @author quesadx
 */
public class SystemParameterDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String paramName;
    private String paramValue;
    private String description;
    private Boolean modified;

    public SystemParameterDTO() {
        this.modified = false;
    }

    public SystemParameterDTO(SystemParameter systemParameter) {
        this();
        this.id = systemParameter.getId();
        this.paramName = systemParameter.getParamName();
        this.paramValue = systemParameter.getParamValue();
        this.description = systemParameter.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
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
        final SystemParameterDTO other = (SystemParameterDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "SystemParameterDTO{" + "paramName=" + paramName + ", paramValue=" + paramValue + '}';
    }
}
