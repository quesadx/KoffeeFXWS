/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object for CustomerOrder entity
 * 
 * @author quesadx
 */
public class CustomerOrderDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String status;
    private LocalDate createdAt;
    private Date updatedAt;
    private Long totalAmount;
    private Long version;
    private Long createdBy;
    private String createdByName;
    private Long customerId;
    private String customerName;
    private Long diningAreaId;
    private String diningAreaName;
    private Long diningTableId;
    private String diningTableName;
    private Boolean modified;
    private List<OrderItemDTO> orderItems;
    private List<OrderItemDTO> deletedOrderItems;
    private InvoiceDTO invoice;

    public CustomerOrderDTO() {
        this.orderItems = new ArrayList<>();
        this.deletedOrderItems = new ArrayList<>();
        this.modified = false;
        this.status = "PENDING";
    }

    public CustomerOrderDTO(CustomerOrder customerOrder) {
        this();
        this.id = customerOrder.getId();
        this.status = customerOrder.getStatus();
        this.createdAt = customerOrder.getCreatedAt();
        this.updatedAt = customerOrder.getUpdatedAt();
        this.totalAmount = customerOrder.getTotalAmount();
        this.version = customerOrder.getVersion();
        
        if (customerOrder.getCreatedBy() != null) {
            this.createdBy = customerOrder.getCreatedBy().getId();
            this.createdByName = customerOrder.getCreatedBy().getUsername();
        }
        
        if (customerOrder.getCustomerId() != null) {
            this.customerId = customerOrder.getCustomerId().getId();
            this.customerName = customerOrder.getCustomerId().getFirstName() + " " + 
                            customerOrder.getCustomerId().getLastName();
        }
        
        if (customerOrder.getDiningAreaId() != null) {
            this.diningAreaId = customerOrder.getDiningAreaId().getId();
            this.diningAreaName = customerOrder.getDiningAreaId().getName();
        }
        
        if (customerOrder.getDiningTableId() != null) {
            this.diningTableId = customerOrder.getDiningTableId().getId();
            this.diningTableName = customerOrder.getDiningTableId().getName();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Long getDiningTableId() {
        return diningTableId;
    }

    public void setDiningTableId(Long diningTableId) {
        this.diningTableId = diningTableId;
    }

    public String getDiningTableName() {
        return diningTableName;
    }

    public void setDiningTableName(String diningTableName) {
        this.diningTableName = diningTableName;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItemDTO> getDeletedOrderItems() {
        return deletedOrderItems;
    }

    public void setDeletedOrderItems(List<OrderItemDTO> deletedOrderItems) {
        this.deletedOrderItems = deletedOrderItems;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
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
        final CustomerOrderDTO other = (CustomerOrderDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CustomerOrderDTO{" + "id=" + id + ", status=" + status + ", totalAmount=" + totalAmount + '}';
    }
}
