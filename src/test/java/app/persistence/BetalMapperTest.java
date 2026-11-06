package app.persistence;

import app.dto.AdminOrderDTO;
import app.dto.OrderlineDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BetalMapperTest extends DatabaseTestBefore {

    @Test
    void updateOrderStatus () throws Exception {
        betalMapper.updateOrderStatus(1,"Paid");
        List<AdminOrderDTO> orders = adminMapper.getAllOrders();

        for (AdminOrderDTO order : orders) {
            if (order.getOrder_id() == 1) {
                assertEquals("Paid", String.valueOf(order.getStatus()));
            }
        }

    }


}
