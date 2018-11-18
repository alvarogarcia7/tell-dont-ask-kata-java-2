package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.*;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
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
        assertThat(insertedOrder.getStatus(), is(OrderStatus.CREATED));
        assertThat(insertedOrder.getTotal(), is(new BigDecimal("23.20")));
        assertThat(insertedOrder.getTax(), is(new BigDecimal("2.13")));
        assertThat(insertedOrder.getCurrency(), is("EUR"));
        assertThat(insertedOrder.getItems(), hasSize(2));
        assertThat(insertedOrder.getItems().get(0).getProduct().getName(), is("salad"));
        assertThat(insertedOrder.getItems().get(0).getProduct().getPrice(), is(new BigDecimal("3.56")));
        assertThat(insertedOrder.getItems().get(0).getQuantity(), is(2));
        assertThat(insertedOrder.getItems().get(0).getTaxedAmount(), is(new BigDecimal("7.84")));
        assertThat(insertedOrder.getItems().get(0).getTax(), is(new BigDecimal("0.72")));
        assertThat(insertedOrder.getItems().get(1).getProduct().getName(), is("tomato"));
        assertThat(insertedOrder.getItems().get(1).getProduct().getPrice(), is(new BigDecimal("4.65")));
        assertThat(insertedOrder.getItems().get(1).getQuantity(), is(3));
        assertThat(insertedOrder.getItems().get(1).getTaxedAmount(), is(new BigDecimal("15.36")));
        assertThat(insertedOrder.getItems().get(1).getTax(), is(new BigDecimal("1.41")));

        final Order order = new Order();
        order.setItems(new ArrayList<>());
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(new BigDecimal("23.20"));
        order.setTax(new BigDecimal("2.13"));
        order.setId(0);
        order.setCurrency("EUR");
        {
            OrderItem salad = new OrderItem();
            final Product product = new Product();
            product.setName("salad");
            product.setPrice(new BigDecimal("3.56"));
            product.setCategory(new Category(new BigDecimal("10")));
            salad.setTax(new BigDecimal("0.72"));
            salad.setTaxedAmount(new BigDecimal("7.84"));
            salad.setProduct(product);
            salad.setQuantity(2);
            order.getItems().add(salad);
        }
        {
            OrderItem tomato = new OrderItem();
            tomato.setTaxedAmount(new BigDecimal("15.36"));
            tomato.setTax(new BigDecimal("1.41"));
            tomato.setQuantity(3);
            final Product product1 = new Product();
            product1.setName("tomato");
            product1.setPrice(new BigDecimal("4.65"));
            product1.setCategory(new Category(new BigDecimal("10")));
            tomato.setProduct(product1);
            order.getItems().add(tomato);
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
