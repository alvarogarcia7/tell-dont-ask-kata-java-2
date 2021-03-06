package it.gabrieletondi.telldontaskkata.useCase.approve_order;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    public OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        order.approve(request).map(it -> {
            orderRepository.save(it);
            return null;
        });
    }
}
