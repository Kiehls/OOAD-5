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
        System.out.println("결제를 취소하시겠습니까? Yes(0번) or No(1번)");
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
        String baseReceipt = PrintReceiptBase(customer);
        baseReceipt += "      **신용카드 결제**    \n";
        baseReceipt += "카드사명:          해외VISA카드\n";
        baseReceipt += "승인금액:\t\t\t  " + totalAmount + "\n";
        baseReceipt += "발행일시: " + dateTime + "\n";
        baseReceipt += "------------------------------\n";
        System.out.println(baseReceipt);
    }
    private void PrintReceiptCash(Customer customer) {
        String baseReceipt = PrintReceiptBase(customer);
        baseReceipt += "현금 결제:\t\t\t  " + totalAmount + "\n";
        baseReceipt += "------------------------------\n";
        baseReceipt += "발행일시: " + dateTime + "\n";
        baseReceipt += "------------------------------\n";
        System.out.println(baseReceipt);
    }
    private String PrintReceiptBase(Customer customer) {
        String receiptSTR = "";
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = date.format(new Date(System.currentTimeMillis()));

        receiptSTR += "\n          영수증          \n";
        receiptSTR += "영수증 번호: " + (receiptNumber++) + "\n";
        receiptSTR += "------------------------------\n";
        receiptSTR += "수량  품명             금액  \n";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < customer.getCustomerProductList().size(); i++) {
            sb.append(customer.getCustomerProductList().get(i).getProductAmount() + "\t");

            if(isTooLongname(customer.getCustomerProductList().get(i).getProductName().length())) {
                sb.append(customer.getCustomerProductList().get(i).getProductName() + "\t\t\t");
            }else {
                sb.append(customer.getCustomerProductList().get(i).getProductName() + "\t\t");
            }

            sb.append(customer.getCustomerProductList().get(i).getProductCost() * customer.getCustomerProductList().get(i).getProductAmount() + "\n");
        }
        receiptSTR += sb.toString();
        receiptSTR += "------------------------------\n";
        receiptSTR += "주문 합계:\t\t\t  " + (totalAmount * 0.9) + "\n";
        receiptSTR += "부 가 세: \t\t\t  " + (totalAmount * 0.1) + "\n";
        receiptSTR += "------------------------------\n";
        receiptSTR += "합계 금액:\t\t\t  " + totalAmount + "\n";
        receiptSTR += "------------------------------\n";

        return receiptSTR;
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
