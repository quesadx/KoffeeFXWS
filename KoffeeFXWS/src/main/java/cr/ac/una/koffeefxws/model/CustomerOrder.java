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
import java.time.LocalDate;
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
    @NamedQuery(name = "CustomerOrder.findById", query = "SELECT c FROM CustomerOrder c WHERE c.id = :id"),
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
    private Long id;
    @Size(max = 30)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate createdAt;
    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "TOTAL_AMOUNT")
    private Long totalAmount;
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

    public CustomerOrder(Long customerOrderId) {
        this.id = customerOrderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long customerOrderId) {
        this.id = customerOrderId;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerOrder)) {
            return false;
        }
        CustomerOrder other = (CustomerOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.CustomerOrder[ customerOrderId=" + id + " ]";
    }
    
}
