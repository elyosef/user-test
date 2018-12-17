package com.company;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    // the accounts that can be loaded in this program
    private static NewUser newUser=null;
    private static Market newMarket=null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDirection userDirection = new UserDirection();

//variables for creating all obj
        int isSignup = 0;
        int count = 0;
        String input;

//this variables is for user
        String userName;
        String family;
        int Id;
        String password;

//this variables is for produkt
        String name;
        double price;
        int amount;

//this variables is for market
        String marketName;
        int open;
        int close;

        //maine singe up / login
        switch (userDirection.login()) {
            case 1://user sign up
                do {
                    //this is for second try if user name already exist
                    if (count != 0) {
                        userDirection.userNameExistInMap();
                    }
                    // getting information fot creating a new user
                    userName = userDirection.newUesrName();
                    family = userDirection.newUserFamily();
                    Id = userDirection.Id();
                    password = userDirection.newUesrPssWord();

                    count = 1;

                    //puting all information in the new user for creation
                } while (!Server.mainUserConnection(userName, family, Id, password, false));
                count = 0;
                newUser = new NewUser(userName, family, Id, password);
                break;

            case 2://user login
                do {
                    //this is for second try if user name already exist
                    if (count != 0) {
                        userDirection.userNameExistInMap();
                    }
                    // getting information fot creating a new user
                    userName = userDirection.newUesrName();
                    password = userDirection.newUesrPssWord();

                    count = 1;
                    //putting all information  user need for login
                } while (!Server.mainUserConnection(userName, password));
                count = 0;
                newUser = new NewUser(userName, password);
                break;

            case 3://market sign up
                do {
                    //this is for second try if market name already exist
                    if (count != 0) {
                        System.out.println("Please type a different market name ");
                    }
                    // getting information fot creating a new user
                    marketName = userDirection.marketName();
                    family = userDirection.newUserFamily();
                    Id = userDirection.Id();
                    password = userDirection.newUesrPssWord();
                    open = userDirection.open();
                    close = userDirection.close();

                    count = 1;

                    //putting all information  market need for creation
                } while (!Server.mainMarketConnection(marketName, family, Id, password, false, open, close));
                count = 0;
                newMarket = new Market(marketName, family, Id, password, open, close);
                isSignup = 1;
                break;

            case 4://market sign up
                do {
                    //this is for second try if user name already exist
                    if (count != 0) {
                        System.out.println("Please type a different market name ");
                    }
                    // getting information fot creating a new market
                    marketName = userDirection.marketName();
                    password= userDirection.newUesrPssWord();

                    count = 1;
                    ///putting all information market needs for login
                } while (!Server.mainMarketConnection(marketName, password));
                count = 0;
                newMarket = new Market(marketName, password);
                isSignup = 1;
                break;
            case 5:
                isSignup=3;
                System.out.println("see ya");
                break;

        }
        System.out.println("login success!");

        while (isSignup == 0)
            switch (userDirection.afterLoginAllUserOptions()) {
                case 1:
                    do {
                        //this is for second try if user name already exist
                        if (count != 0) {
                            userDirection.productNameExistInMap();
                        }
                        // getting information fot creating a new user
                        name = userDirection.name();
                        price = userDirection.price();
                        amount = userDirection.amount();
                        count = 1;
                        //puting all information in the new user for creation
                    } while (!Server.addingNewProduct(name, price, amount,1 ));
                    count = 0;
                    break;
                case 2:
                    Server.getUserProduct();
                    break;
                case 3:
                    Server.userUpdateProductDetails();
                    break;
                case 4:
                    Server.userRemoveProduct();
                    break;
                case 5:
                    System.out.println("see ya");
                    isSignup=3;
                    break;
            }
        while (isSignup == 1) {
            switch (userDirection.afterLoginAllUserOptions()) {
                case 1:
                    do {
                        //this is for second try if user name already exist
                        if (count != 0) {
                            userDirection.productNameExistInMap();
                        }
                        // getting information fot creating a new user
                        name = userDirection.name();
                        price = userDirection.price();
                        amount = userDirection.amount();
                        count = 1;
                        //puting all information in the new user for creation
                    } while (!Server.addingNewProduct(name, price, amount, 2));
                    count = 0;
                    break;
                case 2:
                    Server.getMarketProduct();
                    break;
                case 3:
                    Server.marketUpdateProductDetails();
                    break;
                case 4:
                    Server.marketRemoveProduct();
                    break;
                case 5:
                    System.out.println("see ya");
                    isSignup=3;
                    break;
            }
        }

    }
}