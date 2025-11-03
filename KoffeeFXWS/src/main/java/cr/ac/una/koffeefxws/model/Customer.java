/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * @author quesadx
 */
@Entity
@Table(name = "CUSTOMER")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"),
  @NamedQuery(name = "Customer.findById", query = "SELECT c FROM Customer c WHERE c.id = :id"),
  @NamedQuery(
      name = "Customer.findByFirstName",
      query = "SELECT c FROM Customer c WHERE c.firstName = :firstName"),
  @NamedQuery(
      name = "Customer.findByLastName",
      query = "SELECT c FROM Customer c WHERE c.lastName = :lastName"),
  @NamedQuery(
      name = "Customer.findByEmail",
      query = "SELECT c FROM Customer c WHERE c.email = :email"),
  @NamedQuery(
      name = "Customer.findByPhone",
      query = "SELECT c FROM Customer c WHERE c.phone = :phone"),
  @NamedQuery(
      name = "Customer.findByCreationDate",
      query = "SELECT c FROM Customer c WHERE c.creationDate = :creationDate"),
})
public class Customer implements Serializable {

  private static final long serialVersionUID = 1L;

  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
  @SequenceGenerator(name = "customer_seq", sequenceName = "seq_customer_id", allocationSize = 1)
  @Basic(optional = false)
  @Column(name = "CUSTOMER_ID")
  private Long id;

  @Size(max = 150)
  @Column(name = "FIRST_NAME")
  private String firstName;

  @Size(max = 150)
  @Column(name = "LAST_NAME")
  private String lastName;

  // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
  @Size(max = 200)
  @Column(name = "EMAIL")
  private String email;

  // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax
  // format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using
  // this annotation to enforce field validation
  @Size(max = 50)
  @Column(name = "PHONE")
  private String phone;

  @Column(name = "CREATION_DATE")
  // @Temporal(TemporalType.TIMESTAMP)
  private LocalDate creationDate;

  @Version
  @Column(name = "VERSION")
  private Long version;

  @OneToMany(mappedBy = "customerId", fetch = FetchType.LAZY)
  private List<CustomerOrder> customerOrderList;

  @OneToMany(mappedBy = "customerId", fetch = FetchType.LAZY)
  private List<Invoice> invoiceList;

  public Customer() {}

  public Customer(Long customerId) {
    this.id = customerId;
  }

  public Customer(CustomerDTO dto) {
    this.id = dto.getId();
    actualizar(dto);
  }

  public void actualizar(CustomerDTO dto) {
    this.firstName = dto.getFirstName();
    this.lastName = dto.getLastName();
    this.email = dto.getEmail();
    this.phone = dto.getPhone();
  }

  /** Lifecycle hook: Automatically sets creation date before persisting */
  @PrePersist
  protected void onCreate() {
    if (creationDate == null) {
      creationDate = LocalDate.now();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long customerId) {
    this.id = customerId;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  @XmlTransient
  public List<CustomerOrder> getCustomerOrderList() {
    return customerOrderList;
  }

  public void setCustomerOrderList(List<CustomerOrder> customerOrderList) {
    this.customerOrderList = customerOrderList;
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
    if (!(object instanceof Customer)) {
      return false;
    }
    Customer other = (Customer) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cr.ac.una.koffeefxws.model.Customer[ customerId=" + id + " ]";
  }
}
