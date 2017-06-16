package MVC_Pattern.Sales;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Netapp on 2017-06-14.
 */
public class Product implements Cloneable {

    private int product_amount;
    private int product_cost;
    private String product_name;
    private String product_date;

    public Product() {
        this.product_amount = 0;
        this.product_cost = 0;
        this.product_name = null;
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.product_date = date.format(new Date(System.currentTimeMillis()));
    }

    public Product(String name, int cost, int amount) {
        this.product_amount = amount;
        this.product_cost = cost;
        this.product_name = name;
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.product_date = date.format(new Date(System.currentTimeMillis()));

        System.out.println("상품이 등록되었습니다. \n" +
                "Name: " + this.product_name + ", " +
                "Cost: " + this.product_cost + ", " +
                "Amount: " + this.product_amount);
    }

    public Product(String name, int cost, int amount, String date) {
        this.product_amount = amount;
        this.product_cost = cost;
        this.product_name = name;
        this.product_date = date;
    }

    public Object clone() throws CloneNotSupportedException {
        Product product = (Product) super.clone();
        return product;
    }

    public void fill_product(int amount) {
        this.product_amount += amount;
    }

    public void change_amount(int amount) {
        this.product_amount = amount;
    }

    public int getProductAmount() {
        return this.product_amount;
    }

    public int getProductCost() {
        return this.product_cost;
    }

    public String getProductName() {
        return this.product_name;
    }

    public String RegisterProductData() {

        String str = this.product_name + " " +
                this.product_cost + " " +
                this.product_amount + " " +
                this.product_date;

        return str;
    }

    public String CurrentProductData() {

        String str = "상품명: " + this.product_name + " " +
                "상품가격: " + this.product_cost + " " +
                "재고량: " + this.product_amount;

        return str;
    }
}
