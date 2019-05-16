package receiptstacker.pp159333.com.receiptstacker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.android.gms.vision.text.TextBlock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
<<<<<<< HEAD
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.StrictMath.max;
=======
import java.util.List;

import static android.graphics.Bitmap.createBitmap;
>>>>>>> 00348f05ded7312faaa5ca8be12e3573df6eb3f9

/*
A class representing a Receipt.
totalPrice is the highest price on the receipt.
dateOfPurchase is the first date on the receipt.
bussinessName is the largest TextBlock on the receipt.
OCR is a SparseArray of TextBlocks that we can iterate through when searching for product names.
image is the same, a the image taken
 */

public class Receipt {
<<<<<<< HEAD
    private String bussinessName;
    private double totalPrice;
    private Date dateOfPurchase;
    private Bitmap image;
    SparseArray<TextBlock> OCR;

    //For creation after capture
    public Receipt(SparseArray<TextBlock> OCR, Bitmap image){
        this.OCR = OCR;
=======
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
>>>>>>> 00348f05ded7312faaa5ca8be12e3573df6eb3f9
        this.image = image;
        int key;
        double maxPrice = -1;
        Date testDate = new Date();
        Boolean dateCheck = true;
        Pattern price = Pattern.compile("\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})");
        Pattern date = Pattern.compile("^\\d{1,2}[///./:\\s/t/-]\\d{1,2}[///./:\\s/t /-]\\d{1,4}");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        int height = 0;
        int tempHeight = 0;
        int heightkey = -1;

        for(int i = 0; i < OCR.size(); i++) {
            key = OCR.keyAt(i);
            TextBlock element = OCR.get(key);
            String testStr = element.getValue();
            Matcher priceMatcher = price.matcher(testStr);
            if(priceMatcher.find()){
                System.out.print("Price: ");
                System.out.print(priceMatcher.group(0)+"\n");
                double currentPrice = Double.parseDouble(priceMatcher.group(0));
                if(maxPrice<=currentPrice){
                    maxPrice = max(maxPrice, currentPrice);
                }
            }
            if(dateCheck) {
                Matcher dateMatcher = date.matcher(testStr);
                if (dateMatcher.find()) {
                    System.out.print("Date: ");
                    System.out.print(dateMatcher.group(0) + "\n");
                    try {
                        testDate = dateFormat.parse(dateMatcher.group(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (testDate != null) {
                        dateOfPurchase = testDate;
                        dateCheck = false;
                    }
                }
            }
            tempHeight = element.getBoundingBox().bottom -  element.getBoundingBox().top ;
            if (tempHeight > height){
                height = tempHeight;
                heightkey = key;
            }
        }
        if(heightkey != -1) {
            bussinessName = OCR.get(heightkey).getValue();
        }
        if(maxPrice != -1) {
            totalPrice = maxPrice;
        }
    }



    public Bitmap getImage() {
        return image;
    }


<<<<<<< HEAD
    public String getBussinessName() {
        return bussinessName;
=======
    public String getBusinessName() {
        return businessName;
>>>>>>> 00348f05ded7312faaa5ca8be12e3573df6eb3f9
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }
<<<<<<< HEAD
    public SparseArray<TextBlock> getOCR() {
        return OCR;
=======

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
>>>>>>> 00348f05ded7312faaa5ca8be12e3573df6eb3f9
    }
}
