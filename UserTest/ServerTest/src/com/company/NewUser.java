package com.company;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class NewUser implements Serializable,Sendable {
    private String name;
    private String  password;
    private String family;
    private int ID;
    private double coins;
    private Map<String ,Product > myProducts=new HashMap<>();


    @Override
    public String toString() {
        return "NewUser{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", family='" + family + '\'' +
                ", ID=" + ID +
                ", coins=" + coins+
                "\n"+", myProducts=" + myProducts +
                '}'+"\n";
    }


    //constructor

    public NewUser(String name, String family, int ID, String password) {
        this.name = name;
        this.family = family;
        this.ID = ID;
        this.password = password;
    }

    public NewUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public NewUser(Map<String ,Product > myProducts) {
        this.myProducts = myProducts;
    }

    //getters


    public double getCoins() {
        return coins;
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public int getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    public Map<String ,Product > getMyProducts() {
        return myProducts;
    }

    //setters


    public void setCoins(double coins) {
        this.coins = coins;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMyProducts(Map<String ,Product > myProducts) {
        this.myProducts = myProducts;
    }

    public void addProduct(String name , Product product){
        if(myProducts==null){
            myProducts = new HashMap<>();
        }
        myProducts.put(name ,product);
    }

    public String productName(){
        Scanner s = new Scanner(System.in);
        String name =null;
        do{
            try {
                System.out.println("Enter the product name plaes");
                name = s.next();
            } catch (Exception e) {
                s.nextLine();
            }
        }while (name.equals(null));

        return name;
    }

    public double price(){
        Scanner s = new Scanner(System.in);
        int x =0;
        int ok = 0;
        do{
            try {
                System.out.println("Enter your price");
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

    public int amount(){
        Scanner s = new Scanner(System.in);
        int x =0;
        int ok = 0;
        do{
            try {
                System.out.println("Enter the amount you have");
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

    //</editor-fold>
    //getting the user from user
    public NewUser(InputStream inputStream) throws IOException {

        int actuallyRead;

        if (inputStream.read()==2){

            //incoming family
            int userFamilyLength = inputStream.read();
            byte[] userFamilyBytes = new byte[userFamilyLength];
            actuallyRead = inputStream.read(userFamilyBytes);
            if(actuallyRead != userFamilyLength)
                throw new IOException("problem with user family");
            this.family = new String(userFamilyBytes);

            //incoming id
            byte[] intBytes = new byte[4];
            actuallyRead = inputStream.read(intBytes);
            if(actuallyRead != 4)
                throw new IOException("expected four bytes but received " + actuallyRead + " bytes..  :-(");
            this.ID = ByteBuffer.wrap(intBytes).getInt();

            //incoming coins
            byte[] coinsBytes = new byte[8];
            actuallyRead = inputStream.read(coinsBytes);
            if(actuallyRead != 8)
                throw new IOException("expected four bytes but received " + actuallyRead + " bytes..  :-(");
            this.coins = ByteBuffer.wrap(coinsBytes).getDouble();
        }

        //incoming name
        int userNameLength = inputStream.read();
        byte[] userNameBytes = new byte[userNameLength];
        actuallyRead = inputStream.read(userNameBytes);
        if(actuallyRead != userNameLength)
            throw new IOException("problem with user name");
        this.name = new String(userNameBytes);

        //incoming password
        int passwordLength = inputStream.read();
        byte[] passwordBytes = new byte[passwordLength];
        actuallyRead = inputStream.read(passwordBytes);
        if(actuallyRead != passwordLength)
            throw new IOException("problem with user password");
        this.password = new String(passwordBytes);

    }

    //default sending (all user obj)
    public void write(OutputStream outputStream)throws IOException{
        write(outputStream, false);
    }

    //sending the user to user
    public void write(OutputStream outputStream , boolean login) throws IOException{

        outputStream.write(login ? 1 : 2);

        if(!login){
            //outgoing sending this family
            byte[] familyNameBytes = family.getBytes();
            outputStream.write(familyNameBytes.length);
            outputStream.write(familyNameBytes);

            //outgoing sending this ID
            byte[] IdBytes = new byte[4];
            ByteBuffer.wrap( IdBytes).putInt(ID);
            outputStream.write(IdBytes);

            //outgoing sending this coins
            byte[] coinsBytes = new byte[8];
            ByteBuffer.wrap(coinsBytes).putDouble(coins);
            outputStream.write(coinsBytes);

        }
        //outgoing sending this name
        byte[] userNameBytes = name.getBytes();
        outputStream.write(userNameBytes.length);
        outputStream.write(userNameBytes);

        //outgoing sending this password
        byte[] passwordBytes = password.getBytes();
        outputStream.write(passwordBytes.length);
        outputStream.write(passwordBytes);

    }

    public void sendingName (OutputStream outputStream) throws IOException {

        //outgoing sending this name
        byte[] userNameBytes = name.getBytes();
        outputStream.write(userNameBytes.length);
        outputStream.write(userNameBytes);
    }

    public void sendingProduct(OutputStream outputStream) throws IOException {
        if (myProducts==null){
            return;
        }

        byte[] mapSize = new byte[4];
        ByteBuffer.wrap( mapSize).putInt(myProducts.size());
        outputStream.write(mapSize);

        for (Object product :myProducts.values()){
            ((Product) product).write(outputStream);
        }

    }

    public void gettingProduct(InputStream inputStream) throws IOException {

        Product product;


        byte[] mapSize = new byte[4];
        int actuallyRead = inputStream.read(mapSize);
        if(actuallyRead != 4)
            throw new IOException("expected four bytes but received " + actuallyRead + " bytes..  :-(");
        int mapSiZe = ByteBuffer.wrap(mapSize).getInt();


        for (int i = 0; i <mapSiZe ; i++) {
            product= new Product (inputStream);
            myProducts.put(product.getName() , product);
        }

    }



}
