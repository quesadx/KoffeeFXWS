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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author quesadx
 */
@Entity
@Table(name = "CUSTOMER_ORDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerOrder.findAll", query = "SELECT c FROM CustomerOrder c"),
    @NamedQuery(name = "CustomerOrder.findByCustomerOrderId", query = "SELECT c FROM CustomerOrder c WHERE c.customerOrderId = :customerOrderId"),
    @NamedQuery(name = "CustomerOrder.findByStatus", query = "SELECT c FROM CustomerOrder c WHERE c.status = :status"),
    @NamedQuery(name = "CustomerOrder.findByCreatedAt", query = "SELECT c FROM CustomerOrder c WHERE c.createdAt = :createdAt"),
    @NamedQuery(name = "CustomerOrder.findByUpdatedAt", query = "SELECT c FROM CustomerOrder c WHERE c.updatedAt = :updatedAt"),
    @NamedQuery(name = "CustomerOrder.findByTotalAmount", query = "SELECT c FROM CustomerOrder c WHERE c.totalAmount = :totalAmount")})
public class CustomerOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ORDER_ID")
    private BigDecimal customerOrderId;
    @Size(max = 30)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AppUser createdBy;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customerId;
    @JoinColumn(name = "DINING_AREA_ID", referencedColumnName = "DINING_AREA_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private DiningArea diningAreaId;
    @JoinColumn(name = "DINING_TABLE_ID", referencedColumnName = "DINING_TABLE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private DiningTable diningTableId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerOrderId", fetch = FetchType.LAZY)
    private List<OrderItem> orderItemList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customerOrderId", fetch = FetchType.LAZY)
    private Invoice invoice;

    public CustomerOrder() {
    }

    public CustomerOrder(BigDecimal customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public BigDecimal getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(BigDecimal customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public DiningArea getDiningAreaId() {
        return diningAreaId;
    }

    public void setDiningAreaId(DiningArea diningAreaId) {
        this.diningAreaId = diningAreaId;
    }

    public DiningTable getDiningTableId() {
        return diningTableId;
    }

    public void setDiningTableId(DiningTable diningTableId) {
        this.diningTableId = diningTableId;
    }

    @XmlTransient
    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerOrderId != null ? customerOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerOrder)) {
            return false;
        }
        CustomerOrder other = (CustomerOrder) object;
        if ((this.customerOrderId == null && other.customerOrderId != null) || (this.customerOrderId != null && !this.customerOrderId.equals(other.customerOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.CustomerOrder[ customerOrderId=" + customerOrderId + " ]";
    }
    
}
