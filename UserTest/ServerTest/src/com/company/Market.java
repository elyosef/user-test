package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;

public class Market extends NewUser implements Serializable,Sendable {

    private int openTime;
    private int closeTime;

    Date date =new Date();

    public Market(String name, String family, int ID, String password, int openTime, int closeTime) {
        super(name, family, ID, password);
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public Market(String name, String password) {
        super(name, password);
    }

    public Market(Map<String, Product> myProducts) {
        super(myProducts);
    }

    //getters

    public int getOpenTime() {
        return openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }


    //setters


    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }


    public boolean ifopen(){
        if (this.date.getHours()>openTime && this.date.getHours()<closeTime){
            return true;
        }else {
            return false;
        }
    }

    public Market(InputStream inputStream) throws IOException {
        super(inputStream);

        int actuallyRead;

        if (inputStream.read()==2){

            //incoming open time
            byte[] openBytes = new byte[4];
            actuallyRead = inputStream.read(openBytes);
            if(actuallyRead != 4)
                throw new IOException("expected four bytes but received " + actuallyRead + " bytes..  :-(");
            this.openTime=ByteBuffer.wrap(openBytes).getInt();

            //incoming close time
            byte[] closeBytes = new byte[4];
            actuallyRead = inputStream.read(closeBytes);
            if(actuallyRead != 4)
                throw new IOException("expected four bytes but received " + actuallyRead + " bytes..  :-(");
            this.closeTime=ByteBuffer.wrap(closeBytes).getInt();
        }

    }

    //default sending (all market obj)
    public void write(OutputStream outputStream)throws IOException{
        write(outputStream, false);
    }

    //sending the market to server
    public void write(OutputStream outputStream , boolean login) throws IOException{
        super.write(outputStream,login);

        outputStream.write(login ? 1 : 2);

        if(!login){

            //outgoing sending this openTime
            byte[] openBytes = new byte[4];
            ByteBuffer.wrap(openBytes ).putInt(openTime);
            outputStream.write(openBytes);


            //outgoing sending this closeTime
            byte[] closeBytes = new byte[4];
            ByteBuffer.wrap( closeBytes ).putInt(closeTime);
            outputStream.write(closeBytes);


        }

    }

    @Override
    public String toString() {
        return "Market{" +
                "openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", date=" + date + super.toString()+
                '}'+"\n";
    }
}
