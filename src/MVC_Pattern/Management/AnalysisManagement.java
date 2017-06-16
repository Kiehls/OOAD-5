package MVC_Pattern.Management;

import MVC_Pattern.Sales.Product;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static MVC_Pattern.Management.Management.PrintErrorHandling;
import static MVC_Pattern.Management.Management.isCancel;
import static MVC_Pattern.PosSystem.isCorrectMenu;

/**
 * Created by Leo on 2017-06-14.
 */
public class AnalysisManagement {

    private ArrayList<Product> inititemList;
    private ArrayList<Product> soldItemList;
    private ArrayList<Product> reorderItemList;
    private Scanner keyboard = new Scanner(System.in);

    public AnalysisManagement(Management.DataCapsule capsule) {
        this.inititemList = capsule.getInitItemList();
        this.soldItemList = capsule.getSoldItemList();
        this.reorderItemList = capsule.getOrderItemList();
    }

    public void MiddleAccounts() {

    }

    public void ShowEachProductSaleRecord() {
        PrintItemList();
        while(true) {
            String productNumber = keyboard.nextLine();
            if(!isCorrectMenu(productNumber)) {
                PrintErrorHandling();
            }
            if(isCancel(productNumber)) {
                return;
            }
            System.out.println("\n*----------" + inititemList.get(Integer.parseInt(productNumber)).getProductName()
                    + " 판매기록.----------*");

            if(isReordered()) {
                PrintProductSaleInfo(productNumber, getSoldItemAmount(productNumber), getReorderItemTotalPrice(productNumber, getSoldItemTotalPrice(productNumber)));
                System.out.println("\t" + getReorderItemAmount(productNumber));
            }
            else {
                PrintProductSaleInfo(productNumber, getSoldItemAmount(productNumber), getSoldItemTotalPrice(productNumber));
            }
        }
    }

    private void PrintItemList() {
        for(int i = 0; i < inititemList.size(); i++) {
            if(i % 4 == 0)
                System.out.println("");
            System.out.print((i + 1) + "." + inititemList.get(i).getProductName() + " ");
        }
        System.out.print("0.취소\n\n");
    }

    private boolean isReordered() {
        if(soldItemList.isEmpty()) {
            return false;
        }
        return true;
    }
    private void PrintProductSaleInfo(String productNumber, int soldItemAmount, int soldTotalPrice) {
        System.out.print(
                "상품이름: " + inititemList.get(Integer.parseInt(productNumber)).getProductName() +
                        "\t 판매량: " + soldItemAmount +
                        "\t 판매금액: " + soldTotalPrice);
    }

    private int getSoldItemAmount(String productNumber) {
        return inititemList.get(Integer.parseInt(productNumber)).getProductAmount()
                - soldItemList.get(Integer.parseInt(productNumber)).getProductAmount();
    }

    private int getSoldItemTotalPrice(String productNumber) {
        return getSoldItemAmount(productNumber) * inititemList.get(Integer.parseInt(productNumber)).getProductCost();
    }

    private int getReorderItemAmount(String productNumber) {
        return reorderItemList.get(Integer.parseInt(productNumber)).getProductAmount();
    }

    private int getReorderItemTotalPrice(String productNumber, int soldTotalPrice) {
        return getReorderItemAmount(productNumber) * reorderItemList.get(Integer.parseInt(productNumber)).getProductCost()
                + soldTotalPrice;
    }
}
