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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
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
@Table(name = "PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
    @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name = :name"),
    @NamedQuery(name = "Product.findByShortName", query = "SELECT p FROM Product p WHERE p.shortName = :shortName"),
    @NamedQuery(name = "Product.findByPrice", query = "SELECT p FROM Product p WHERE p.price = :price"),
    @NamedQuery(name = "Product.findByIsQuickMenu", query = "SELECT p FROM Product p WHERE p.isQuickMenu = :isQuickMenu"),
    @NamedQuery(name = "Product.findByIsActive", query = "SELECT p FROM Product p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "Product.findByCreatedAt", query = "SELECT p FROM Product p WHERE p.createdAt = :createdAt"),
    @NamedQuery(name = "Product.findByPurchaseFrequency", query = "SELECT p FROM Product p WHERE p.purchaseFrequency = :purchaseFrequency")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "seq_product_id", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NAME")
    private String name;
    @Size(max = 100)
    @Column(name = "SHORT_NAME")
    private String shortName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "IS_QUICK_MENU")
    private Character isQuickMenu;
    @Column(name = "IS_ACTIVE")
    private Character isActive;
    @Column(name = "CREATED_AT")
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDate createdAt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PURCHASE_FREQUENCY")
    private Double purchaseFrequency;
    @Version
    @Column(name = "VERSION")
    private Long version;
    @JoinColumn(name = "PRODUCT_GROUP_ID", referencedColumnName = "PRODUCT_GROUP_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductGroup productGroupId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId", fetch = FetchType.LAZY)
    private List<OrderItem> orderItemList;

    public Product() {
    }

    public Product(Long productId) {
        this.id = productId;
    }

    public Product(Long productId, String name, Double price, Double purchaseFrequency) {
        this.id = productId;
        this.name = name;
        this.price = price;
        this.purchaseFrequency = purchaseFrequency;
    }

    public Product(ProductDTO dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(ProductDTO dto) {
        this.name = dto.getName();
        this.shortName = dto.getShortName();
        this.price = dto.getPrice();
        this.isQuickMenu = dto.getIsQuickMenu() != null && dto.getIsQuickMenu() ? 'Y' : 'N';
        this.isActive = dto.getIsActive() != null && dto.getIsActive() ? 'Y' : 'N';
        this.purchaseFrequency = dto.getPurchaseFrequency();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long productId) {
        this.id = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Character getIsQuickMenu() {
        return isQuickMenu;
    }

    public void setIsQuickMenu(Character isQuickMenu) {
        this.isQuickMenu = isQuickMenu;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Double getPurchaseFrequency() {
        return purchaseFrequency;
    }

    public void setPurchaseFrequency(Double purchaseFrequency) {
        this.purchaseFrequency = purchaseFrequency;
    }

    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }

    public ProductGroup getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(ProductGroup productGroupId) {
        this.productGroupId = productGroupId;
    }

    @XmlTransient
    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.Product[ productId=" + id + " ]";
    }
    
}
