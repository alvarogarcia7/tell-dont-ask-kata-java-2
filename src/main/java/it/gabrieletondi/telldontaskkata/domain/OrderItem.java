package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTaxedAmount(BigDecimal taxedAmount) {
        this.taxedAmount = taxedAmount;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                Objects.equals(product, orderItem.product) &&
                Objects.equals(taxedAmount, orderItem.taxedAmount) &&
                Objects.equals(tax, orderItem.tax);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderItem{");
        sb.append("product=").append(product);
        sb.append(", quantity=").append(quantity);
        sb.append(", taxedAmount=").append(taxedAmount);
        sb.append(", tax=").append(tax);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {

        return Objects.hash(product, quantity, taxedAmount, tax);
    }
}
