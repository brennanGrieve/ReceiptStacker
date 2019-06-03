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

import static android.graphics.Bitmap.createBitmap;

/**
 * A class representing a Receipt.
 * highestPrice is the highest price on the receipt.
 * dateOfPurchase is the first date on the receipt.
 * businessName is the largest TextBlock on the receipt.
 * OCR is a SparseArray of TextBlocks that we can iterate through when searching for product names.
 * image is the same, a the image taken
 * customTag is a Tag entered in by the user
 */

public class Receipt {


    private String businessName;
    private double highestPrice;
    private Date dateOfPurchase;
    private Bitmap image;
    SparseArray<TextBlock> textBlockOCR;
    String stringOCR;
    String customTag;

    /**
     * Public Constructor for Receipt Object. Takes Parameters to assign to Receipt Attributes.
     * @param newOCR New array of OCR TextBlocks
     * @param image New Bitmap image of Receipt
     */

    public Receipt(SparseArray<TextBlock> newOCR, Bitmap image) {
        this.textBlockOCR = newOCR;
        this.image = image;
        this.customTag = null;
        updateData();
    }

    /**
     * Image Getter.
     * @return Bitmap image of Receipt
     */

    public Bitmap getImage() {
        return image;
    }

    /**
     * Business Name Getter
     * @return Receipt Business Name
     */

    public String getBusinessName() {
        return businessName;
    }

    /**
     * Highest Price Getter
     * @return Receipt Highest Price
     */

    public double getHighestPrice() {
        return highestPrice;
    }

    /**
     * Date of Purchase Getter
     * @return Receipt Date of Purchase
     */

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    /**
     * OCR Array Getter
     * @return Receipt OCR Array
     */

    public SparseArray<TextBlock> getTextBlockOCR() {
        return textBlockOCR;
    }

    /**
     * BusinessName Setter.
     * @param businessName New value for businessName
     */

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * Highest Price Setter
     * @param highestPrice New value for highestPrice
     */

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    /**
     * Date of Purchase Setter
     * @param dateOfPurchase New value for dateOfPurchase
     */

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    /**
     * Image Setter
     * @param image New Receipt Image to be stored.
     */

   // public void setImage(Bitmap image) {
    //    this.image = image;
    //}

    /**
     * OCR Setter.
     * @param textBlockOCR New SparseArray of TextBlocks to be stored.
     */

    //public void setTextBlockOCR(SparseArray<TextBlock> textBlockOCR) {
    //    this.textBlockOCR = textBlockOCR;
    //}

    /**
     * Function to derive metadata from OCR readings.
     * This is run during the Constructor in order to parse the OCR string for data of interest
     * Such as Date of Purchase and Business Name.
     */

    public void updateData(){
        int key;
        double maxPrice = -1;
        Date testDate = new Date();
        Pattern price = Pattern.compile("\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})");
        Pattern date = Pattern.compile("^\\d{1,2}[///.\\s/t/-]\\d{1,2}[///.\\s/t /-]\\d{1,4}");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        int height = 0;
        int tempHeight = 0;
        int heightkey = -1;
        int numberOfLines;
        double currentPrice;
        String[] dateArr;
        if(textBlockOCR != null) {
            for (int i = 0; i < textBlockOCR.size(); i++) {
                key = textBlockOCR.keyAt(i);
                TextBlock element = textBlockOCR.get(key);
                String testStr = element.getValue();
                Matcher priceMatcher = price.matcher(testStr);
                if (priceMatcher.find()) {
                    currentPrice = Double.parseDouble(priceMatcher.group(0).replace(',', '.'));
                    if (maxPrice <= currentPrice) {
                        maxPrice = max(maxPrice, currentPrice);
                    }
                }
                Matcher dateMatcher = date.matcher(testStr);
                if (dateMatcher.find()) {
                    try {
                        dateArr = dateMatcher.group(0).split("[///.\\s/t/-]");
                        if (dateArr[2].length() <= 2) {

                        }
                        testDate = dateFormat.parse(dateMatcher.group(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (testDate != null && testDate.getYear() > 2000) {
                        dateOfPurchase = testDate;
                    }
                }
                tempHeight = element.getBoundingBox().bottom - element.getBoundingBox().top;
                numberOfLines = element.getValue().split("\n").length;
                tempHeight = tempHeight / numberOfLines;
                if (element.getValue().split(" ").length > 5) {
                    tempHeight = 0;
                }
                if (tempHeight > height) {
                    height = tempHeight;
                    heightkey = key;
                }
            }
        }
        if(heightkey != -1) {
            businessName = textBlockOCR.get(heightkey).getValue();
        }else{
            businessName = "";
        }
        if(maxPrice != -1) {
            highestPrice = maxPrice;
        }else{
            highestPrice = -1;
        }
        if(dateOfPurchase == null){
            dateOfPurchase = new Date(0);
        }
    }

    /**
     * Called as a Surrogate Constructor of sorts to reinitialize the multi-capture object
     * For when a new multi-capture is begun.
     * @param newOCR New OCR Data to initialize the Multi-Capture Receipt with
     * @param newImage New Receipt Image bitmap to initialize the Multi-Capture Receipt with
     */


    public void reinitialize(SparseArray<TextBlock> newOCR, Bitmap newImage) {
        this.textBlockOCR = newOCR;
        this.image = newImage;
        updateData();
    }

    /**
     * Method to update stored Receipt attributes derived from OCR data when extra OCR data is added
     * to a Multi-Capture Receipt.
     */

    public void updateDynamicDerivedValues(){
        int key;
        double maxPrice = -1;
        double currentPrice;
        Pattern price = Pattern.compile("\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})");
        for(int i = 0; i < textBlockOCR.size(); i++) {
            key = textBlockOCR.keyAt(i);
            TextBlock element = textBlockOCR.get(key);
            String testStr = element.getValue();
            Matcher priceMatcher = price.matcher(testStr);
            if(priceMatcher.find()){
                currentPrice = Double.parseDouble(priceMatcher.group(0));
                if(maxPrice<=currentPrice){
                    maxPrice = max(maxPrice, currentPrice);
                }
            }
        }
    }

    /**
     * Sets Receipt OCR and image to null.
     */

    public void reset(){
        //like this ?
        textBlockOCR = null;
        image = null;
    }

    /**
     * Takes a captured bitmap image and concatenates it onto the existing bitmap in the Receipt object.
     * This is done by taking the bounds of the 2 bitmaps, and creating a canvas to draw both bitmaps onto.
     * @param newImage Bitmap image of subsequent camera capture to be concatenated to the existing Receipt Bitmap
     */

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

    /**
     * Concatenates OCR Data taken from subsequent shots in a multi-capture.
     * @param newOCR Subsequent Capture OCR Data to be concatenated to existing Receipt OCR Data
     */

    public void addNewOCR(SparseArray<TextBlock> newOCR){
        int newKeySequence = textBlockOCR.size();
        newKeySequence++;
        if(newOCR != null){
            for(int i=0; i < newOCR.size(); i++){
                textBlockOCR.put(newKeySequence, newOCR.valueAt(i));
                newKeySequence++;
            }
            updateDynamicDerivedValues();
        }
    }

    /**
     * Getter for stringOCR attribute.
     * @return stringOCR - Receipt OCR Data in String Form.
     */

    public String getStringOCR(){return stringOCR;}

    /**
     * Method that parses the textBlockOCR and customTag attribute into the stringOCR attribute by means of a StringBuilder.
     * Takes no Parameters, has no return value.
     */

    public void parseOCRToString(){
        if(textBlockOCR != null) {
            if (textBlockOCR.size() != 0) {
                StringBuilder ocrBuilder = new StringBuilder();
                for (int i = 0; i < textBlockOCR.size(); i++) {
                    TextBlock item = textBlockOCR.valueAt(i);
                    ocrBuilder.append(item.getValue());
                    ocrBuilder.append(" ");
                }
                if(customTag != null){
                    ocrBuilder.append("\n");
                    ocrBuilder.append(customTag);
                }
                stringOCR = ocrBuilder.toString();
            }
        } else
            if (customTag != null){
                stringOCR = customTag;
        }
    }

    /**
     * customTag Setter.
     * @param tag new value of customTag.
     */
    
    public void setcustomTag(String tag){
        customTag = tag;
    }
}
