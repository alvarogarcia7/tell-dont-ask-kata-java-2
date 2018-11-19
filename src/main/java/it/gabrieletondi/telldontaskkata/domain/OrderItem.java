package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {
    private final Product product;
    private final int quantity;
    private final BigDecimal taxedAmount;
    private final BigDecimal tax;

    private OrderItem(Product product, int quantity, BigDecimal taxedAmount, BigDecimal tax) {
        this.product = product;
        this.quantity = quantity;
        this.taxedAmount = taxedAmount;
        this.tax = tax;
    }

    public static OrderItem buildFrom(Product product, int quantity) {
        return new OrderItem(product, quantity, product.taxedAmount(quantity), product.tax(quantity));
    }

    public BigDecimal totalPrice() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }

    public BigDecimal tax() {
        return tax;
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
