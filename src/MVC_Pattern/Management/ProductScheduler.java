package MVC_Pattern.Management;

import MVC_Pattern.Sales.Product;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by Netapp on 2017-06-16.
 */
public class ProductScheduler extends TimerTask {
    private int number;
    private int amount;
    private Product reorderProduct;
    private ArrayList<Product> productList;
    private ArrayList<Product> reorderProductList;

    ProductScheduler(Management.AlertCapsule capsule) {
        this.number = capsule.getProductNumber();
        this.amount = capsule.getProductAmount();
        this.productList = new ArrayList<>();
        this.reorderProductList = new ArrayList<>();
        this.productList = capsule.getInitItemList();
        this.reorderProductList = capsule.getReorderItemList();

    }

    @Override
    public void run() {
        System.out.println("\n*----------주문한 상품이 입고되었습니다.----------*");
        this.productList.get(number).fill_product(amount);
        try {
            reorderProduct = (Product)this.productList.get(number).clone();
            reorderProduct.change_amount(amount);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.reorderProductList.get(number).change_amount(this.reorderProduct.getProductAmount());
    }
    public Product getReorderProduct() {
        return reorderProduct;
    }
}
