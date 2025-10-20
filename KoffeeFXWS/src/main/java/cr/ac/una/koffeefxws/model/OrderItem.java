/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "ORDER_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderItem.findAll", query = "SELECT o FROM OrderItem o"),
    @NamedQuery(name = "OrderItem.findById", query = "SELECT o FROM OrderItem o WHERE o.id = :id"),
    @NamedQuery(name = "OrderItem.findByQuantity", query = "SELECT o FROM OrderItem o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "OrderItem.findByUnitPrice", query = "SELECT o FROM OrderItem o WHERE o.unitPrice = :unitPrice"),
    @NamedQuery(name = "OrderItem.findByStatus", query = "SELECT o FROM OrderItem o WHERE o.status = :status")})
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    @SequenceGenerator(name = "order_item_seq", sequenceName = "seq_order_item_id", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ITEM_ID")
    private Long id;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIT_PRICE")
    private Double unitPrice;
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

    public OrderItem(Long itemId) {
        this.id = itemId;
    }

    public OrderItem(Long itemId, Double unitPrice) {
        this.id = itemId;
        this.unitPrice = unitPrice;
    }

    public OrderItem(OrderItemDTO dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(OrderItemDTO dto) {
        this.quantity = dto.getQuantity();
        this.unitPrice = dto.getUnitPrice();
        this.status = dto.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long itemId) {
        this.id = itemId;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderItem)) {
            return false;
        }
        OrderItem other = (OrderItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.OrderItem[ itemId=" + id + " ]";
    }
    
}
