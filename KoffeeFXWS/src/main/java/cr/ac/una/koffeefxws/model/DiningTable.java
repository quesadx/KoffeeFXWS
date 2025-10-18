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
@Table(name = "DINING_TABLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiningTable.findAll", query = "SELECT d FROM DiningTable d"),
    @NamedQuery(name = "DiningTable.findById", query = "SELECT d FROM DiningTable d WHERE d.id = :id"),
    @NamedQuery(name = "DiningTable.findByName", query = "SELECT d FROM DiningTable d WHERE d.name = :name"),
    @NamedQuery(name = "DiningTable.findByImageUrl", query = "SELECT d FROM DiningTable d WHERE d.imageUrl = :imageUrl"),
    @NamedQuery(name = "DiningTable.findByXPos", query = "SELECT d FROM DiningTable d WHERE d.xPos = :xPos"),
    @NamedQuery(name = "DiningTable.findByYPos", query = "SELECT d FROM DiningTable d WHERE d.yPos = :yPos"),
    @NamedQuery(name = "DiningTable.findByWidth", query = "SELECT d FROM DiningTable d WHERE d.width = :width"),
    @NamedQuery(name = "DiningTable.findByHeight", query = "SELECT d FROM DiningTable d WHERE d.height = :height"),
    @NamedQuery(name = "DiningTable.findByStatus", query = "SELECT d FROM DiningTable d WHERE d.status = :status")})
public class DiningTable implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DINING_TABLE_ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;
    @Size(max = 1000)
    @Column(name = "IMAGE_URL")
    private String imageUrl;
    @Column(name = "X_POS")
    private Integer xPos;
    @Column(name = "Y_POS")
    private Integer yPos;
    @Column(name = "WIDTH")
    private Integer width;
    @Column(name = "HEIGHT")
    private Integer height;
    @Size(max = 20)
    @Column(name = "STATUS")
    private String status;
    @JoinColumn(name = "DINING_AREA_ID", referencedColumnName = "DINING_AREA_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DiningArea diningAreaId;
    @OneToMany(mappedBy = "diningTableId", fetch = FetchType.LAZY)
    private List<CustomerOrder> customerOrderList;

    public DiningTable() {
    }

    public DiningTable(Long diningTableId) {
        this.id = diningTableId;
    }

    public DiningTable(Long diningTableId, String name) {
        this.id = diningTableId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long diningTableId) {
        this.id = diningTableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getXPos() {
        return xPos;
    }

    public void setXPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getYPos() {
        return yPos;
    }

    public void setYPos(Integer yPos) {
        this.yPos = yPos;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DiningArea getDiningAreaId() {
        return diningAreaId;
    }

    public void setDiningAreaId(DiningArea diningAreaId) {
        this.diningAreaId = diningAreaId;
    }

    @XmlTransient
    public List<CustomerOrder> getCustomerOrderList() {
        return customerOrderList;
    }

    public void setCustomerOrderList(List<CustomerOrder> customerOrderList) {
        this.customerOrderList = customerOrderList;
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
        if (!(object instanceof DiningTable)) {
            return false;
        }
        DiningTable other = (DiningTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.koffeefxws.model.DiningTable[ diningTableId=" + id + " ]";
    }
    
}
