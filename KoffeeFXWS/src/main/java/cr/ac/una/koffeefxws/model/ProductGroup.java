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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author quesadx
 */
@Entity
@Table(name = "PRODUCT_GROUP")
@XmlRootElement
@NamedQueries(
    {
        @NamedQuery(
            name = "ProductGroup.findAll",
            query = "SELECT p FROM ProductGroup p"
        ),
        @NamedQuery(
            name = "ProductGroup.findById",
            query = "SELECT p FROM ProductGroup p WHERE p.id = :id"
        ),
        @NamedQuery(
            name = "ProductGroup.findByName",
            query = "SELECT p FROM ProductGroup p WHERE p.name = :name"
        ),
        @NamedQuery(
            name = "ProductGroup.findByShortName",
            query = "SELECT p FROM ProductGroup p WHERE p.shortName = :shortName"
        ),
        @NamedQuery(
            name = "ProductGroup.findByIsQuickMenu",
            query = "SELECT p FROM ProductGroup p WHERE p.isQuickMenu = :isQuickMenu"
        ),
        @NamedQuery(
            name = "ProductGroup.findByPurchaseFrequency",
            query = "SELECT p FROM ProductGroup p WHERE p.purchaseFrequency = :purchaseFrequency"
        ),
    }
)
public class ProductGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "product_group_seq"
    )
    @SequenceGenerator(
        name = "product_group_seq",
        sequenceName = "seq_product_group_id",
        allocationSize = 1
    )
    @Basic(optional = false)
    @Column(name = "PRODUCT_GROUP_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NAME")
    private String name;

    @Size(max = 50)
    @Column(name = "SHORT_NAME")
    private String shortName;

    @Column(name = "IS_QUICK_MENU")
    private Character isQuickMenu;

    @Basic(optional = false)
    @NotNull
    @Column(name = "PURCHASE_FREQUENCY")
    private Double purchaseFrequency;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @OneToMany(mappedBy = "productGroupId", fetch = FetchType.LAZY)
    private List<Product> productList;

    public ProductGroup() {}

    public ProductGroup(Long productGroupId) {
        this.id = productGroupId;
    }

    public ProductGroup(
        Long productGroupId,
        String name,
        Double purchaseFrequency
    ) {
        this.id = productGroupId;
        this.name = name;
        this.purchaseFrequency = purchaseFrequency;
    }

    public ProductGroup(ProductGroupDTO dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(ProductGroupDTO dto) {
        this.name = dto.getName();
        this.shortName = dto.getShortName();
        this.isQuickMenu = dto.getIsQuickMenu() != null && dto.getIsQuickMenu()
            ? 'Y'
            : 'N';
        this.purchaseFrequency = dto.getPurchaseFrequency();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long productGroupId) {
        this.id = productGroupId;
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

    public Character getIsQuickMenu() {
        return isQuickMenu;
    }

    public void setIsQuickMenu(Character isQuickMenu) {
        this.isQuickMenu = isQuickMenu;
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

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
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
        if (!(object instanceof ProductGroup)) {
            return false;
        }
        ProductGroup other = (ProductGroup) object;
        if (
            (this.id == null && other.id != null) ||
            (this.id != null && !this.id.equals(other.id))
        ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (
            "cr.ac.una.koffeefxws.model.ProductGroup[ productGroupId=" +
            id +
            " ]"
        );
    }
}
