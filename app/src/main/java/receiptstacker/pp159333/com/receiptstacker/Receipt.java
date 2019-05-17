package receiptstacker.pp159333.com.receiptstacker;

import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.text.TextBlock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.StrictMath.max;

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
    public Receipt(SparseArray<TextBlock> OCR, Bitmap image){
        this.OCR = OCR;
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
}
