package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Category {
    private final BigDecimal taxPercentage;

    public Category(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(taxPercentage, category.taxPercentage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taxPercentage);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Category{");
        sb.append("taxPercentage=").append(taxPercentage);
        sb.append('}');
        return sb.toString();
    }
}
