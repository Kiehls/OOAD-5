package MVC_Pattern.Management;

import MVC_Pattern.Customer.Customer;
import MVC_Pattern.Sales.Payment;
import MVC_Pattern.Sales.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static MVC_Pattern.PosSystem.isCorrectMenu;
import static MVC_Pattern.PosSystem.isNumeric;

/**
 * Created by Netapp on 2017-06-14.
 */
public class Management {
    public static int initialMoney;
    public static int inboundMoney;
    private ArrayList<Product> initItemList;
    private ArrayList<Product> soldItemList;
    private ArrayList<Product> orderItemList;
    private Scanner keyboard;

    public Management(int initialMoney) {
        this.initialMoney = initialMoney;
        this.keyboard = new Scanner(System.in);
        this.initItemList = new ArrayList<>();
        this.soldItemList = new ArrayList<>();
        this.orderItemList = new ArrayList<>();
        LoadProductList();
        SetItemListZero();
    }

    private void CalculateCustomerItem(Customer customer) {
        Payment customerPay = new Payment(customer);
        inboundMoney += customerPay.IncomeCash();
        if(isPaymentSuccess(customerPay.getPaymentResult())) {
            SoldItemManage(customer);
        }
    }

    public void RegisterItem() {
        Product product = MakeProduct();
        getItemList().add(product);
        SetItemListZero();
    }

    public void ReOrderProduct() {
        System.out.println("\n*----------재주문할 물품을 선택해주세요.----------*");
        PrintItemList();
        String menuSelect = GetMenu();
        if(isCancel(menuSelect)) {
            return;
        }
        System.out.println("\n*----------재주문할 물품의 수량을 입력해주세요.----------*");
        Timer timer = new Timer();
        AlertCapsule capsule = new AlertCapsule(Integer.parseInt(menuSelect), Integer.parseInt(keyboard.nextLine()), getItemList(), getOrderItemList());
        ProductScheduler scheduler = new ProductScheduler(capsule);
        timer.schedule(scheduler, 1000);
    }

    private void AlertLowAmountProduct() {
        System.out.println("\n*----------알람을 받을 물품을 선택해주세요.----------*");
        PrintItemList();
        String productNumber = keyboard.nextLine();
        if(isCorrectProductNumber(Integer.parseInt(productNumber))) {
            System.out.println("상품의 하한을 설정해주세요.");
            String productAmount = keyboard.nextLine();
            if(isNumeric(productAmount)) {
                Alert(Integer.parseInt(productAmount), Integer.parseInt(productNumber));
            }
            else {
                AmountErrorHandling();
            }
        }
        else {
            AlertErrorHandling();
        }
    }

    public void CustomerSale() {
        Random randPaymentType = new Random();
        Customer customer = new Customer(randPaymentType.nextInt(1), getItemList());
        CalculateCustomerItem(customer);
    }

    private Product MakeProduct() {
        System.out.println("등록할 상품의 상품이름, 가격, 수량을 입력해주세요.");
        String productInfo = keyboard.nextLine();
        Product product = new Product(
                productInfo.split(" ")[0],
                Integer.parseInt(productInfo.split(" ")[1]),
                Integer.parseInt(productInfo.split(" ")[2]));
        return product;
    }

    public ArrayList<Product> getItemList() {
        return initItemList;
    }

    private ArrayList<Product> getSoldItemList() {
        return this.soldItemList;
    }

    private ArrayList<Product> getOrderItemList() {
        return this.orderItemList;
    }

    public void PrintItemList() {
        for(int i = 1; i < getItemList().size(); i++) {
            if(i % 4 == 0)
                System.out.println("");
            System.out.print(i + "." + getItemList().get(i).getProductName() + " ");
        }
        System.out.print("0.취소\n\n");
    }

    private void ShowSystemMenu() {
        System.out.println("\n*----------Select Menu----------*");
        System.out.println("1.중간 결산 2.물품별 판매기록 3.재고알람 설정 0.뒤로가기");
    }

    public void ShowCurrentMoney() {
        System.out.println("Current Cash amout: " + (inboundMoney + initialMoney));
    }

    public static void PrintErrorHandling() {
        System.out.println("올바른 메뉴 선택이 아닙니다. 다시 선택해주세요.");
    }

    private static void AlertErrorHandling() {
        System.out.println("올바른 제품번호 선택이 아닙니다. 다시 선택해주세요.");
    }

    private static void AmountErrorHandling() {
        System.out.println("올바른 제품수량 입력이 아닙니다. 다시 선택해주세요.");
    }

    public void ShowSystem() {
        while(true) {
            ShowSystemMenu();
            String select = keyboard.nextLine();

            DataCapsule capsule = new DataCapsule(getItemList(), getSoldItemList(), getOrderItemList());
            AnalysisManagement statisticManagement = new AnalysisManagement(capsule);

            if(!isCorrectMenu(select)) {
                PrintErrorHandling();
            }

            if(isCancel(select)) {
                break;
            }
            switch(Integer.parseInt(select)) {
                case 1:
                    statisticManagement.Accounts();
                    break;
                case 2:
                    statisticManagement.ShowEachProductSaleRecord();
                    break;
                case 3:
                    AlertLowAmountProduct();
                    break;
            }
        }
    }

    public void SaveProductList() {
        FileOutputStream output;
        try {
            output = new FileOutputStream("ProductFile");

            for (int i = 1; i < initItemList.size(); i++) {
                String str = i + ". " + initItemList.get(i).RegisterProductData() + "\n";
                output.write(str.getBytes());
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n*----------Today's total closing account----------*");
        DataCapsule capsule = new DataCapsule(getItemList(), getSoldItemList(), getOrderItemList());
        AnalysisManagement statisticManagement = new AnalysisManagement(capsule);
        statisticManagement.Accounts();
    }

    private void LoadProductList() {
        Product tempProduct = new Product();
        initItemList.add(tempProduct);

        try {
            BufferedReader in = new BufferedReader(new FileReader("ProductFile"));
            String str;
            while ((str = in.readLine()) != null) {
                Product product = new Product(
                        str.split(" ")[1],
                        Integer.parseInt(str.split(" ")[2]),
                        Integer.parseInt(str.split(" ")[3]),
                        str.split(" ")[4] + " " + str.split(" ")[5]);
                initItemList.add(product);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Load Database System Complete");
    }

    private void Alert(int productAmount, int productNumber) {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(getItemList().get(productNumber).getProductAmount() <= productAmount) {
                    System.out.println("*----------Notification---------------------*");
                    System.out.println("현재 " + getItemList().get(productNumber).getProductName() +
                            " 상품의 재고량이 한계보다 낮습니다."
                    );
                    System.out.println("*-------------------------------------------*");
                }
                else {
                    exec.shutdown();
                }
            }
        },0, 7, TimeUnit.SECONDS);
    }

    private String GetMenu() {
        String menu;
        while(true) {
             menu = keyboard.nextLine();
            if(!isCorrectMenu(menu)) {
                PrintErrorHandling();
            }
            else{
                break;
            }
        }
        return menu;
    }

    public static boolean isCancel(String menu) {
        if(Integer.parseInt(menu) == 0) {
            return true;
        }
        return false;
    }

    private boolean isPaymentSuccess(int result) {
        if(result == 1) return true;
        else return false;
    }

    private boolean isCorrectProductNumber(int productNumber) {
        if(0 < productNumber && productNumber <= getItemList().size()) {
            return true;
        }
        return false;
    }

    private void SoldItemManage(Customer customer) {
        for(int i = 0; i < customer.getCustomerProductList().size(); i++) {
            for(int j = 0; j < getSoldItemList().size(); j++) {
                if (customer.getCustomerProductList().get(i).getProductName().equals(getSoldItemList().get(j).getProductName())) {
                    getSoldItemList().get(j).change_amount(customer.getCustomerProductList().get(i).getProductAmount());
                    break;
                }
            }
        }
    }

    private void SetItemListZero() {
        this.soldItemList = ArrayCopy(initItemList);
        this.orderItemList = ArrayCopy(initItemList);
    }

    public static ArrayList<Product> ArrayCopy(ArrayList<Product> list) {
        ArrayList<Product> temp = new ArrayList<>();
        for (Product list_item : list) {
            try {
                temp.add((Product) list_item.clone());
                temp.get(temp.size()-1).change_amount(0);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public class DataCapsule {
        private ArrayList<Product> initItemList;
        private ArrayList<Product> soldItemList;
        private ArrayList<Product> orderItemList;

        DataCapsule(ArrayList<Product> initItemList, ArrayList<Product> soldItemList,
                    ArrayList<Product> orderItemList) {
            this.initItemList = initItemList;
            this.soldItemList = soldItemList;
            this.orderItemList = orderItemList;
        }

        public ArrayList<Product> getInitItemList() {
            return this.initItemList;
        }
        public ArrayList<Product> getSoldItemList() {
            return this.soldItemList;
        }
        public ArrayList<Product> getOrderItemList() {
            return this.orderItemList;
        }
    }

    public class AlertCapsule {
        private int productNumber;
        private int productAmount;
        private ArrayList<Product> initItemList;
        private ArrayList<Product> reorderItemList;

        AlertCapsule(int number, int amount, ArrayList<Product> initItemList, ArrayList<Product> reorderItemList) {
            this.productAmount = amount;
            this.productNumber = number;
            this.initItemList = new ArrayList<>();
            this.reorderItemList = new ArrayList<>();
            this.initItemList = initItemList;
            this.reorderItemList = reorderItemList;
        }

        public ArrayList<Product> getInitItemList() {
            return this.initItemList;
        }

        public ArrayList<Product> getReorderItemList() {
            return this.reorderItemList;
        }

        public int getProductNumber() {
            return productNumber;
        }

        public int getProductAmount() {
            return productAmount;
        }
    }
}
