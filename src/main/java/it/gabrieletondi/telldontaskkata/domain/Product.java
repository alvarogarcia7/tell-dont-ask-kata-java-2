package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private String name;
    private BigDecimal price;
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Product{");
        sb.append("name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append(", category=").append(category);
        sb.append('}');
        return sb.toString();
    }

    public BigDecimal unitaryTax() {
        return getPrice().divide(valueOf(100)).multiply(getCategory().getTaxPercentage()).setScale(2, HALF_UP);
    }

    public BigDecimal unitaryTaxedAmount(BigDecimal unitaryTax) {
        return getPrice().add(unitaryTax).setScale(2, HALF_UP);
    }
}
