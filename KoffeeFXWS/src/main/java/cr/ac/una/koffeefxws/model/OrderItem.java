/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author quesadx
 */
@Entity
@Table(name = "ORDER_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderItem.findAll", query = "SELECT o FROM OrderItem o"),
    @NamedQuery(name = "OrderItem.findByItemId", query = "SELECT o FROM OrderItem o WHERE o.itemId = :itemId"),
    @NamedQuery(name = "OrderItem.findByQuantity", query = "SELECT o FROM OrderItem o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "OrderItem.findByUnitPrice", query = "SELECT o FROM OrderItem o WHERE o.unitPrice = :unitPrice"),
    @NamedQuery(name = "OrderItem.findByStatus", query = "SELECT o FROM OrderItem o WHERE o.status = :status")})
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ITEM_ID")
    private BigDecimal itemId;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;
    @Size(max = 20)
    @Column(name = "STATUS")
    private String status;
    @JoinColumn(name = "CUSTOMER_ORDER_ID", referencedColumnName = "CUSTOMER_ORDER_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CustomerOrder customerOrderId;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product productId;

    public OrderItem() {
    }

    public OrderItem(BigDecimal itemId) {
        this.itemId = itemId;
    }

    public OrderItem(BigDecimal itemId, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getItemId() {
        return itemId;
    }

    public void setItemId(BigDecimal itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomerOrder getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(CustomerOrder customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderItem)) {
            return false;
        }
        OrderItem other = (OrderItem) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.OrderItem[ itemId=" + itemId + " ]";
    }
    
}
