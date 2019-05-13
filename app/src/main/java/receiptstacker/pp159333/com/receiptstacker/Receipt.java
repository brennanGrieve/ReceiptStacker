package receiptstacker.pp159333.com.receiptstacker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Date;
import java.util.List;

import static android.graphics.Bitmap.createBitmap;

/*
A class representing a Receipt.
 */

public class Receipt {
    private String ProductName;
    private String businessName;
    private Float price;
    private Date dateOfPurchase;
    private Bitmap image;
    private List<String> nameList;
    private List<Float> priceList;

    public Receipt(String productName, String businessName, float price, Date dateOfPurchase, Bitmap image) {
        ProductName = productName;
        this.businessName = businessName;
        this.price = price;
        this.dateOfPurchase = dateOfPurchase;
        this.image = image;
    }



    public Bitmap getImage() {
        return image;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public float getPrice() {
        return price;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public List<Float> getPriceList() {
        return priceList;
    }

    public void reset(){
        ProductName = null;
        businessName = null;
        image = null;
        dateOfPurchase = null;
        if(nameList != null) {
            nameList.clear();
            priceList.clear();
        }
    }

    public void reinitialize(String newPName, String newBName, float newPrice, Date newDate, Bitmap newPic){
        ProductName = newPName;
        businessName = newBName;
        price = newPrice;
        dateOfPurchase = newDate;
        image = newPic;
    }

    public void mergeBitmaps(Bitmap newImage){
        if(image == null){
            image = newImage;
        }
        else{
            Bitmap mergedImage = createBitmap(image.getWidth(), image.getHeight() + newImage.getHeight(), image.getConfig());
            //merge the images here
            Rect originalImageBounds = new Rect(0, 0, image.getWidth(), image.getHeight());
            Rect mergeIntoBounds = new Rect(0, image.getHeight(), image.getWidth(), image.getHeight() + newImage.getHeight());
            Canvas merging = new Canvas(mergedImage);
            merging.drawBitmap(image, null, originalImageBounds, null);
            merging.drawBitmap(newImage, null, mergeIntoBounds, null);
            image = mergedImage;
        }
    }

    public void addTags(){
        //add tags to list here, change method parameters as necessary
    }
}
