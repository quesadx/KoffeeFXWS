/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author quesadx
 */
@Entity
@Table(name = "SYSTEM_PARAMETER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemParameter.findAll", query = "SELECT s FROM SystemParameter s"),
    @NamedQuery(name = "SystemParameter.findById", query = "SELECT s FROM SystemParameter s WHERE s.id = :id"),
    @NamedQuery(name = "SystemParameter.findByParamName", query = "SELECT s FROM SystemParameter s WHERE s.paramName = :paramName"),
    @NamedQuery(name = "SystemParameter.findByParamValue", query = "SELECT s FROM SystemParameter s WHERE s.paramValue = :paramValue"),
    @NamedQuery(name = "SystemParameter.findByDescription", query = "SELECT s FROM SystemParameter s WHERE s.description = :description")})
public class SystemParameter implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PARAM_ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PARAM_NAME")
    private String paramName;
    @Size(max = 1000)
    @Column(name = "PARAM_VALUE")
    private String paramValue;
    @Size(max = 1000)
    @Column(name = "DESCRIPTION")
    private String description;

    public SystemParameter() {
    }

    public SystemParameter(Long paramId) {
        this.id = paramId;
    }

    public SystemParameter(Long paramId, String paramName) {
        this.id = paramId;
        this.paramName = paramName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long paramId) {
        this.id = paramId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemParameter)) {
            return false;
        }
        SystemParameter other = (SystemParameter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.SystemParameter[ paramId=" + id + " ]";
    }
    
}
