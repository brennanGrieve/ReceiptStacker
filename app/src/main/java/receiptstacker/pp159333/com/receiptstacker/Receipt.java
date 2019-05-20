package receiptstacker.pp159333.com.receiptstacker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.SparseArray;

import com.google.android.gms.vision.text.TextBlock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.StrictMath.max;
import java.util.List;

import static android.graphics.Bitmap.createBitmap;

/*
A class representing a Receipt.
totalPrice is the highest price on the receipt.
dateOfPurchase is the first date on the receipt.
bussinessName is the largest TextBlock on the receipt.
OCR is a SparseArray of TextBlocks that we can iterate through when searching for product names.
image is the same, a the image taken
 */

public class Receipt {
    private String bussinessName;
    private double totalPrice;
    private Date dateOfPurchase;
    private Bitmap image;
    SparseArray<TextBlock> OCR;

    //For creation after capture

    public Receipt(SparseArray<TextBlock> OCR, Bitmap image) {
        //this.price = price;
        //this.dateOfPurchase = dateOfPurchase;
        this.image = image;
        int key;
        double maxPrice = -1;
        this.OCR = OCR;
        Date testDate = new Date();
        Boolean dateCheck = true;
        Pattern price = Pattern.compile("\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})");
        Pattern date = Pattern.compile("^\\d{1,2}[///.\\s/t/-]\\d{1,2}[///.\\s/t /-]\\d{1,4}");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        int height = 0;
        int tempHeight = 0;
        int heightkey = -1;
        int numberOfLines;
        double currentPrice;
        String[] dateArr;
        for(int i = 0; i < OCR.size(); i++) {
            key = OCR.keyAt(i);
            TextBlock element = OCR.get(key);
            String testStr = element.getValue();
            Matcher priceMatcher = price.matcher(testStr);
            if(priceMatcher.find()){
                currentPrice = Double.parseDouble(priceMatcher.group(0));
                if(maxPrice<=currentPrice){
                    maxPrice = max(maxPrice, currentPrice);
                }
            }
            Matcher dateMatcher = date.matcher(testStr);
            if (dateMatcher.find()) {
                try {
                    dateArr = dateMatcher.group(0).split("[///.\\s/t/-]");
                    if(dateArr[2].length()<=2){

                    }
                    testDate = dateFormat.parse(dateMatcher.group(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (testDate != null && testDate.getYear() > 2000) {
                    dateOfPurchase = testDate;
                }
            }
            tempHeight = element.getBoundingBox().bottom -  element.getBoundingBox().top;
            numberOfLines = element.getValue().split("\n").length ;
            tempHeight = tempHeight / numberOfLines;
            if(element.getValue().split(" ").length>5){
                tempHeight = 0;
            }
            if (tempHeight > height){
                height = tempHeight;
                heightkey = key;
            }
        }
        if(heightkey != -1) {
            bussinessName = OCR.get(heightkey).getValue();
        }else{
            bussinessName = "Test name";
        }
        if(maxPrice != -1) {
            totalPrice = maxPrice;
        }else{
            totalPrice = -1;
        }
        if(dateOfPurchase == null){
            System.out.println("Date is null");
            dateOfPurchase = new Date(0);
        }
    }

    public Bitmap getImage() {
        return image;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public SparseArray<TextBlock> getOCR() {
        return OCR;
    }

    public void reinitialize(SparseArray<TextBlock> newOCR, Bitmap newImage) {
        OCR = newOCR;
        image = newImage;
    }

    public void reset(){
        //like this ?
        OCR = null;
        image =null;
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

}
