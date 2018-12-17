package com.company;

import java.util.Scanner;
//this class is only for directions if we ues console
public class UserDirection {

    Scanner s = new Scanner(System.in);
    //main log in program
    public int login(){
        Scanner s = new Scanner(System.in);
        int x =0;
        int ok = 0;
        System.out.println("What's up");
        System.out.println("are you a:");

        do{
            try {
                System.out.println("New user ? press 1");
                System.out.println("Exist user ? press 2");
                System.out.println("New market ? press 3");
                System.out.println("Exist market ? press 4");
                System.out.println("press 5 to go out of program");
                System.out.println("Type between 1 or 5 please");
                ok = s.nextInt();
                if (ok>=1 || ok<=5){
                    x=1;
                }
            } catch (Exception e) {
                s.nextLine();
            }
        }while (x==0);
        return ok;
    }
    //gives direction for getting user name and spits out user name with no exceptions
    public String newUesrName(){
        Scanner s = new Scanner(System.in);
        String name =null;
        do{
            try {
                System.out.println("Enter your first name pleas");
                name = s.next();
            } catch (Exception e) {
                s.nextLine();
            }
        }while (name.equals(null));

        return name;
    }
    //gives direction for getting family name and spits out family name with no exceptions
    public String newUserFamily(){
        Scanner s = new Scanner(System.in);
        String name =null;
        do{
            try {
                System.out.println("Enter your Family name pleas");
                name = s.next();
            } catch (Exception e) {
                s.nextLine();
            }
        }while (name.equals(null));

        return name;
    }
    //gives direction for getting Id and spits out Id with no exceptions
    public int Id(){
        Scanner s = new Scanner(System.in);
        int x =0;
        int ok = 0;
        do{
            try {
                System.out.println("Enter your ID pleas");
                ok = s.nextInt();
                if (ok!=0) {
                    x = 1;
                }
            } catch (Exception e) {
                s.nextLine();
            }
        }while (x==0);
        return ok;
    }
    //gives direction for getting password and spits out password with no exceptions
    public String newUesrPssWord(){
        Scanner s = new Scanner(System.in);
        String password =null;
        do{
            try {
                System.out.println("Enter your password pleas");
                password = s.next();
            } catch (Exception e) {
                s.nextLine();
            }
        }while (password.equals(null));

        return password;
    }
    //gives direction for getting age and spits out age with no exceptions
    public int age (){
        System.out.println("Enter student age");
        return s.nextInt();
    }
    //gives direction for getting address and spits out address with no exceptions
    public String address(){
        System.out.println("Enter student address");
        return s.next();
    }
    //gives direction if in sign up the name is already taken
    public void userNameExistInMap(){
        System.out.println("User name exist please write a different name");
    }
    //main program options and spits out the number option with no exceptions
    public int afterLoginAllUserOptions(){
        Scanner s = new Scanner(System.in);
        boolean x = false;
        int ok = 0;
        System.out.println("what do you want to do?");

        do{
            try {
                System.out.println("press");
                System.out.println("1 >>for adding a new product to sell");
                System.out.println("2 >>To see your products");
                System.out.println("3 >>To update a product");
                System.out.println("4 >>To remove a product");
                System.out.println("5 >>To exist");
                System.out.println("Type  1 - 5 pleas");
                ok = s.nextInt();
                if (ok>0 && ok<=5){
                    x=true;
                }
            } catch (Exception e) {
                s.nextLine();
            }
        }while (x==false);
        return ok;
    }
    //gives direction for getting name and spits out name with no exceptions
    public String name(){
        Scanner s = new Scanner(System.in);
        String name =null;
        do{
            try {
                System.out.println("Enter your product name pleas");
                name = s.next();
            } catch (Exception e) {
                s.nextLine();
            }
        }while (name.equals(null));

        return name;
    }
    //gives direction for getting price and spits out price with no exceptions
    public double price(){
        Scanner s = new Scanner(System.in);
        int x =0;
        double ok = 0;
        do{
            try {
                System.out.println("Enter your product price pleas");
                ok = s.nextDouble();
                if (ok!=0) {
                    x = 1;
                }
            } catch (Exception e) {
                s.nextLine();
            }
        }while (x==0);
        return ok;
    }
    //gives direction for getting amount and spits out amount with no exceptions
    public int amount(){
        Scanner s = new Scanner(System.in);
        int x =0;
        int ok = 0;
        do{
            try {
                System.out.println("Enter the amount of this product you have pleas");
                ok = s.nextInt();
                if (ok!=0) {
                    x = 1;
                }
            } catch (Exception e) {
                s.nextLine();
            }
        }while (x==0);
        return ok;
    }
    //if product already exists in server this is what user sees
    public void productNameExistInMap(){
        System.out.println(" problem with adding the product please try again");
    }
    //gives direction for getting market name and spits out market name with no exceptions
    public String marketName(){
        Scanner s = new Scanner(System.in);
        String name =null;
        do{
            try {
                System.out.println("Enter your market name pleas");
                name = s.next();
            } catch (Exception e) {
                s.nextLine();
            }
        }while (name.equals(null));

        return name;
    }
    //gives direction for getting open time and spits out open time with no exceptions
    public int open(){
        Scanner s = new Scanner(System.in);
        int x =0;
        int ok = 0;
        do{
            try {
                System.out.println("At what time doz the market opens (only full hours) ");
                ok = s.nextInt();
                if (ok>=0 || ok<=24){
                    x=1;
                }
            } catch (Exception e) {
                s.nextLine();
            }
        }while (x==0);
        return ok;
    }
    //gives direction for getting close time and spits out oclose time with no exceptions
    public int close(){
        Scanner s = new Scanner(System.in);
        int x =0;
        int ok = 0;
        do{
            try {
                System.out.println("At what time doz the market closes (only full hours) ");
                ok = s.nextInt();
                if (ok>=0 || ok<=24){
                    x=1;
                }
            } catch (Exception e) {
                s.nextLine();
            }
        }while (x==0);
        return ok;
    }
}
