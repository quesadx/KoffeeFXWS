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
 * Data Transfer Object for DiningTable entity
 * 
 * @author quesadx
 */
public class DiningTableDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private byte[] image;
    private Integer xPos;
    private Integer yPos;
    private Integer width;
    private Integer height;
    private String status;
    private Long version;
    private Long diningAreaId;
    private String diningAreaName;
    private Boolean modified;
    private List<CustomerOrderDTO> customerOrders;

    public DiningTableDTO() {
        this.customerOrders = new ArrayList<>();
        this.modified = false;
        // DB constraint allows FREE/OCCUPIED/RESERVED; use FREE as default
        this.status = "FREE";
    }

    public DiningTableDTO(DiningTable diningTable) {
        this();
        this.id = diningTable.getId();
        this.name = diningTable.getName();
        this.image = diningTable.getImage();
        this.xPos = diningTable.getXPos();
        this.yPos = diningTable.getYPos();
        this.width = diningTable.getWidth();
        this.height = diningTable.getHeight();
        this.status = diningTable.getStatus();
        this.version = diningTable.getVersion();
        
        if (diningTable.getDiningAreaId() != null) {
            this.diningAreaId = diningTable.getDiningAreaId().getId();
            this.diningAreaName = diningTable.getDiningAreaId().getName();
        }
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getXPos() {
        return xPos;
    }

    public void setXPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getYPos() {
        return yPos;
    }

    public void setYPos(Integer yPos) {
        this.yPos = yPos;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getDiningAreaId() {
        return diningAreaId;
    }

    public void setDiningAreaId(Long diningAreaId) {
        this.diningAreaId = diningAreaId;
    }

    public String getDiningAreaName() {
        return diningAreaName;
    }

    public void setDiningAreaName(String diningAreaName) {
        this.diningAreaName = diningAreaName;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
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
        final DiningTableDTO other = (DiningTableDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "DiningTableDTO{" + "name=" + name + ", status=" + status + '}';
    }
}
