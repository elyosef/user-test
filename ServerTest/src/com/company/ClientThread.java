package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;


public class ClientThread extends Thread {

    public static final int OKAY = 200;
    public static final int FAILURE = 201;

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

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private Map<String, NewUser> usersMap;
    private Map<String, Market> marketMap;
    private Map<String, Product> products;
    private File fileUsers;
    private File fileMarket;
    private File fileProducts;


    public ClientThread(Socket socket, Map<String, NewUser> usersMap, Map<String, Market> marketMap, Map<String, Product> products, File fileUsers, File fileMarket, File fileProducts) {
        this.socket = socket;
        this.usersMap = usersMap;
        this.marketMap = marketMap;
        this.products = products;
        this.fileUsers = fileUsers;
        this.fileMarket = fileMarket;
        this.fileProducts = fileProducts;
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            int action = inputStream.read();

            switch (action) {
                case SIGNUP:
                    signup();
                    GetMap(usersMap);
                    sendMapToFile(fileUsers, usersMap, 1);
                    break;
                case LOGIN:
                    login();
                    break;
                case NEW_PRODUCT:
                    addNewProductRemoveOrReplaceProduct(1, 1);
                    sendMapToFile(fileUsers, usersMap, 1);
                    GetMap(usersMap);
                    sendMapToFile(fileProducts, products, 3);
                    GetMap(products);
                    break;
                case LOGIN_MARKET:
                    loginMarket();
                    break;
                case MARKET_SINGUP:
                    signupMarket();
                    GetMap(marketMap);
                    sendMapToFile(fileMarket, marketMap, 2);
                    break;
                case NEW_PRODUCT_MARKET:
                    addNewProductRemoveOrReplaceProduct(2, 1);
                    sendMapToFile(fileMarket, marketMap, 2);
                    GetMap(marketMap);
                    sendMapToFile(fileProducts, products, 3);
                    GetMap(products);
                    break;
                case SEND_USER_HIS_PRODUCS:
                    sendProductsOfUserOrMarket(1);
                    break;
                case SEND_MARKET_HIS_PRODUCS:
                    sendProductsOfUserOrMarket(2);
                    break;
                case USER_UPDATE_PRODUCT_DETAILS:
                    addNewProductRemoveOrReplaceProduct(1, 2);
                    sendMapToFile(fileUsers, usersMap, 1);
                    GetMap(usersMap);
                    sendMapToFile(fileProducts, products, 3);
                    GetMap(products);
                    break;
                case MARKET_UPDATE_PRODUCT_DETAILS:
                    addNewProductRemoveOrReplaceProduct(2, 2);
                    sendMapToFile(fileMarket, marketMap, 2);
                    GetMap(marketMap);
                    sendMapToFile(fileProducts, products, 3);
                    GetMap(products);
                    break;
                case USER_REMOVE_PRODUCT:
                    addNewProductRemoveOrReplaceProduct(1, 3);
                    sendMapToFile(fileUsers, usersMap, 1);
                    GetMap(usersMap);
                    sendMapToFile(fileProducts, products, 3);
                    GetMap(products);
                    break;
                case MARKET_REMOVE_PRODUCT:
                    addNewProductRemoveOrReplaceProduct(2, 3);
                    sendMapToFile(fileMarket, marketMap, 2);
                    GetMap(marketMap);
                    sendMapToFile(fileProducts, products, 3);
                    GetMap(products);
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //new user sighup
    private void signup() throws IOException {

        //getting from clint information for creating new user
        NewUser newUser = new NewUser(inputStream);

        boolean success = false;//for telling user everything is ok

        //holding map from getting new user to avoid 2 same users
        synchronized (usersMap) {
            if (!usersMap.containsKey(newUser.getName())) { //if the new user doz not exist
                usersMap.put(newUser.getName(), newUser);//put the new user in map
                success = true; //all went well? yes!!
            }
        }
        //now telling user if all went well or not....
        outputStream.write(success ? OKAY : FAILURE);
    }

    //new market sighup
    private void signupMarket() throws IOException {

        //getting from clint information for creating new market
        Market market = new Market(inputStream);

        boolean success = false;//for telling user everything is ok

        //holding map from getting new user to avoid 2 same users
        synchronized (marketMap) {
            if (!marketMap.containsKey(market.getName())) { //if the new user doz not exist
                marketMap.put(market.getName(), market);//put the new user in map
                success = true; //all went well? yes!!
            }
        }
        //now telling user if all went well or not....
        outputStream.write(success ? OKAY : FAILURE);
    }

    //user login
    private void login() throws IOException {
        NewUser newUser = new NewUser(inputStream);
        outputStream.write(validateUser(newUser) ? OKAY : FAILURE);
    }

    //market login
    private void loginMarket() throws IOException {
        Market market = new Market(inputStream);
        outputStream.write(validateMarket(market) ? OKAY : FAILURE);
    }

    //when login this checks if the user exist in user map
    private boolean validateUser(NewUser newUser) {
        NewUser existingUser = usersMap.get(newUser.getName());
        if (existingUser != null) {
            return newUser.getPassword().equals(existingUser.getPassword());
        }
        return false;
    }

    //when login this checks if the market exist in market map
    private boolean validateMarket(Market market) {
        Market existingMarket = marketMap.get(market.getName());
        if (existingMarket != null) {
            return market.getPassword().equals(existingMarket.getPassword());
        }
        return false;
    }


    //for server side to know what is happening with new objects singing ...
    public void GetMap(Map map) {
        for (Object obj : map.keySet()) {
            System.out.println(map.get(obj.toString()));
        }
    }

    /**
     * this method can do 3 things according to the numbers u put in
     * and doz those 3 things to ether kind of account after log in according to @param.
     * one adds new product to exists account.
     * tow updates product of exists account.
     * three removers product from exists account.
     *
     * @param accountType // (1) user (2) market
     * @param action      (1) add , (2) replace ,(3) remove
     * @throws IOException
     */
    private void addNewProductRemoveOrReplaceProduct(int accountType, int action) throws IOException {

        switch (accountType) {
            //user
            case 1:
                NewUser newUser = new NewUser(inputStream);
                if (!validateUser(newUser))
                    return;
                //creating a new product form getting information to
                Product product = new Product(inputStream);

                if (action == 1) {
                //must create new hash map if doz noy exist
                if (this.products == null) {
                    products = new HashMap<>();
                }
                //add
                    //now checking if this new product exist only to know if to put in server product map**
                    synchronized (products) {
                        if (!this.products.containsKey(product.getName())) {
                            products.put(product.getName(), product);//**
                            usersMap.get(newUser.getName()).addProduct(product.getName(), product);
                            outputStream.write(OKAY);
                            System.out.println(product);
                            return;
                        }
                    }
                    if (this.products.containsKey(product.getName())) {
                        if (usersMap.get(newUser.getName()).getMyProducts().containsKey(product.getName())) {
                            outputStream.write(FAILURE);
                            return;
                        }
                        if (!usersMap.get(newUser.getName()).getMyProducts().containsKey(product.getName())) {
                            usersMap.get(newUser.getName()).addProduct(product.getName(), product);
                            outputStream.write(OKAY);
                            setMainProductsPriceAndAmount(product);
                            return;
                        }
                    }return;
                }
                //replace
                if (action == 2) {
                    String key = product.getName();
                    double oldPrice = usersMap.get(newUser.getName()).getMyProducts().get(key).getPrice();
                    int oldAmount = usersMap.get(newUser.getName()).getMyProducts().get(key).getAmount();

                    usersMap.get(newUser.getName()).getMyProducts().remove(key);
                    usersMap.get(newUser.getName()).addProduct(product.getName(), product);
                    outputStream.write(OKAY);

                    oldAmount = (product.getAmount()) - oldAmount ;

                    oldPrice = (int)(((oldPrice + product.getPrice()) / 2 )*10);
                    oldPrice = oldPrice/10;

                    Product product0 = new Product(key, oldPrice, oldAmount);

                    setMainProductsPriceAndAmount(product0);

                    GetMap(products);
                    return;
                }
                if (action == 3) {
                    String key = product.getName();
                    if (!usersMap.get(newUser.getName()).getMyProducts().containsKey(key)){
                        outputStream.write(FAILURE);
                    return;
                }

                    double oldPrice = usersMap.get(newUser.getName()).getMyProducts().get(key).getPrice();
                    int oldAmount = usersMap.get(newUser.getName()).getMyProducts().get(key).getAmount();

                    usersMap.get(newUser.getName()).getMyProducts().remove(key);
                    outputStream.write(OKAY);

                    products.get(key).setPrice((products.get(key).getPrice() + oldPrice) / 2);
                    products.get(key).setAmount(products.get(key).getAmount() - oldAmount);

                    GetMap(products);

                    return;
                }

                break;
            //market
            case 2:
                Market market = new Market(inputStream);
                if (!validateMarket(market)) {
                    return;
                }
                Product product1 = new Product(inputStream);

                if (action == 1) {
                    if (this.products == null) {
                        products = new HashMap<>();
                    }
                    synchronized (products) {
                        if (!this.products.containsKey(product1.getName())) {
                            products.put(product1.getName(), product1);//**
                            marketMap.get(market.getName()).addProduct(product1.getName(), product1);
                            outputStream.write(OKAY);
                            System.out.println(product1);
                            return;
                        }
                    }
                    if (this.products.containsKey(product1.getName())) {
                        //add

                        if (marketMap.get(market.getName()).getMyProducts().containsKey(product1.getName())) {
                            outputStream.write(FAILURE);
                            return;
                        }
                        if (!marketMap.get(market.getName()).getMyProducts().containsKey(product1.getName())) {
                            marketMap.get(market.getName()).addProduct(product1.getName(), product1);
                            outputStream.write(OKAY);

                            setMainProductsPriceAndAmount(product1);
                            return;
                        }
                    }
                }
                //replace
                if (action == 2) {
                    String key1 = product1.getName();
                    double oldPrice1 = marketMap.get(market.getName()).getMyProducts().get(key1).getPrice();
                    int oldAmount1 = marketMap.get(market.getName()).getMyProducts().get(key1).getAmount();

                    marketMap.get(market.getName()).getMyProducts().remove(key1);
                    marketMap.get(market.getName()).addProduct(product1.getName(), product1);
                    outputStream.write(OKAY);


                    oldAmount1 = (product1.getAmount()) - oldAmount1 ;

                    oldPrice1 = (int)(((oldPrice1 + product1.getPrice()) / 2 )*10);
                    oldPrice1 = oldPrice1/10;

                    Product product00 = new Product(key1, oldPrice1, oldAmount1);

                    setMainProductsPriceAndAmount(product00);

                    GetMap(products);
                    return;
                }
                if (action == 3) {
                    String key = product1.getName();
                    if (!marketMap.get(market.getName()).getMyProducts().containsKey(key)){
                        outputStream.write(FAILURE);
                    return;}
                    double oldPrice = marketMap.get(market.getName()).getMyProducts().get(key).getPrice();
                    int oldAmount = marketMap.get(market.getName()).getMyProducts().get(key).getAmount();

                    marketMap.get(market.getName()).getMyProducts().remove(key);
                    outputStream.write(OKAY);

                    products.get(key).setPrice((products.get(key).getPrice() + oldPrice) / 2);
                    products.get(key).setAmount(products.get(key).getAmount() - oldAmount);

                    GetMap(products);
                    return;
                }
                break;
        }


    }

    /**
     * this method gets a file map and updates it according to action number
     *
     * @param file//of (1)userMap , (2)marketMap , (3)products
     * @param map      //only (1)userMap , (2)marketMap , (3)products
     * @param action   (1)userMap , (2)marketMap , (3)products
     */
    public void sendMapToFile(File file, Map map, int action) {

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);

            byte[] mapSize = new byte[4];
            ByteBuffer.wrap(mapSize).putInt(map.size());
            outputStream.write(mapSize);

            if (action == 1) {
                for (Object obj : map.values()) {
                    ((NewUser) obj).write(outputStream);
                    ((NewUser) obj).sendingProduct(outputStream);
                }
            }
            if (action == 2) {
                for (Object obj : map.values()) {
                    ((Market) obj).write(outputStream);
                    ((Market) obj).sendingProduct(outputStream);
                }
            }
            if (action == 3) {
                for (Object obj : map.values()) {
                    ((Product) obj).write(outputStream);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
     * this method sets main product Map amount and price when
     * a user or market adds new product
     *
     * @param product
     */
    private void setMainProductsPriceAndAmount(Product product) {
        String key = product.getName();
        double price = this.products.get(key).getPrice();
        int amount = this.products.get(key).getAmount();

        this.products.get(key).setPrice((price + product.getPrice()) / 2);
        this.products.get(key).setAmount(amount + product.getAmount());
    }

    /**
     * this method goes to user or market according to int 1 for user 2 for market login
     * and sends him his products
     *
     * @param userOrMarket (1) user (2)market
     * @throws IOException
     */
    public void sendProductsOfUserOrMarket(int userOrMarket) throws IOException {

        switch (userOrMarket) {
            //user
            case 1:
                NewUser newUser = new NewUser(inputStream);
                if (!validateUser(newUser))
                    return;
                usersMap.get(newUser.getName()).sendingProduct(outputStream);
                break;
            //market
            case 2:
                Market market = new Market(inputStream);
                if (!validateMarket(market))
                    return;
                marketMap.get(market.getName()).sendingProduct(outputStream);
                break;
        }

    }

}