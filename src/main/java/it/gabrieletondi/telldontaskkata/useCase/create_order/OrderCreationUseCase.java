package it.gabrieletondi.telldontaskkata.useCase.create_order;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.UnknownProductException;
import it.gabrieletondi.telldontaskkata.useCase.sell_item.SellItemRequest;
import it.gabrieletondi.telldontaskkata.useCase.sell_item.SellItemsRequest;

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
                items.add(OrderItem.buildFrom(product, itemRequest.getQuantity()));

            }
        }
        Order order = Order.buildFrom(items, OrderStatus.CREATED, 0);

        orderRepository.save(order);
    }

}
