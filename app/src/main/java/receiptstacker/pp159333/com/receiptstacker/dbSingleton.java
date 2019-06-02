package receiptstacker.pp159333.com.receiptstacker;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import com.google.android.gms.vision.text.TextBlock;

/**
 * Singleton class providing code for Database interaction.
 */

public class dbSingleton {

    /**
     * Private static SQLiteDatabase object.
     * Only ever exists once, in line with the Singleton design pattern.
     */
    private static SQLiteDatabase receiptDB;

    /**
     * Private Singleton Constructor.
     * Called by initDB if the database is not yet opened.
     */

    private dbSingleton(){}

    /**
     * Opens an instance of the database if one does not already exists.
     * Also uses an SQLQuery to create the database schema, if it doesn't already exist.
     * @param appContext Application context, required to open Database.
     */

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

    /**
     * Method to commit a new Receipt to the database.
     * Takes in a Receipt object, and the file path of the Receipt image now saved to the Filesystem.
     * @param inputReceipt Receipt containing data to be stored in Database.
     * @param imagePath File Path of the Receipt image that has been stored on the filesystem.
     */

    public static void commitToDB(Receipt inputReceipt, String imagePath){

        receiptDB.execSQL("INSERT INTO RECEIPT VALUES('" + imagePath + "', '"+ inputReceipt.getBusinessName() + "', '" + inputReceipt.getHighestPrice() + "','" + inputReceipt.getDateOfPurchase() + "','" + inputReceipt.getStringOCR() + "')");

    }

    /**
     * Function to search through the database.
     * @param input
     * @return
     */

    //Change the return value and input parameters of the following methods when the search system is designed
    //May require multiple methods when

    public static String [] searchDB(String input){
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


    /**
     * Method that gathers all the paths to the photos and returns them as an array of strings.
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

    /**
     * Function to return the number of photos stored in the database.
     * @return Number of photos to existing in the database to be read.
     */

    public static int getNumberOfPhotos(){
        int numRows = (int)DatabaseUtils.longForQuery(receiptDB, "SELECT COUNT(*) FROM Receipt", null);
        return numRows;
    }

    /**
     * Function to fetch data from the database.
     * @param imagePath Used as the primary key to fetch a specific set of data from the Database.
     * @return arrayOfPhotoInfo Array of Strings representing information about the Receipt stored in the database.
     */

    public static String [] getData(String imagePath){
        String [] arrayOfPhotoInfo = new String [5];

        arrayOfPhotoInfo[0] = DatabaseUtils.stringForQuery(receiptDB, "SELECT R_BUSINESS_NAME FROM Receipt WHERE R_IMAGE_PATH = '"+imagePath+"'", null);
        arrayOfPhotoInfo[1] = DatabaseUtils.stringForQuery(receiptDB, "SELECT R_PURCHASE_DATE FROM Receipt WHERE R_IMAGE_PATH = '"+imagePath+"'", null);
        arrayOfPhotoInfo[2] = DatabaseUtils.stringForQuery(receiptDB, "SELECT R_TOTAL_PRICE FROM RECEIPT WHERE R_IMAGE_PATH = '"+imagePath+"'", null);
        arrayOfPhotoInfo[3] = DatabaseUtils.stringForQuery(receiptDB, "SELECT R_OCR_RAW_DATA FROM RECEIPT WHERE R_IMAGE_PATH = '"+imagePath+"'", null);
        return arrayOfPhotoInfo;
    }

}