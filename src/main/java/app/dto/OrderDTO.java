package app.dto;

import app.entities.Status;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private int order_id;
    private LocalDateTime orderdate;
    private Status status;
    private double totalPrice;
    private List<OrderlineDTO> orderlines;

        public OrderDTO(int order_id, LocalDateTime orderdate, Status status, double totalPrice, List<OrderlineDTO> orderlines){
            this.order_id = order_id;
            this.orderdate = orderdate;
            this.status = status;
            this.totalPrice = totalPrice;
            this.orderlines = orderlines;
        }
        public int getOrder_id() {
            return order_id;
        }
        public LocalDateTime getOrderdate() {
            return orderdate;
        }
        public Status getStatus() {
            return status;
        }
        public double getTotalPrice() {
            return totalPrice;
        }
        public List<OrderlineDTO> getOrderlines() {
            return orderlines;
        }
}
