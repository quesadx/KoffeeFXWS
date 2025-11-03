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
 * Data Transfer Object for AppUser entity
 *
 * @author quesadx
 */
public class AppUserDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String email;
  private Boolean isActive;
  private LocalDate creationDate;
  private Long version;
  private Character userRole;
  private String token;
  private Boolean modified;
  private List<CashOpeningDTO> cashOpenings;
  private List<CustomerOrderDTO> customerOrders;
  private List<InvoiceDTO> invoices;

  public AppUserDTO() {
    this.cashOpenings = new ArrayList<>();
    this.customerOrders = new ArrayList<>();
    this.invoices = new ArrayList<>();
    this.modified = false;
  }

  public AppUserDTO(AppUser appUser) {
    this();
    this.id = appUser.getId();
    this.firstName = appUser.getFirstName();
    this.lastName = appUser.getLastName();
    this.username = appUser.getUsername();
    this.password = appUser.getPassword();
    this.email = appUser.getEmail();
    this.isActive = appUser.getIsActive() != null && appUser.getIsActive().equals('Y');
    this.creationDate = appUser.getCreationDate();
    this.version = appUser.getVersion();
    this.userRole = appUser.getUserRole();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Character getUserRole() {
    return userRole;
  }

  public void setUserRole(Character userRole) {
    this.userRole = userRole;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Boolean getModified() {
    return modified;
  }

  public void setModified(Boolean modified) {
    this.modified = modified;
  }

  public List<CashOpeningDTO> getCashOpenings() {
    return cashOpenings;
  }

  public void setCashOpenings(List<CashOpeningDTO> cashOpenings) {
    this.cashOpenings = cashOpenings;
  }

  public List<CustomerOrderDTO> getCustomerOrders() {
    return customerOrders;
  }

  public void setCustomerOrders(List<CustomerOrderDTO> customerOrders) {
    this.customerOrders = customerOrders;
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
    final AppUserDTO other = (AppUserDTO) obj;
    return Objects.equals(this.id, other.id);
  }

  @Override
  public String toString() {
    return ("AppUserDTO{"
        + "username="
        + username
        + ", firstName="
        + firstName
        + ", lastName="
        + lastName
        + '}');
  }
}
