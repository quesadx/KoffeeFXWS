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
 * Data Transfer Object for ProductGroup entity
 * 
 * @author quesadx
 */
public class ProductGroupDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private String shortName;
    private Boolean isQuickMenu;
    private Double purchaseFrequency;
    private Boolean modified;
    private List<ProductDTO> products;
    private List<ProductDTO> deletedProducts;

    public ProductGroupDTO() {
        this.products = new ArrayList<>();
        this.deletedProducts = new ArrayList<>();
        this.modified = false;
        this.isQuickMenu = false;
        this.purchaseFrequency = 0.0;
    }

    public ProductGroupDTO(ProductGroup productGroup) {
        this();
        this.id = productGroup.getId();
        this.name = productGroup.getName();
        this.shortName = productGroup.getShortName();
        this.isQuickMenu = productGroup.getIsQuickMenu() != null && productGroup.getIsQuickMenu().equals('Y');
        this.purchaseFrequency = productGroup.getPurchaseFrequency();
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

    public Boolean getIsQuickMenu() {
        return isQuickMenu;
    }

    public void setIsQuickMenu(Boolean isQuickMenu) {
        this.isQuickMenu = isQuickMenu;
    }

    public Double getPurchaseFrequency() {
        return purchaseFrequency;
    }

    public void setPurchaseFrequency(Double purchaseFrequency) {
        this.purchaseFrequency = purchaseFrequency;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public List<ProductDTO> getDeletedProducts() {
        return deletedProducts;
    }

    public void setDeletedProducts(List<ProductDTO> deletedProducts) {
        this.deletedProducts = deletedProducts;
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
        final ProductGroupDTO other = (ProductGroupDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ProductGroupDTO{" + "name=" + name + ", shortName=" + shortName + '}';
    }
}
