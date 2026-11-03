package app.entities;

public class Orderline {
    private int orderLine_id;
    private int order_id;
    private Bottom bottoms;
    private Topping toppings;
    private int amount;
    private double unitPrice;

    public Orderline(int orderLine_id, int order_id, Bottom bottoms, Topping toppings, int amount, double unitPrice) {
        this.orderLine_id = orderLine_id;
        this.order_id = order_id;
        this.bottoms = bottoms;
        this.toppings = toppings;
        this.amount = amount;
        this.unitPrice = unitPrice;
    }

    public int getOrderLine_id() {
        return orderLine_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public Bottom getBottoms() {
        return bottoms;
    }

    public Topping getToppings() {
        return toppings;
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

    public void setBottoms(Bottom bottoms) {
        this.bottoms = bottoms;
    }

    public void setToppings(Topping toppings) {
        this.toppings = toppings;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
