package app.dto;

import app.entities.Status;

import java.time.LocalDateTime;
import java.util.List;

public class AdminOrderDTO {
    private int order_id;
    private String email;
    private LocalDateTime orderDate;
    private Status status;
    private double totalPrice;
    private List<OrderlineDTO> orderlines;

    public AdminOrderDTO(int order_id, String email, LocalDateTime orderDate, Status status, double totalPrice, List<OrderlineDTO> orderlines) {
        this.order_id = order_id;
        this.email = email;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.orderlines = orderlines;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
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

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderlines(List<OrderlineDTO> orderlines) {
        this.orderlines = orderlines;
    }
}
