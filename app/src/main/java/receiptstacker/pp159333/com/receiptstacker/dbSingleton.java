package receiptstacker.pp159333.com.receiptstacker;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import com.google.android.gms.vision.text.TextBlock;

import java.text.Format;
import java.text.SimpleDateFormat;

public class dbSingleton {

    private static SQLiteDatabase receiptDB;

    private dbSingleton(){}

    public static void initDB(Context appContext){
        if(receiptDB == null){
            receiptDB = appContext.openOrCreateDatabase("Receipts.db", Context.MODE_PRIVATE, null);

            receiptDB.execSQL("CREATE TABLE IF NOT EXISTS RECEIPT(" +
                    "R_IMAGE_PATH varchar2(100), " +
                    "R_BUSINESS_NAME varchar2(20), " +
                    "R_TOTAL_PRICE REAL," +
                    "R_PURCHASE_DATE DATE," +
                    "R_OCR_RAW_DATA varchar2(3000))");
        }
    }

    public static void commitToDB(Receipt inputReceipt, String imagePath){

        String rawOCRString = "";
        SparseArray<TextBlock> OCRitems;
        OCRitems = inputReceipt.getOCR();
        if(OCRitems != null) {
            if (OCRitems.size() != 0) {
                StringBuilder ocrBuilder = new StringBuilder();
                for (int i = 0; i < OCRitems.size(); i++) {
                    TextBlock item = OCRitems.valueAt(i);
                    ocrBuilder.append(item.getValue());
                    ocrBuilder.append(" ");
                }
                rawOCRString = ocrBuilder.toString();
            }
        }

        receiptDB.execSQL("INSERT INTO RECEIPT VALUES('" + imagePath + "', '"+ inputReceipt.getBusinessName() + "', '" + inputReceipt.getTotalPrice() + "','" + inputReceipt.getDateOfPurchase() + "','" + rawOCRString + "')");


        //}

    }

    //Currently Incomplete and unimplemented. May be completed in future

//    public static void dropFromDB(String imagePath){
//        receiptDB.execSQL("DROP FROM Receipts WHERE R_IMAGE_PATH = " + imagePath);
//    }

    //Change the return value and input parameters of the following methods when the search system is designed
    //May require multiple methods when

    public static String [] searchDB(String input){
        // change me later, when ocr is in the database
        int max = getNumberOfPhotos();
        String [] arrayOfPaths = new String[max+1]; //change the size of this
        int i =0;
        String sql = "SELECT DISTINCT R.R_IMAGE_PATH  FROM Receipt R WHERE  R_BUSINESS_NAME LIKE '%"+ input +"%' OR R_PURCHASE_DATE LIKE '%"+ input +"%'";
        if(receiptDB != null) {
            Cursor c = receiptDB.rawQuery(sql, new String[]{});
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    arrayOfPaths[i] = c.getString(0);
                    i++;
                } while (c.moveToNext());
                c.close();
            }
        }else{
            System.out.println("RECEIPTDB IS NULL");

        }
        return arrayOfPaths;

    }


    /***************************************
     * loadPhotos
     * gathers all the paths to the photos and returns them as an array of strings
     * @return arrayOfItems
     */
    public static String [] loadPhotos(){
        int max = getNumberOfPhotos();
        String [] arrayOfItems = new String[max+1]; //change the size of this
        int i =0;
        String sql = "SELECT R_IMAGE_PATH FROM Receipt WHERE R_IMAGE_PATH = R_IMAGE_PATH";
        if(receiptDB != null) {
            Cursor c = receiptDB.rawQuery(sql, new String[]{});
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    arrayOfItems[i] = c.getString(0);
                    i++;
                } while (c.moveToNext());
                c.close();
            }
        }
        return arrayOfItems;
    }

    public static int getNumberOfPhotos(){
        int numRows = (int)DatabaseUtils.longForQuery(receiptDB, "SELECT COUNT(*) FROM Receipt", null);
        return numRows;
    }

    public static String [] getData(String imagePath){
        String [] arrayOfPhotoInfo = new String [5];

        arrayOfPhotoInfo[0] = DatabaseUtils.stringForQuery(receiptDB, "SELECT R_BUSINESS_NAME FROM Receipt WHERE R_IMAGE_PATH = '"+imagePath+"'", null);
        System.out.println(arrayOfPhotoInfo[0]);
        //maybe need to change cuz its a date
        arrayOfPhotoInfo[1] = DatabaseUtils.stringForQuery(receiptDB, "SELECT R_PURCHASE_DATE FROM Receipt WHERE R_IMAGE_PATH = '"+imagePath+"'", null);
        System.out.println(arrayOfPhotoInfo[1]);
        //arrayOfPhotoInfo[2] = DatabaseUtils.stringForQuery(receiptDB, "SELECT P.P_PRODUCT_NAME FROM PRODUCT P, RECEIPT R WHERE R.R_IMAGE_PATH = '"+imagePath+"'", null);
        //arrayOfPhotoInfo[3] = DatabaseUtils.stringForQuery(receiptDB, "SELECT P.P_PRODUCT_PRICE FROM PRODUCT P, RECEIPT R WHERE R.R_IMAGE_PATH = '"+imagePath+"'", null);
        // System.out.println(arrayOfPhotoInfo[3]);
        return arrayOfPhotoInfo;
    }

}