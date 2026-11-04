package app.entities;

public class Orderline {
    private int orderLine_id;
    private int order_id;
    private int bottom_id;
    private int topping_id;
    private int amount;
    private double unitPrice;

    public Orderline(int orderLine_id, int order_id, int bottom_id, int topping_id, int amount, double unitPrice) {
        this.orderLine_id = orderLine_id;
        this.order_id = order_id;
        this.bottom_id = bottom_id;
        this.topping_id = topping_id;
        this.amount = amount;
        this.unitPrice = unitPrice;
    }

    public int getOrderLine_id() {
        return orderLine_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getAmount() {
        return amount;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setOrderLine_id(int orderLine_id) {
        this.orderLine_id = orderLine_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getBottom_id() {
        return bottom_id;
    }

    public int getTopping_id() {
        return topping_id;
    }
}
