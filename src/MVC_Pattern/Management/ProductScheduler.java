package MVC_Pattern.Management;

import MVC_Pattern.Sales.Product;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by Netapp on 2017-06-16.
 */
public class ProductScheduler extends TimerTask {
    private int menu;
    private int amount;
    private Product reorderProduct;
    private ArrayList<Product> productList;

    ProductScheduler(int menu, int amount, ArrayList<Product> itemList) {
        this.menu = menu;
        this.amount = amount;
        this.productList = itemList;
    }

    @Override
    public void run() {
        System.out.println("\n*----------주문한 상품이 입고되었습니다.----------*");
        this.productList.get(menu).fill_product(amount);
        try {
            reorderProduct = (Product)this.productList.get(menu).clone();
            reorderProduct.change_amount(amount);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
    public Product getReorderProduct() {
        return reorderProduct;
    }
}
