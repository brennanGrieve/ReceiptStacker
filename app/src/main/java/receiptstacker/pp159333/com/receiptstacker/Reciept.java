package receiptstacker.pp159333.com.receiptstacker;

import android.graphics.Bitmap;

import java.util.Date;

public class Reciept {
    private String ProductName;
    private String BussinessName;
    private float price;
    private Date dateOfPurchase;
    private Bitmap image;

    public Reciept(String productName, String bussinessName, float price, Date dateOfPurchase) {
        ProductName = productName;
        BussinessName = bussinessName;
        this.price = price;
        this.dateOfPurchase = dateOfPurchase;
    }

    public Reciept(String productName, String bussinessName, float price, Date dateOfPurchase, Bitmap image) {
        ProductName = productName;
        BussinessName = bussinessName;
        this.price = price;
        this.dateOfPurchase = dateOfPurchase;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getBussinessName() {
        return BussinessName;
    }

    public float getPrice() {
        return price;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }
}
