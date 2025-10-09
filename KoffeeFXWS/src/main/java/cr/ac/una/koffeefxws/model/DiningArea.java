/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author quesadx
 */
@Entity
@Table(name = "DINING_AREA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiningArea.findAll", query = "SELECT d FROM DiningArea d"),
    @NamedQuery(name = "DiningArea.findByDiningAreaId", query = "SELECT d FROM DiningArea d WHERE d.diningAreaId = :diningAreaId"),
    @NamedQuery(name = "DiningArea.findByName", query = "SELECT d FROM DiningArea d WHERE d.name = :name"),
    @NamedQuery(name = "DiningArea.findByIsBar", query = "SELECT d FROM DiningArea d WHERE d.isBar = :isBar"),
    @NamedQuery(name = "DiningArea.findByIsServiceCharged", query = "SELECT d FROM DiningArea d WHERE d.isServiceCharged = :isServiceCharged"),
    @NamedQuery(name = "DiningArea.findByIsActive", query = "SELECT d FROM DiningArea d WHERE d.isActive = :isActive")})
public class DiningArea implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DINING_AREA_ID")
    private BigDecimal diningAreaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NAME")
    private String name;
    @Column(name = "IS_BAR")
    private Character isBar;
    @Column(name = "IS_SERVICE_CHARGED")
    private Character isServiceCharged;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "diningAreaId", fetch = FetchType.LAZY)
    private List<DiningTable> diningTableList;
    @OneToMany(mappedBy = "diningAreaId", fetch = FetchType.LAZY)
    private List<CustomerOrder> customerOrderList;

    public DiningArea() {
    }

    public DiningArea(BigDecimal diningAreaId) {
        this.diningAreaId = diningAreaId;
    }

    public DiningArea(BigDecimal diningAreaId, String name) {
        this.diningAreaId = diningAreaId;
        this.name = name;
    }

    public BigDecimal getDiningAreaId() {
        return diningAreaId;
    }

    public void setDiningAreaId(BigDecimal diningAreaId) {
        this.diningAreaId = diningAreaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getIsBar() {
        return isBar;
    }

    public void setIsBar(Character isBar) {
        this.isBar = isBar;
    }

    public Character getIsServiceCharged() {
        return isServiceCharged;
    }

    public void setIsServiceCharged(Character isServiceCharged) {
        this.isServiceCharged = isServiceCharged;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

    @XmlTransient
    public List<DiningTable> getDiningTableList() {
        return diningTableList;
    }

    public void setDiningTableList(List<DiningTable> diningTableList) {
        this.diningTableList = diningTableList;
    }

    @XmlTransient
    public List<CustomerOrder> getCustomerOrderList() {
        return customerOrderList;
    }

    public void setCustomerOrderList(List<CustomerOrder> customerOrderList) {
        this.customerOrderList = customerOrderList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diningAreaId != null ? diningAreaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiningArea)) {
            return false;
        }
        DiningArea other = (DiningArea) object;
        if ((this.diningAreaId == null && other.diningAreaId != null) || (this.diningAreaId != null && !this.diningAreaId.equals(other.diningAreaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.DiningArea[ diningAreaId=" + diningAreaId + " ]";
    }
    
}
