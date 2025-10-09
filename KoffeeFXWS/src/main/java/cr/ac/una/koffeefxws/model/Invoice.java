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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author quesadx
 */
@Entity
@Table(name = "INVOICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findByInvoiceId", query = "SELECT i FROM Invoice i WHERE i.invoiceId = :invoiceId"),
    @NamedQuery(name = "Invoice.findByInvoiceNumber", query = "SELECT i FROM Invoice i WHERE i.invoiceNumber = :invoiceNumber"),
    @NamedQuery(name = "Invoice.findBySubtotal", query = "SELECT i FROM Invoice i WHERE i.subtotal = :subtotal"),
    @NamedQuery(name = "Invoice.findByTaxRate", query = "SELECT i FROM Invoice i WHERE i.taxRate = :taxRate"),
    @NamedQuery(name = "Invoice.findByServiceRate", query = "SELECT i FROM Invoice i WHERE i.serviceRate = :serviceRate"),
    @NamedQuery(name = "Invoice.findByDiscountRate", query = "SELECT i FROM Invoice i WHERE i.discountRate = :discountRate"),
    @NamedQuery(name = "Invoice.findByTotal", query = "SELECT i FROM Invoice i WHERE i.total = :total"),
    @NamedQuery(name = "Invoice.findByAmountReceived", query = "SELECT i FROM Invoice i WHERE i.amountReceived = :amountReceived"),
    @NamedQuery(name = "Invoice.findByChangeAmount", query = "SELECT i FROM Invoice i WHERE i.changeAmount = :changeAmount"),
    @NamedQuery(name = "Invoice.findByCreatedAt", query = "SELECT i FROM Invoice i WHERE i.createdAt = :createdAt"),
    @NamedQuery(name = "Invoice.findByIsPrinted", query = "SELECT i FROM Invoice i WHERE i.isPrinted = :isPrinted"),
    @NamedQuery(name = "Invoice.findByIsEmailSent", query = "SELECT i FROM Invoice i WHERE i.isEmailSent = :isEmailSent"),
    @NamedQuery(name = "Invoice.findByPaymentMethod", query = "SELECT i FROM Invoice i WHERE i.paymentMethod = :paymentMethod")})
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INVOICE_ID")
    private BigDecimal invoiceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "SUBTOTAL")
    private BigDecimal subtotal;
    @Column(name = "TAX_RATE")
    private BigDecimal taxRate;
    @Column(name = "SERVICE_RATE")
    private BigDecimal serviceRate;
    @Column(name = "DISCOUNT_RATE")
    private BigDecimal discountRate;
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Column(name = "AMOUNT_RECEIVED")
    private BigDecimal amountReceived;
    @Column(name = "CHANGE_AMOUNT")
    private BigDecimal changeAmount;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "IS_PRINTED")
    private Character isPrinted;
    @Column(name = "IS_EMAIL_SENT")
    private Character isEmailSent;
    @Size(max = 20)
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AppUser createdBy;
    @JoinColumn(name = "CASH_OPENING_ID", referencedColumnName = "CASH_OPENING_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private CashOpening cashOpeningId;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customerId;
    @JoinColumn(name = "CUSTOMER_ORDER_ID", referencedColumnName = "CUSTOMER_ORDER_ID")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private CustomerOrder customerOrderId;

    public Invoice() {
    }

    public Invoice(BigDecimal invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Invoice(BigDecimal invoiceId, String invoiceNumber) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(BigDecimal invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(BigDecimal serviceRate) {
        this.serviceRate = serviceRate;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Character getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(Character isPrinted) {
        this.isPrinted = isPrinted;
    }

    public Character getIsEmailSent() {
        return isEmailSent;
    }

    public void setIsEmailSent(Character isEmailSent) {
        this.isEmailSent = isEmailSent;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public CashOpening getCashOpeningId() {
        return cashOpeningId;
    }

    public void setCashOpeningId(CashOpening cashOpeningId) {
        this.cashOpeningId = cashOpeningId;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public CustomerOrder getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(CustomerOrder customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceId != null ? invoiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.invoiceId == null && other.invoiceId != null) || (this.invoiceId != null && !this.invoiceId.equals(other.invoiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.Invoice[ invoiceId=" + invoiceId + " ]";
    }
    
}
