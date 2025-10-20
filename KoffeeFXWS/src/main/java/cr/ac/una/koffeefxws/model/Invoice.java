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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
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
import java.time.LocalDate;

/**
 *
 * @author quesadx
 */
@Entity
@Table(name = "INVOICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findById", query = "SELECT i FROM Invoice i WHERE i.id = :id"),
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_seq")
    @SequenceGenerator(name = "invoice_seq", sequenceName = "seq_invoice_id", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "INVOICE_ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "SUBTOTAL")
    private Double subtotal;
    @Column(name = "TAX_RATE")
    private Double taxRate;
    @Column(name = "SERVICE_RATE")
    private Double serviceRate;
    @Column(name = "DISCOUNT_RATE")
    private Double discountRate;
    @Column(name = "TOTAL")
    private Double total;
    @Column(name = "AMOUNT_RECEIVED")
    private Double amountReceived;
    @Column(name = "CHANGE_AMOUNT")
    private Double changeAmount;
    @Column(name = "CREATED_AT")
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDate createdAt;
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

    public Invoice(Long invoiceId) {
        this.id = invoiceId;
    }

    public Invoice(Long invoiceId, String invoiceNumber) {
        this.id = invoiceId;
        this.invoiceNumber = invoiceNumber;
    }

    public Invoice(InvoiceDTO dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(InvoiceDTO dto) {
        this.invoiceNumber = dto.getInvoiceNumber();
        this.subtotal = dto.getSubtotal();
        this.taxRate = dto.getTaxRate();
        this.serviceRate = dto.getServiceRate();
        this.discountRate = dto.getDiscountRate();
        this.total = dto.getTotal();
        this.amountReceived = dto.getAmountReceived();
        this.changeAmount = dto.getChangeAmount();
        this.createdAt = dto.getCreatedAt();
        this.isPrinted = dto.getIsPrinted() != null && dto.getIsPrinted() ? 'Y' : 'N';
        this.isEmailSent = dto.getIsEmailSent() != null && dto.getIsEmailSent() ? 'Y' : 'N';
        this.paymentMethod = dto.getPaymentMethod();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long invoiceId) {
        this.id = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(Double serviceRate) {
        this.serviceRate = serviceRate;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(Double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public Double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.Invoice[ invoiceId=" + id + " ]";
    }
    
}
