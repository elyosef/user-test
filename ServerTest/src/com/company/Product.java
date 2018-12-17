package com.company;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.nio.ByteBuffer;
        import java.util.Objects;


public class Product {
    //product name
    private String name;
    //product price
    private double price;
    //product amount
    private int amount;

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}'+"\n";
    }

    /**
     * constructor self create
     * @param name
     * @param price
     * @param amount
     */

    public Product(String name, double price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }
    /**
     * constructor inputStream create
     * @param inputStream
     * @throws IOException
     */
    public  Product (InputStream inputStream)throws IOException {

        int actuallyRead;

        //incoming name
        int productNameLength = inputStream.read();
        byte[] productNameBytes = new byte[productNameLength];
        actuallyRead = inputStream.read(productNameBytes);
        if(actuallyRead != productNameLength)
            throw new IOException("problem with product name"+productNameLength+" "+actuallyRead);
        this.name = new String(productNameBytes);

        //incoming price
        byte[] doubleBytes = new byte[8];
        actuallyRead = inputStream.read(doubleBytes);
        if(actuallyRead != 8)
            throw new IOException("expected 8 bytes but received " + actuallyRead + " bytes.. price");
        this.price = ByteBuffer.wrap(doubleBytes).getDouble();

        //incoming amount
        byte[] intBytes = new byte[4];
        actuallyRead = inputStream.read(intBytes);
        if(actuallyRead != 4)
            throw new IOException("expected four bytes but received " + actuallyRead + " bytes..  amount");
        this.amount = ByteBuffer.wrap(intBytes).getInt();

    }


    //getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    //getters
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    /**
     * write outputStream this class fields
     * @param outputStream
     * @throws IOException
     */
    public void write(OutputStream outputStream) throws IOException{

        //outgoing sending this name
        byte[] uameBytes = name.getBytes();
        outputStream.write(uameBytes.length);
        outputStream.write(uameBytes);

        //outgoing sending this price
        byte[] doubleBytes = new byte[8];
        ByteBuffer.wrap( doubleBytes).putDouble(price);
        outputStream.write(doubleBytes);

        //outgoing sending this amount
        byte[] IdBytes = new byte[4];
        ByteBuffer.wrap( IdBytes).putInt(amount);
        outputStream.write(IdBytes);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.getPrice(), getPrice()) == 0 &&
                getAmount() == product.getAmount() &&
                Objects.equals(getName(), product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getAmount());
    }
}
