package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {

        final ArrayList<OrderItem> items = new ArrayList<>();
        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = productCatalog.getByName(itemRequest.getProductName());

            if (product == null) {
                throw new UnknownProductException();
            } else {
                final BigDecimal taxedAmount = product.taxedAmount(itemRequest.getQuantity());
                final BigDecimal taxAmount = product.taxFor(itemRequest.getQuantity());
                final OrderItem orderItem = build(itemRequest, product, taxAmount, taxedAmount);
                items.add(orderItem);

            }
        }
        Order order = Order.buildFrom(items, OrderStatus.CREATED, 0);

        orderRepository.save(order);
    }

    private static OrderItem build(SellItemRequest itemRequest, Product product, BigDecimal taxAmount, BigDecimal taxedAmount) {
        final OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setTax(taxAmount);
        orderItem.setTaxedAmount(taxedAmount);
        return orderItem;
    }

}
