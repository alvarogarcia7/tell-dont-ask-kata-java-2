package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class Order {
    private final BigDecimal total;
    private final String currency;
    private final List<OrderItem> items;
    private final BigDecimal tax;
    private final OrderStatus status;
    private final int id;

    private Order(int id, List<OrderItem> items, OrderStatus status, BigDecimal total, BigDecimal tax) {
        this.total = total;
        this.currency = "EUR";
        this.items = items;
        this.tax = tax;
        this.status = status;
        this.id = id;
    }

    public static Order buildFrom(List<OrderItem> items, OrderStatus status, int id) {
        final BigDecimal totalTax = items.stream().map(OrderItem::tax).reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal totalAmount = items.stream().map(it -> {
            return it.product().getPrice().multiply(new BigDecimal(it.quantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Order(id, items, status, totalAmount, totalTax);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
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

    public Optional<Order> ship() {
        if (status.equals(CREATED) || status.equals(REJECTED)) {
            throw new OrderCannotBeShippedException();
        }

        if (status.equals(SHIPPED)) {
            throw new OrderCannotBeShippedTwiceException();
        }

        return Optional.of(buildFrom(this.items, OrderStatus.SHIPPED, this.id));
    }

    public Optional<Order> approve(OrderApprovalRequest request) {
        if (status == OrderStatus.SHIPPED) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (request.isApproved() && status == OrderStatus.REJECTED) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!request.isApproved() && status == OrderStatus.APPROVED) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        return Optional.of(request.isApproved() ? buildFrom(this.items, OrderStatus.APPROVED, id) : buildFrom(items, OrderStatus.REJECTED, id));
    }
}
