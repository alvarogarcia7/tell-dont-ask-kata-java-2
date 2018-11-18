package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderApprovalUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    public void approvedExistingOrder() {
        orderRepository.addOrder(Order.buildFrom(new ArrayList<>(), OrderStatus.CREATED, 1));

        useCase.run(orderApprovalRequest(true));

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus(), is(OrderStatus.APPROVED));
    }

    @Test
    public void rejectedExistingOrder() {
        orderRepository.addOrder(Order.buildFrom(new ArrayList<>(), OrderStatus.CREATED, 1));

        useCase.run(orderApprovalRequest(false));

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus(), is(OrderStatus.REJECTED));
    }

    @Test(expected = RejectedOrderCannotBeApprovedException.class)
    public void cannotApproveRejectedOrder() {
        orderRepository.addOrder(Order.buildFrom(new ArrayList<>(), OrderStatus.REJECTED, 1));

        useCase.run(orderApprovalRequest(true));

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }


    @Test(expected = ApprovedOrderCannotBeRejectedException.class)
    public void cannotRejectApprovedOrder() {
        orderRepository.addOrder(Order.buildFrom(new ArrayList<>(), OrderStatus.APPROVED, 1));

        useCase.run(orderApprovalRequest(false));

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeApproved() {
        orderRepository.addOrder(Order.buildFrom(new ArrayList<>(), OrderStatus.SHIPPED, 1));

        useCase.run(orderApprovalRequest(true));

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeRejected() {
        orderRepository.addOrder(Order.buildFrom(new ArrayList<>(), OrderStatus.SHIPPED, 1));

        useCase.run(orderApprovalRequest(false));

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    private OrderApprovalRequest orderApprovalRequest(boolean approved) {
        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(approved);
        return request;
    }
}
