package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Main {


    public static final int PORT = 3001;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try{
            //פתיחת פורט
            serverSocket = new ServerSocket(PORT);

            Map<String, NewUser> usersMap = new HashMap<>();
            Map<String, Market> marketMap = new HashMap<>();
            Map <String , Product> productMap= new HashMap<>();

            File usersFileMap =new File("usersMap.txt");
            File marketFileMap =new File("marketMap.txt");
            File productFileMap =new File("productMap.txt");

            if (getFileToMap(marketFileMap,marketMap,2)){
                System.out.println(marketMap);
            }

            if (getFileToMap(usersFileMap,usersMap,1)){
                System.out.println(usersMap);
            }

            if (getFileToMap(productFileMap,productMap,3)){
                System.out.println(productMap);
            }

            while (true){
                Socket socket = serverSocket.accept();

                new ClientThread(socket,usersMap,marketMap,productMap,usersFileMap,marketFileMap,productFileMap).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean getFileToMap(File file, Map map, int x){

        if (!file.exists()){
            return false;
        }

        FileInputStream inputStream = null;
        try{
            NewUser newUser;
            Market market;
            Product product;

            inputStream = new FileInputStream(file);

            byte[] bufferSize = new byte[4];
            inputStream.read(bufferSize);
            int mapSiZe = ByteBuffer.wrap(bufferSize).getInt();


            if (x == 1) {
                for (int i = 0; i <mapSiZe ; i++) {
                    newUser= new NewUser (inputStream);
                    newUser.gettingProduct(inputStream);
                    map.put(newUser.getName() , newUser);
                }}
            if (x==2){
                for (int i = 0; i <mapSiZe ; i++) {
                    market= new Market (inputStream);
                    market.gettingProduct(inputStream);
                    map.put(market.getName() , market);
                }}
            if (x==3) {
                for (int i = 0; i < mapSiZe; i++) {
                    product = new Product(inputStream);
                    map.put(product.getName(), product);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

        }return true;
    }




}
