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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author quesadx
 */
@Entity
@Table(name = "CASH_OPENING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CashOpening.findAll", query = "SELECT c FROM CashOpening c"),
    @NamedQuery(name = "CashOpening.findById", query = "SELECT c FROM CashOpening c WHERE c.id = :id"),
    @NamedQuery(name = "CashOpening.findByOpeningDate", query = "SELECT c FROM CashOpening c WHERE c.openingDate = :openingDate"),
    @NamedQuery(name = "CashOpening.findByInitialAmount", query = "SELECT c FROM CashOpening c WHERE c.initialAmount = :initialAmount"),
    @NamedQuery(name = "CashOpening.findByIsClosed", query = "SELECT c FROM CashOpening c WHERE c.isClosed = :isClosed"),
    @NamedQuery(name = "CashOpening.findByClosingDate", query = "SELECT c FROM CashOpening c WHERE c.closingDate = :closingDate"),
    @NamedQuery(name = "CashOpening.findByClosingAmount", query = "SELECT c FROM CashOpening c WHERE c.closingAmount = :closingAmount"),
    @NamedQuery(name = "CashOpening.findByNotes", query = "SELECT c FROM CashOpening c WHERE c.notes = :notes")})
public class CashOpening implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CASH_OPENING_ID")
    private Long id;
    @Column(name = "OPENING_DATE")
    private LocalDate openingDate;
    @Column(name = "INITIAL_AMOUNT")
    private Long initialAmount;
    @Column(name = "IS_CLOSED")
    private Character isClosed;
    @Column(name = "CLOSING_DATE")
    private LocalDate closingDate;
    @Column(name = "CLOSING_AMOUNT")
    private Long closingAmount;
    @Size(max = 2000)
    @Column(name = "NOTES")
    private String notes;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AppUser userId;
    @OneToMany(mappedBy = "cashOpeningId", fetch = FetchType.LAZY)
    private List<Invoice> invoiceList;

    public CashOpening() {
    }

    public CashOpening(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public Long getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(Long initialAmount) {
        this.initialAmount = initialAmount;
    }

    public Character getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Character isClosed) {
        this.isClosed = isClosed;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public Long getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(Long closingAmount) {
        this.closingAmount = closingAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AppUser getUserId() {
        return userId;
    }

    public void setUserId(AppUser userId) {
        this.userId = userId;
    }

    @XmlTransient
    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
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
        if (!(object instanceof CashOpening)) {
            return false;
        }
        CashOpening other = (CashOpening) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.CashOpening[ id=" + id + " ]";
    }
    
}
