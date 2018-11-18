package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public Order() {
        this.setCurrency("EUR");
        this.setTotal(new BigDecimal("0"));
        this.setTax(new BigDecimal("0"));
        this.setItems(new ArrayList<>());
    }

    private BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    private void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    private void setItems(List<OrderItem> items) {
        this.items = items;
    }

    private BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                Objects.equals(total, order.total) &&
                Objects.equals(currency, order.currency) &&
                Objects.equals(items, order.items) &&
                Objects.equals(tax, order.tax) &&
                status == order.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(total, currency, items, tax, status, id);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Order{");
        sb.append("total=").append(total);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", items=").append(items);
        sb.append(", tax=").append(tax);
        sb.append(", status=").append(status);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }

    public void addTax(BigDecimal taxAmount) {
        setTax(getTax().add(taxAmount));
    }

    public void addTotal(BigDecimal taxedAmount) {
        setTotal(getTotal().add(taxedAmount));
    }

    public void created() {
        setStatus(OrderStatus.CREATED);
    }

    public void approved() {
        setStatus(OrderStatus.APPROVED);
    }
}
