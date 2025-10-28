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
 * Data Transfer Object for DiningArea entity
 * 
 * @author quesadx
 */
public class DiningAreaDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private Boolean isBar;
    private Boolean isServiceCharged;
    private Boolean isActive;
    private Long version;
    private Boolean modified;
    private List<DiningTableDTO> diningTables;
    private List<DiningTableDTO> deletedDiningTables;
    private List<CustomerOrderDTO> customerOrders;

    public DiningAreaDTO() {
        this.diningTables = new ArrayList<>();
        this.deletedDiningTables = new ArrayList<>();
        this.customerOrders = new ArrayList<>();
        this.modified = false;
        this.isBar = false;
        this.isServiceCharged = false;
        this.isActive = true;
    }

    public DiningAreaDTO(DiningArea diningArea) {
        this();
        this.id = diningArea.getId();
        this.name = diningArea.getName();
        this.isBar = diningArea.getIsBar() != null && diningArea.getIsBar().equals('Y');
        this.isServiceCharged = diningArea.getIsServiceCharged() != null && diningArea.getIsServiceCharged().equals('Y');
        this.isActive = diningArea.getIsActive() != null && diningArea.getIsActive().equals('Y');
        this.version = diningArea.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsBar() {
        return isBar;
    }

    public void setIsBar(Boolean isBar) {
        this.isBar = isBar;
    }

    public Boolean getIsServiceCharged() {
        return isServiceCharged;
    }

    public void setIsServiceCharged(Boolean isServiceCharged) {
        this.isServiceCharged = isServiceCharged;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public List<DiningTableDTO> getDiningTables() {
        return diningTables;
    }

    public void setDiningTables(List<DiningTableDTO> diningTables) {
        this.diningTables = diningTables;
    }

    public List<DiningTableDTO> getDeletedDiningTables() {
        return deletedDiningTables;
    }

    public void setDeletedDiningTables(List<DiningTableDTO> deletedDiningTables) {
        this.deletedDiningTables = deletedDiningTables;
    }

    public List<CustomerOrderDTO> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrderDTO> customerOrders) {
        this.customerOrders = customerOrders;
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
        final DiningAreaDTO other = (DiningAreaDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "DiningAreaDTO{" + "name=" + name + ", isBar=" + isBar + ", isActive=" + isActive + '}';
    }
}
