package MVC_Pattern.Sales;

import MVC_Pattern.Customer.Customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static MVC_Pattern.PosSystem.receiptNumber;

/**
 * Created by Netapp on 2017-06-14.
 */
public class Payment {

    private String dateTime;
    private int totalAmount = 0;
    private Customer customer;
    private int paymentResult;
    private Scanner keyboard = new Scanner(System.in);

    public Payment(Customer customer) {
        this.customer = customer;
        paymentResult = Sale();
    }

    private int Sale() {
        ScanBarcode(this.customer.getCustomerProductList());
        if(CancelPayment(Integer.parseInt(keyboard.nextLine()))) {
            return -1;
        }
        System.out.println("\n*----------결제가 완료되었습니다.----------*");
        if(isPaymentCash(this.customer.getPaymentType())) {
            PrintReceiptCash(this.customer);
        }
        else {
            PrintReceiptCredit(this.customer);
        }
        return 1;
    }
    private void ScanBarcode(ArrayList<Product> itemList) {
        System.out.println("\n*-----------Product Item List------------*");
        for(int i = 0; i < itemList.size(); i++) {
            System.out.println((i + 1) + "." + itemList.get(i).getProductName() + " " +
                    itemList.get(i).getProductAmount() + "개"
            );
            totalAmount += itemList.get(i).getProductAmount() * itemList.get(i).getProductCost();
        }
        System.out.println("*---------------------------------*");
        System.out.println("총 금액: " + totalAmount + "\n");
        System.out.println("결제를 취소하시겠습니까? (0번)");
    }

    public int IncomeCash() {
        if(isPaymentCash(customer.getPaymentType())) {
            return this.totalAmount;
        }
        return 0;
    }

    public int getPaymentResult() {
        return this.paymentResult;
    }

    private boolean CancelPayment(int cancel) {
        if(cancel == 0) return true;
        else return false;
    }

    private void PrintReceiptCredit(Customer customer) {
        String baseReciept = PrintReceiptBase(customer);
        baseReciept += "      **신용카드 결제**    \n";
        baseReciept += "카드사명:          해외VISA카드\n";
        baseReciept += "승인금액:\t\t\t  " + totalAmount + "\n";
        baseReciept += "발행일시: " + dateTime + "\n";
        baseReciept += "------------------------------\n";
        System.out.println(baseReciept);
    }
    private void PrintReceiptCash(Customer customer) {
        String baseReciept = PrintReceiptBase(customer);
        baseReciept += "현금 결제:\t\t\t  " + totalAmount + "\n";
        baseReciept += "------------------------------\n";
        baseReciept += "발행일시: " + dateTime + "\n";
        baseReciept += "------------------------------\n";
        System.out.println(baseReciept);
    }
    private String PrintReceiptBase(Customer customer) {
        int totalSum = 0;
        String recieptSTR = "";
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = date.format(new Date(System.currentTimeMillis()));

        recieptSTR += "\n          영수증          \n";
        recieptSTR += "영수증 번호: " + receiptNumber + "\n";
        recieptSTR += "------------------------------\n";
        recieptSTR += "수량  품명             금액  \n";
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < customer.getCustomerProductList().size(); i++) {
            sb.append(customer.getCustomerProductList().get(i).getProductAmount() + "\t");

            if(isTooLongname(customer.getCustomerProductList().get(i).getProductName().length())) {
                sb.append(customer.getCustomerProductList().get(i).getProductName() + "\t\t\t");
            }else {
                sb.append(customer.getCustomerProductList().get(i).getProductName() + "\t\t");
            }

            sb.append(customer.getCustomerProductList().get(i).getProductCost() * customer.getCustomerProductList().get(i).getProductAmount() + "\n");
        }
        recieptSTR += sb.toString();
        recieptSTR += "------------------------------\n";
        recieptSTR += "주문 합계:\t\t\t  " + (totalSum * 0.9) + "\n";
        recieptSTR += "부 가 세: \t\t\t  " + (totalSum * 0.1) + "\n";
        recieptSTR += "------------------------------\n";
        recieptSTR += "합계 금액:\t\t\t  " + totalSum + "\n";
        recieptSTR += "------------------------------\n";

        return recieptSTR;
    }

    private boolean isTooLongname(int length) {
        if(length < 8) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isPaymentCash(int paymentType) {
        if(paymentType == 1) {
            return true;
        }
        return false;
    }
}
