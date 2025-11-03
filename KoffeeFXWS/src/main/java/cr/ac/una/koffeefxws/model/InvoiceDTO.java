/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author quesadx
 */
public class InvoiceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String invoiceNumber;
    private Double subtotal;
    private Double taxRate;
    private Double serviceRate;
    private Double discountRate;
    private Double total;
    private Double amountReceived;
    private Double changeAmount;
    private LocalDate createdAt;
    private Boolean isPrinted;
    private Boolean isEmailSent;
    private String paymentMethod;
    private Long version;
    private Long createdBy;
    private String createdByName;
    private Long cashOpeningId;
    private Long customerId;
    private String customerName;
    private Long customerOrderId;
    private Boolean modified;

    public InvoiceDTO() {
        this.modified = false;
        this.isPrinted = false;
        this.isEmailSent = false;
        this.taxRate = 0.0;
        this.serviceRate = 0.0;
        this.discountRate = 0.0;
        this.paymentMethod = "CASH";
    }

    public InvoiceDTO(Invoice invoice) {
        this();
        this.id = invoice.getId();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.subtotal = invoice.getSubtotal();
        this.taxRate = invoice.getTaxRate();
        this.serviceRate = invoice.getServiceRate();
        this.discountRate = invoice.getDiscountRate();
        this.total = invoice.getTotal();
        this.amountReceived = invoice.getAmountReceived();
        this.changeAmount = invoice.getChangeAmount();
        this.createdAt = invoice.getCreatedAt();
        this.isPrinted = invoice.getIsPrinted() != null && invoice.getIsPrinted().equals('Y');
        this.isEmailSent = invoice.getIsEmailSent() != null && invoice.getIsEmailSent().equals('Y');
        this.paymentMethod = invoice.getPaymentMethod();
        this.version = invoice.getVersion();

        if (invoice.getCreatedBy() != null) {
            this.createdBy = invoice.getCreatedBy().getId();
            this.createdByName = invoice.getCreatedBy().getUsername();
        }

        if (invoice.getCashOpeningId() != null) {
            this.cashOpeningId = invoice.getCashOpeningId().getId();
        }

        if (invoice.getCustomerId() != null) {
            this.customerId = invoice.getCustomerId().getId();
            this.customerName =
                    invoice.getCustomerId().getFirstName()
                            + " "
                            + invoice.getCustomerId().getLastName();
        }

        if (invoice.getCustomerOrderId() != null) {
            this.customerOrderId = invoice.getCustomerOrderId().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(Boolean isPrinted) {
        this.isPrinted = isPrinted;
    }

    public Boolean getIsEmailSent() {
        return isEmailSent;
    }

    public void setIsEmailSent(Boolean isEmailSent) {
        this.isEmailSent = isEmailSent;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public Long getCashOpeningId() {
        return cashOpeningId;
    }

    public void setCashOpeningId(Long cashOpeningId) {
        this.cashOpeningId = cashOpeningId;
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

    public Long getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(Long customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public Double getTaxAmount() {
        if (subtotal != null && taxRate != null) {
            return subtotal * (taxRate / 100.0);
        }
        return 0.0;
    }

    public Double getServiceAmount() {
        if (subtotal != null && serviceRate != null) {
            return subtotal * (serviceRate / 100.0);
        }
        return 0.0;
    }

    public Double getDiscountAmount() {
        if (subtotal != null && discountRate != null) {
            return subtotal * (discountRate / 100.0);
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
        final InvoiceDTO other = (InvoiceDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return ("InvoiceDTO{"
                + "invoiceNumber="
                + invoiceNumber
                + ", total="
                + total
                + ", paymentMethod="
                + paymentMethod
                + '}');
    }
}
