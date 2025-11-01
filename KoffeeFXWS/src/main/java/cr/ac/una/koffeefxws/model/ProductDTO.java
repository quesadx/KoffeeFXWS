/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author quesadx
 */
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String shortName;
    private Double price;
    private Boolean isQuickMenu;
    private Boolean isActive;
    private LocalDate createdAt;
    private Double purchaseFrequency;
    private Long version;
    private Long productGroupId;
    private String productGroupName;
    private Boolean modified;
    private List<OrderItemDTO> orderItems;

    public ProductDTO() {
        this.orderItems = new ArrayList<>();
        this.modified = false;
        this.isQuickMenu = false;
        this.isActive = true;
        this.purchaseFrequency = 0.0;
    }

    public ProductDTO(Product product) {
        this();
        this.id = product.getId();
        this.name = product.getName();
        this.shortName = product.getShortName();
        this.price = product.getPrice();
        this.isQuickMenu =
            product.getIsQuickMenu() != null &&
            product.getIsQuickMenu().equals('Y');
        this.isActive =
            product.getIsActive() != null && product.getIsActive().equals('Y');
        this.createdAt = product.getCreatedAt();
        this.purchaseFrequency = product.getPurchaseFrequency();
        this.version = product.getVersion();

        if (product.getProductGroupId() != null) {
            this.productGroupId = product.getProductGroupId().getId();
            this.productGroupName = product.getProductGroupId().getName();
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsQuickMenu() {
        return isQuickMenu;
    }

    public void setIsQuickMenu(Boolean isQuickMenu) {
        this.isQuickMenu = isQuickMenu;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Double getPurchaseFrequency() {
        return purchaseFrequency;
    }

    public void setPurchaseFrequency(Double purchaseFrequency) {
        this.purchaseFrequency = purchaseFrequency;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(Long productGroupId) {
        this.productGroupId = productGroupId;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
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
        final ProductDTO other = (ProductDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ProductDTO{" + "name=" + name + ", price=" + price + '}';
    }
}
