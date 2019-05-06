package receiptstacker.pp159333.com.receiptstacker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class dbSingleton {

    private static SQLiteDatabase receiptDB;

    private dbSingleton(){}

    public static void initDB(Context appContext){
        if(receiptDB == null){
            receiptDB = appContext.openOrCreateDatabase("Receipts.db", Context.MODE_PRIVATE, null);
            receiptDB.execSQL("CREATE TABLE IF NOT EXISTS Receipts(R_IMAGE_PATH varchar2(100), R_OCR_RAW_DATA varchar2(500))");
        }
    }

    public static void commitToDB(String imagePath, String OCRData){
        receiptDB.execSQL("INSERT INTO Receipts(R_IMAGE_PATH, R_OCR_RAW_DATA)VALUES('" + imagePath + "','" + OCRData + "')");
    }

    public static void dropFromDB(String imagePath){
        receiptDB.execSQL("DROP FROM Receipts WHERE R_IMAGE_PATH = " + imagePath);
    }

    //Change the return value and input parameters of the following methods when the search system is designed
    //May require multiple methods when

    public static void searchInDB(){

    }

}
