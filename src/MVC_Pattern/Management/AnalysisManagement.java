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

    public void Accounts() {
        int totalPrice = 0;
        for(int i = 1; i < inititemList.size(); i++) {
            SalesRecord(i);
            if(isReordered()) {
                totalPrice += getReorderItemTotalPrice(i, getSoldItemTotalPrice(i));
            }
            else {
                totalPrice += getSoldItemTotalPrice(i);
            }
        }
        System.out.println("\n총 금액: " + totalPrice +
                "\t\tPos 잔액: " + (Management.initialMoney + Management.inboundMoney) +
                "\t\tPos 원금: " + Management.initialMoney);
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
            SalesRecord(Integer.parseInt(productNumber));
            break;
        }
    }

    private void SalesRecord(int productNumber) {
        if(isReordered()) {
            PrintProductSaleInfo(productNumber, getSoldItemAmount(productNumber),
                    getReorderItemTotalPrice(productNumber, getSoldItemTotalPrice(productNumber)));
            System.out.println("\t" + getReorderItemAmount(productNumber));
        }
        else {
            PrintProductSaleInfo(productNumber, getSoldItemAmount(productNumber), getSoldItemTotalPrice(productNumber));
        }
    }

    private void PrintItemList() {
        for(int i = 1; i < inititemList.size(); i++) {
            if(i % 4 == 0)
                System.out.println("");
            System.out.print(i + "." + inititemList.get(i).getProductName() + " ");
        }
        System.out.print("0.취소\n\n");
    }

    private boolean isReordered() {
        if(soldItemList.isEmpty()) {
            return false;
        }
        return true;
    }
    private void PrintProductSaleInfo(int productNumber, int soldItemAmount, int soldTotalPrice) {
        System.out.print(
                "상품이름: " + inititemList.get(productNumber).getProductName() +
                        "\t 판매량: " + soldItemAmount +
                        "\t 판매금액: " + soldTotalPrice);
    }

    private int getSoldItemAmount(int productNumber) {
        return soldItemList.get(productNumber).getProductAmount();
    }

    private int getSoldItemTotalPrice(int productNumber) {
        return getSoldItemAmount(productNumber) * inititemList.get(productNumber).getProductCost();
    }

    private int getReorderItemAmount(int productNumber) {
        return reorderItemList.get(productNumber).getProductAmount();
    }

    private int getReorderItemTotalPrice(int productNumber, int soldTotalPrice) {
        return getReorderItemAmount(productNumber) * reorderItemList.get(productNumber).getProductCost()
                + soldTotalPrice;
    }
}
