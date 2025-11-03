package cr.ac.una.koffeefxws.model;

import java.io.Serializable;

/**
 * ProductSalesDTO
 *
 * <p>DTO para agregar y presentar datos de ventas de productos en el reporte "Products-Report"
 *
 * <p>Contiene información de productos vendidos entre un rango de fechas, con totales agregados por
 * producto.
 */
public class ProductSalesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName; // Nombre del producto
    private String productCategory; // Categoría/Grupo del producto
    private Integer totalQuantitySold; // Total de unidades vendidas
    private Double avgPrice; // Precio promedio del producto
    private Double totalRevenue; // Ingresos totales (cantidad × precio promedio)

    // ===== CONSTRUCTORES =====

    public ProductSalesDTO() {
        this.totalQuantitySold = 0;
        this.avgPrice = 0.0;
        this.totalRevenue = 0.0;
    }

    public ProductSalesDTO(
            String productName, String productCategory, Integer quantity, Double price) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.totalQuantitySold = quantity != null ? quantity : 0;
        this.avgPrice = price != null ? price : 0.0;
        this.totalRevenue = calculateRevenue();
    }

    // ===== MÉTODOS HELPER =====

    /** Suma cantidad a las unidades ya vendidas */
    public void addQuantity(Integer quantity) {
        if (quantity != null) {
            this.totalQuantitySold =
                    (this.totalQuantitySold != null ? this.totalQuantitySold : 0) + quantity;
            this.totalRevenue = calculateRevenue();
        }
    }

    /** Suma ingresos (cantidad × precio) */
    public void addRevenue(Double revenue) {
        if (revenue != null) {
            this.totalRevenue = (this.totalRevenue != null ? this.totalRevenue : 0.0) + revenue;
        }
    }

    /** Calcula el ingreso total basado en cantidad y precio */
    private Double calculateRevenue() {
        Integer qty = this.totalQuantitySold != null ? this.totalQuantitySold : 0;
        Double price = this.avgPrice != null ? this.avgPrice : 0.0;
        return qty * price;
    }

    // ===== GETTERS Y SETTERS =====

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(Integer totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }

    public Double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    // ===== MÉTODOS ESTÁNDAR =====

    @Override
    public String toString() {
        return "ProductSalesDTO{"
                + "productName='"
                + productName
                + '\''
                + ", productCategory='"
                + productCategory
                + '\''
                + ", totalQuantitySold="
                + totalQuantitySold
                + ", avgPrice="
                + avgPrice
                + ", totalRevenue="
                + totalRevenue
                + '}';
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(productName, productCategory);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductSalesDTO other = (ProductSalesDTO) obj;
        return java.util.Objects.equals(productName, other.productName)
                && java.util.Objects.equals(productCategory, other.productCategory);
    }
}
