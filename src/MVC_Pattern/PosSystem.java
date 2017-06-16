package MVC_Pattern;

import MVC_Pattern.Management.Management;

import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Created by Netapp on 2017-06-14.
 */
public class PosSystem {
    private Scanner keyboard;
    public Management management;
    public static int receiptNumber = 0;

    PosSystem() {
        keyboard = new Scanner(System.in);
    }

    public void InitiateSystem() {
        System.out.println("Initializing Pos System");
        System.out.println("Insert PoS Initialize Cash Amount");
        management = new Management(Integer.parseInt(keyboard.nextLine()));
        ShowPosMenu();
    }

    private void ShowPosMenu() {
        while(true) {
            PrintMenu();
            String menu = keyboard.nextLine();

            if(!isCorrectMenu(menu)) {
                management.PrintErrorHandling();
                continue;
            }

            if(management.isCancel(menu)) {
                management.SaveProductList();
                break;
            }
            switch(Integer.parseInt(menu)) {
                case 1:
                    management.CustomerSale();
                    break;
                case 2:
                    management.ReOrderProduct();
                    break;
                case 3:
                    management.RegisterItem();
                    break;
                case 4:
                    management.PrintItemList();
                    break;
                case 5:
                    management.ShowCurrentMoney();
                    break;
                case 6:
                    management.ShowSystem();
                    break;
                default:
                    break;
            }
        }
    }

    public void PrintMenu() {
        System.out.println("\n*----------Select Menu----------*");
        System.out.println("1.판매 2.발주 3.등록 4.상품 목록 5.잔여 현금량 6.시스템 0.프로그램 종료");
    }

    public static boolean isCorrectMenu(String menu) {
        if(isNumeric(menu)) {
            return true;
        }
        return false;
    }
    public static boolean isNumeric(String str) {
        return str.matches("^[0-9]+$");
    }
}
