package receiptstacker.pp159333.com.receiptstacker;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.ScriptGroup;

import java.util.List;
import java.util.ListIterator;

public class dbSingleton {

    private static SQLiteDatabase receiptDB;

    private dbSingleton(){}

    public static void initDB(Context appContext){
        if(receiptDB == null){
            receiptDB = appContext.openOrCreateDatabase("Receipts.db", Context.MODE_PRIVATE, null);

            receiptDB.execSQL("CREATE TABLE IF NOT EXISTS RECEIPT(" +
                    "R_IMAGE_PATH varchar2(100), " +
                    "R_COMPANY_NAME varchar2(20), " +
                    "R_PURCHASE_DATE DATE)");
            //removed foreign key
            receiptDB.execSQL("CREATE TABLE IF NOT EXISTS PRODUCT(" +
                    "P_PRODUCT_NAME varchar(20), " +
                    "P_PRODUCT_PRICE FLOAT, " +
                    "FOREIGN KEY (R_IMAGE_PATH) REFERENCES RECEIPT(R_IMAGE_PATH))");
        }
    }

    public static void commitToDB(Receipt inputReceipt, String imagePath){
        receiptDB.execSQL("INSERT INTO RECEIPT VALUES('" + imagePath + "', '"+ inputReceipt.getBussinessName() + "', '"+inputReceipt.getDateOfPurchase()+"')");

        //for now just chucking in the "Skateboard" and total price [changes]
        //List<String> namesToInput = inputReceipt.getNameList();
        //List<Float> pricesToInput = inputReceipt.getPriceList();
        //if(namesToInput == null){
        //    return;
        //}
        //ListIterator<String> nameIterator = namesToInput.listIterator();
        //ListIterator<Float> priceIterator = pricesToInput.listIterator();
        //while(nameIterator.hasNext()){

            receiptDB.execSQL("INSERT INTO PRODUCT VALUES('" + "Skateboard" + "', '" + inputReceipt.getTotalPrice() + "', " + "'" + imagePath + "')");

        //}

    }

    public static void dropFromDB(String imagePath){
        receiptDB.execSQL("DROP FROM Receipts WHERE R_IMAGE_PATH = " + imagePath);
    }

    //Change the return value and input parameters of the following methods when the search system is designed
    //May require multiple methods when

    public static String [] searchDB(String input){
        // change me later, when ocr is in the database
        int max = getNumberOfPhotos();
        String [] arrayOfItems = new String[max+1]; //change the size of this
        int i =0;
        String sql = "SELECT DISTINCT R.R_IMAGE_PATH  FROM Receipt R, PRODUCT P WHERE P.P_PRODUCT_NAME LIKE '%"+ input +"%' OR P.P_PRODUCT_PRICE LIKE '%"+ input +"%' OR R.R_COMPANY_NAME LIKE '%"+ input +"%' OR R.R_PURCHASE_DATE LIKE '%"+ input +"%'";
        if(receiptDB != null) {
            Cursor c = receiptDB.rawQuery(sql, new String[]{});
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    System.out.println("ROW[" + i + "]: " + c.getString(0));
                    arrayOfItems[i] = c.getString(0);
                    i++;
                } while (c.moveToNext());
                c.close();
            }
        }else{
            System.out.println("RECEIPTDB IS NULL");

        }
        return arrayOfItems;

    }


    /***************************************
     * loadPhotos
     * gathers all the paths to the photos and returns them as an array of strings
     * @return arrayOfItems
     */
    public static String [] loadPhotos(){
        String [] arrayOfItems = new String[50]; //change the size of this
        int i =0;
        String sql = "SELECT R_IMAGE_PATH FROM Receipt WHERE R_IMAGE_PATH = R_IMAGE_PATH";
        if(receiptDB != null) {
            Cursor c = receiptDB.rawQuery(sql, new String[]{});
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    System.out.println("ROW[" + i + "]: " + c.getString(0));
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

        arrayOfPhotoInfo[0] = DatabaseUtils.stringForQuery(receiptDB, "SELECT R_COMPANY_NAME FROM Receipt WHERE R_IMAGE_PATH = '"+imagePath+"'", null);
        System.out.println(arrayOfPhotoInfo[0]);
        //maybe need to change cuz its a date
        arrayOfPhotoInfo[1] = DatabaseUtils.stringForQuery(receiptDB, "SELECT R_PURCHASE_DATE FROM Receipt WHERE R_IMAGE_PATH = '"+imagePath+"'", null);
        System.out.println(arrayOfPhotoInfo[1]);
        arrayOfPhotoInfo[2] = DatabaseUtils.stringForQuery(receiptDB, "SELECT P.P_PRODUCT_NAME FROM PRODUCT P, RECEIPT R WHERE R.R_IMAGE_PATH = '"+imagePath+"'", null);
        arrayOfPhotoInfo[3] = DatabaseUtils.stringForQuery(receiptDB, "SELECT P.P_PRODUCT_PRICE FROM PRODUCT P, RECEIPT R WHERE R.R_IMAGE_PATH = '"+imagePath+"'", null);
       // System.out.println(arrayOfPhotoInfo[3]);
        return arrayOfPhotoInfo;
    }

}
