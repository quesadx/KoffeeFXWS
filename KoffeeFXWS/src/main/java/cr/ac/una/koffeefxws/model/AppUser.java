/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.koffeefxws.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * @author quesadx
 */
@Entity
@Table(name = "APP_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppUser.findAll", query = "SELECT a FROM AppUser a"),
    @NamedQuery(name = "AppUser.findById", query = "SELECT a FROM AppUser a WHERE a.id = :id"),
    @NamedQuery(
            name = "AppUser.findByFirstName",
            query = "SELECT a FROM AppUser a WHERE a.firstName = :firstName"),
    @NamedQuery(
            name = "AppUser.findByLastName",
            query = "SELECT a FROM AppUser a WHERE a.lastName = :lastName"),
    @NamedQuery(
            name = "AppUser.findByUsername",
            query = "SELECT a FROM AppUser a WHERE a.username = :username"),
    @NamedQuery(
            name = "AppUser.findByPassword",
            query = "SELECT a FROM AppUser a WHERE a.password = :password"),
    @NamedQuery(
            name = "AppUser.findByEmail",
            query = "SELECT a FROM AppUser a WHERE a.email = :email"),
    @NamedQuery(
            name = "AppUser.findByIsActive",
            query = "SELECT a FROM AppUser a WHERE a.isActive = :isActive"),
    @NamedQuery(
            name = "AppUser.findByCreationDate",
            query = "SELECT a FROM AppUser a WHERE a.creationDate = :creationDate"),
})
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
    // annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq")
    @SequenceGenerator(name = "app_user_seq", sequenceName = "seq_app_user_id", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Size(max = 100)
    @Column(name = "LAST_NAME")
    private String lastName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "USERNAME")
    private String username;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "PASSWORD")
    private String password;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 200)
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "IS_ACTIVE")
    private Character isActive;

    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATION_DATE")
    private LocalDate creationDate;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId", fetch = FetchType.LAZY)
    private List<CashOpening> cashOpeningList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<CustomerOrder> customerOrderList;

    @Column(name = "USER_ROLE")
    private Character userRole;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<Invoice> invoiceList;

    public AppUser() {}

    public AppUser(Long id) {
        this.id = id;
    }

    public AppUser(
            Long id, String firstName, String username, String password, LocalDate creationDate) {
        this.id = id;
        this.firstName = firstName;
        this.username = username;
        this.password = password;
        this.creationDate = creationDate;
    }

    public AppUser(AppUserDTO dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(AppUserDTO dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.username = dto.getUsername();
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            this.password = dto.getPassword();
        }
        this.email = dto.getEmail();
        this.isActive = dto.getIsActive() != null && dto.getIsActive() ? 'Y' : 'N';
        this.userRole = dto.getUserRole();
    }

    /** Lifecycle hook: Automatically sets creation date before persisting */
    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
        if (isActive == null) {
            isActive = 'Y';
        }
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

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
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

    @XmlTransient
    public List<CashOpening> getCashOpeningList() {
        return cashOpeningList;
    }

    public void setCashOpeningList(List<CashOpening> cashOpeningList) {
        this.cashOpeningList = cashOpeningList;
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
        if (!(object instanceof AppUser)) {
            return false;
        }
        AppUser other = (AppUser) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.AppUser[ id=" + id + " ]";
    }
}
