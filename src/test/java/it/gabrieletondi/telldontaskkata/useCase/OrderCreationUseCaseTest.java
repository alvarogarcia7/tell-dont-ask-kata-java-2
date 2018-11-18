package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.*;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class OrderCreationUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    final static Product salad = new Product();
    final static Product tomato = new Product();

    static {
        salad.setName("salad");
        salad.setPrice(new BigDecimal("3.56"));
        salad.setCategory(new Category(new BigDecimal("10")));
        tomato.setName("tomato");
        tomato.setPrice(new BigDecimal("4.65"));
        tomato.setCategory(new Category(new BigDecimal("10")));
    }

    private final ProductCatalog productCatalog = new InMemoryProductCatalog(Arrays.asList(salad, tomato));
    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

    @Test
    public void sellMultipleItems() throws Exception {
        SellItemRequest saladRequest = new SellItemRequest();
        saladRequest.setProductName("salad");
        saladRequest.setQuantity(2);

        SellItemRequest tomatoRequest = new SellItemRequest();
        tomatoRequest.setProductName("tomato");
        tomatoRequest.setQuantity(3);

        final SellItemsRequest request = new SellItemsRequest();
        request.setRequests(new ArrayList<>());
        request.getRequests().add(saladRequest);
        request.getRequests().add(tomatoRequest);

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();

        final Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(new BigDecimal("23.20"));
        order.setTax(new BigDecimal("2.13"));
        order.setId(0);
        {
            OrderItem saladOrder = new OrderItem();
            final Product product = OrderCreationUseCaseTest.salad;
            saladOrder.setTax(new BigDecimal("0.72"));
            saladOrder.setTaxedAmount(new BigDecimal("7.84"));
            saladOrder.setProduct(product);
            saladOrder.setQuantity(2);
            order.getItems().add(saladOrder);
        }
        {
            OrderItem tomatoOrder = new OrderItem();
            tomatoOrder.setTaxedAmount(new BigDecimal("15.36"));
            tomatoOrder.setTax(new BigDecimal("1.41"));
            tomatoOrder.setQuantity(3);
            final Product product1 = OrderCreationUseCaseTest.tomato;
            tomatoOrder.setProduct(product1);
            order.getItems().add(tomatoOrder);
        }

        assertThat(insertedOrder, equalTo(order));

    }

    @Test(expected = UnknownProductException.class)
    public void unknownProduct() throws Exception {
        SellItemsRequest request = new SellItemsRequest();
        request.setRequests(new ArrayList<>());
        SellItemRequest unknownProductRequest = new SellItemRequest();
        unknownProductRequest.setProductName("unknown product");
        request.getRequests().add(unknownProductRequest);

        useCase.run(request);
    }
}
