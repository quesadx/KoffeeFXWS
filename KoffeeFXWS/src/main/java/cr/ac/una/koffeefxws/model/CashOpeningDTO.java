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
 * @author quesadx
 */
public class CashOpeningDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private LocalDate openingDate;
  private Long initialAmount;
  private Boolean isClosed;
  private LocalDate closingDate;
  private Long closingAmount;
  private String notes;
  private Long version;
  private Long userId;
  private String userName;
  private Boolean modified;
  private List<InvoiceDTO> invoices;

  public CashOpeningDTO() {
    this.invoices = new ArrayList<>();
    this.modified = false;
    this.isClosed = false;
  }

  public CashOpeningDTO(CashOpening cashOpening) {
    this();
    this.id = cashOpening.getId();
    this.openingDate = cashOpening.getOpeningDate();
    this.initialAmount = cashOpening.getInitialAmount();
    this.isClosed = cashOpening.getIsClosed() != null && cashOpening.getIsClosed().equals('Y');
    this.closingDate = cashOpening.getClosingDate();
    this.closingAmount = cashOpening.getClosingAmount();
    this.notes = cashOpening.getNotes();
    this.version = cashOpening.getVersion();

    if (cashOpening.getUserId() != null) {
      this.userId = cashOpening.getUserId().getId();
      this.userName = cashOpening.getUserId().getUsername();
    }
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

  public Boolean getIsClosed() {
    return isClosed;
  }

  public void setIsClosed(Boolean isClosed) {
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

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Boolean getModified() {
    return modified;
  }

  public void setModified(Boolean modified) {
    this.modified = modified;
  }

  public List<InvoiceDTO> getInvoices() {
    return invoices;
  }

  public void setInvoices(List<InvoiceDTO> invoices) {
    this.invoices = invoices;
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
    final CashOpeningDTO other = (CashOpeningDTO) obj;
    return Objects.equals(this.id, other.id);
  }

  @Override
  public String toString() {
    return ("CashOpeningDTO{" + "openingDate=" + openingDate + ", isClosed=" + isClosed + '}');
  }
}
