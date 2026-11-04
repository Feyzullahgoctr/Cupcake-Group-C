
package app.dto;

public class OrderlineDTO {

    private int orderline_id;
    private int bottom_id;
    private int topping_id;
    private int amount;
    private double price;
    private String bottomName;
    private String toppingName;

    public OrderlineDTO(int orderline_id, int bottom_id, int topping_id, int amount, double price, String bottomName, String toppingName) {
        this.orderline_id = orderline_id;
        this.bottom_id = bottom_id;
        this.topping_id = topping_id;
        this.amount = amount;
        this.price = price;
        this.bottomName = bottomName;
        this.toppingName = toppingName;
    }

    public int getOrderline_id() {
        return orderline_id;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public String getBottomName() {
        return bottomName;
    }

    public String getToppingName() {
        return toppingName;
    }

    public int getBottom_id() {
        return bottom_id;
    }

    public int getTopping_id() {
        return topping_id;
    }
}
