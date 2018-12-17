package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by hackeru on 22 נוב 18.
 */
public class Server {
    public static final int SIGNUP = 102;
    public static final int LOGIN = 103;

    public static final int MARKET_SINGUP = 106;
    public static final int LOGIN_MARKET = 105;

    public static final int NEW_PRODUCT = 104;
    public static final int NEW_PRODUCT_MARKET = 107;

    public static final int SEND_USER_HIS_PRODUCS = 108;
    public static final int SEND_MARKET_HIS_PRODUCS = 111;

    public static final int USER_UPDATE_PRODUCT_DETAILS=109;
    public static final int MARKET_UPDATE_PRODUCT_DETAILS=110;

    public static final int USER_REMOVE_PRODUCT=112;
    public static final int MARKET_REMOVE_PRODUCT=113;

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 3001;
    public static final int OKAY = 200;
    public static final int FAILURE = 201;


    public static NewUser newUser;
    public static Product newProduct;
    public static Market newMarket;
    private static UserDirection userDirection=new UserDirection();

    /**
     * main user Connection
     * @param userName
     * @param password
     * @return
     */
    public static boolean mainUserConnection(String userName,  String password ){
        return mainUserConnection(userName,"", 0,password,true);
    }

    //new user sighup
    public static boolean mainUserConnection(String userName, String family, int Id, String password , boolean isLogin){

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            if (isLogin){
                outputStream.write(LOGIN);
                newUser = new NewUser(userName, password);
            }else {
                outputStream.write(SIGNUP);
                newUser = new NewUser(userName,family,Id, password);
            }
            newUser.write(outputStream ,isLogin);
            int response = inputStream.read();
            if(response == OKAY){
                return true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    public static boolean addingNewProduct (String name, double price , int amount, int x){

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            if(x==1) {
                outputStream.write(Server.NEW_PRODUCT);
                newUser.write(outputStream, true);
                newProduct = new Product(name, price, amount);
                newProduct.write(outputStream);

            }
            if (x==2){
                outputStream.write(NEW_PRODUCT_MARKET);
                newMarket.write(outputStream, true);
                newProduct = new Product(name, price, amount);
                newProduct.write(outputStream);
            }


            int response = inputStream.read();
            if(response == OKAY){
                System.out.println("new product added successfully");
                return true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    //Market sighup
    public static boolean mainMarketConnection(String marketName,  String password ){
        return mainMarketConnection(marketName,"", 0,password,true,0,0);
    }
    //new market sighup
    public static boolean mainMarketConnection(String marketName, String family, int Id, String password , boolean isLogin, int close, int open){

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            if (isLogin){
                outputStream.write(LOGIN_MARKET);
                newMarket = new Market(marketName, password);
            }else {
                outputStream.write(MARKET_SINGUP);
                newMarket = new Market(marketName,family,Id, password,open,close);
            }
            newMarket.write(outputStream ,isLogin);
            int response = inputStream.read();
            if(response == OKAY){
                return true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * shows user his products
     */
    public static void getUserProduct() {

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            outputStream.write(SEND_USER_HIS_PRODUCS);
            newUser.write(outputStream, true);

            newUser.gettingProduct(inputStream);
            System.out.println(newUser.getMyProducts());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * shows market his products
     */
    public static void getMarketProduct() {

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            outputStream.write(SEND_MARKET_HIS_PRODUCS);
            newMarket.write(outputStream, true);

            newMarket.gettingProduct(inputStream);
            System.out.println(newMarket.getMyProducts());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * user Update his Product Details
     */
    public static void userUpdateProductDetails() {

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            outputStream.write(USER_UPDATE_PRODUCT_DETAILS);
            newUser.write(outputStream, true);

            getUserProduct();
            System.out.println("witch one? write the product name you want to update his details");

            String name = userDirection.name();
            double price = userDirection.price();
            int amount = userDirection.amount();

            Product product=new Product(name,price,amount);
            product.write(outputStream);

            int response = inputStream.read();
            if(response == OKAY){
                System.out.println( "product updated successfully");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * market Update his Product Details
     */
    public static void marketUpdateProductDetails() {

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            outputStream.write(MARKET_UPDATE_PRODUCT_DETAILS);
            newMarket.write(outputStream, true);

            getMarketProduct();
            System.out.println("witch one? write the product name you want to update his details");

            String name = userDirection.name();
            double price = userDirection.price();
            int amount = userDirection.amount();

            Product product=new Product(name,price,amount);
            product.write(outputStream);

            int response = inputStream.read();
            if(response == OKAY){
                System.out.println( "product updated successfully");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * market Remove his Product
     */
    public static void marketRemoveProduct() {

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            outputStream.write(MARKET_REMOVE_PRODUCT);
            newMarket.write(outputStream, true);

            getMarketProduct();
            System.out.println("witch one? write the product name you want to remove");

            String name = userDirection.name();
            double price = 0;
            int amount = 0;

            Product product=new Product(name,price,amount);
            product.write(outputStream);

            int response = inputStream.read();
            if(response == OKAY){
                newMarket.getMyProducts().remove(name);
                System.out.println( "product removed successfully");
            }
            if(response == FAILURE){

                System.out.println( "you didn't have this product ");
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * user remove his Product
     */
    public static void userRemoveProduct() {

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            outputStream.write(USER_REMOVE_PRODUCT);
            newUser.write(outputStream, true);

            getUserProduct();
            System.out.println("witch one? write the product name you want to remove");

            String name = userDirection.name();
            double price = 0;
            int amount = 0;

            Product product=new Product(name,price,amount);
            product.write(outputStream);

            int response = inputStream.read();
            if(response == OKAY){
                newUser.getMyProducts().remove(name);
                System.out.println( "product removed successfully");
            }
            if(response == FAILURE){

                System.out.println( "you didn't have this product ");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}





