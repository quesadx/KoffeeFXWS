/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author quesadx
 */
public class OrderItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer quantity;
    private Double unitPrice;
    private String status;
    private Long version;
    private Long customerOrderId;
    private Long productId;
    private String productName;
    private String productShortName;
    private Boolean modified;

    public OrderItemDTO() {
        this.modified = false;
        this.quantity = 1;
        this.status = "PENDING";
    }

    public OrderItemDTO(OrderItem orderItem) {
        this();
        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        this.unitPrice = orderItem.getUnitPrice();
        this.status = orderItem.getStatus();
        this.version = orderItem.getVersion();

        if (orderItem.getCustomerOrderId() != null) {
            this.customerOrderId = orderItem.getCustomerOrderId().getId();
        }

        if (orderItem.getProductId() != null) {
            this.productId = orderItem.getProductId().getId();
            this.productName = orderItem.getProductId().getName();
            this.productShortName = orderItem.getProductId().getShortName();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
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

    public Long getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(Long customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductShortName() {
        return productShortName;
    }

    public void setProductShortName(String productShortName) {
        this.productShortName = productShortName;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public Double getSubtotal() {
        if (quantity != null && unitPrice != null) {
            return quantity * unitPrice;
        }
        return 0.0;
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
        final OrderItemDTO other = (OrderItemDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return ("OrderItemDTO{"
                + "productName="
                + productName
                + ", quantity="
                + quantity
                + ", unitPrice="
                + unitPrice
                + '}');
    }
}
