package MVC_Pattern.Customer;

import MVC_Pattern.Management.Management;
import MVC_Pattern.Sales.Product;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Netapp on 2017-06-14.
 */
public class Customer {
    private String customerID;
    private int paymentType;
    private ArrayList<Product> itemList;

    public Customer(int paymentType, ArrayList<Product> productList) {
        this.paymentType = paymentType;

        Random rand = new Random();
        itemList = new ArrayList<>();
        ArrayList<Integer> indexList = new ArrayList<>();

        int randProductCount = rand.nextInt(10);

        if(randProductCount == 0)
            randProductCount = 1;

        for(int i = 0; i < randProductCount; i++) {
            int randProductIndex = rand.nextInt(productList.size());
            if(randProductIndex == 0)
                continue;
            if(!indexList.contains(randProductIndex)) {
                try {
                    Product guestOrder = (Product)productList.get(randProductIndex).clone();
                    itemList.add(guestOrder);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                indexList.add(randProductIndex);
            }
        }

        for(int i = 0; i < itemList.size(); i++) {
            int randProductAmount = rand.nextInt(10);
            if(itemList.get(i).getProductAmount() < randProductAmount) {
                randProductAmount = itemList.get(i).getProductAmount() / 2;
            }
            if(randProductAmount == 0)
                randProductAmount = 1;
            itemList.get(i).change_amount(randProductAmount);
        }
    }

    public int getPaymentType() {
        return this.paymentType;
    }

    public ArrayList<Product> getCustomerProductList() {
        return itemList;
    }
}
