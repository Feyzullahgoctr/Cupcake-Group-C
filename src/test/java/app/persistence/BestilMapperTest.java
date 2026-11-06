package app.persistence;

import app.dto.OrderlineDTO;
import app.entities.Bottom;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BestilMapperTest extends DatabaseTestBefore {


    @Test
    void getBottoms() throws DatabaseException {
        List<Bottom> bottoms = bestilMapper.getBottoms();

        assertEquals(3, bottoms.size());
        assertEquals("Chocolate", bottoms.get(0).getName());
        assertEquals(5, bottoms.get(0).getPrice());
    }

    @Test
    void getToppings() throws DatabaseException {
        List<Topping> toppings = bestilMapper.getToppings();

        assertEquals(3, toppings.size());
        assertEquals("Blueberry", toppings.get(1).getName());
        assertEquals(5, toppings.get(1).getPrice());
    }

    @Test
    void createOrder_returnsNewId() throws DatabaseException {
        int orderId = bestilMapper.createOrder(2, 0);
        assertEquals(3, orderId);
    }

    @Test
    void createOrderline_addsRow() throws Exception {
        bestilMapper.createOrderline(1, 2, 3, 1, 10);
        List<OrderlineDTO> orderitems = bestilMapper.getOrderlineByOrderid(1);

        assertEquals(2, orderitems.size());
    }

    @Test
    void updateOrderPrice_changesTotal() throws Exception {
        bestilMapper.createOrderline(1, 2, 3, 1, 10);
        bestilMapper.updateOrderPrice(1, 50);
        List<OrderlineDTO> orderitems = bestilMapper.getOrderlineByOrderid(1);

        double total = 0;
        for (OrderlineDTO order : orderitems) {
            if (order.getOrder_id() == 1) {
                total += order.getAmount() * order.getPrice();
            }
        }

        assertEquals(50, total);
    }


    @Test
    void deleteOrderline () throws Exception {
        bestilMapper.deleteOrderlines(2);

        List<OrderlineDTO> orderitems = bestilMapper.getOrderlineByOrderid(2);
        assertEquals(0, orderitems.size());
    }

}
