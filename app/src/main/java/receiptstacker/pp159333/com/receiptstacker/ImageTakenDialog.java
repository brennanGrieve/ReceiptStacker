package receiptstacker.pp159333.com.receiptstacker;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A dialog that shows a receipt for the user.
 * User can decide weather or not they want to take another photo.
 * This can be used in the browse menu as well with small changes to the button names.
 */
public class ImageTakenDialog{
    private Dialog dialog;
    private Context context;
    private Receipt receipt;
    private EditText pdate;
    private EditText pplace;
    private EditText pprice;
    //private EditText ptags;
    /**
     * Takes a context and a receipt and creates a custom dialog.
     * @param context the current context
     * @param receipt a receipt
     */
    ImageTakenDialog(Context context, Receipt receipt) {
        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_image_taken_dialog);
        this.context = context;
        this.receipt = receipt;
        populate(receipt);
    }

    /**
     * Method to create and display the dialog box
     */
    void showDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        final Bitmap expose = receipt.getImage();
        final Receipt receiptFinal = receipt;
        dialog.getWindow().setLayout(width-74, height-100);
        Button okaybutton = dialog.findViewById(R.id.button_keep);
        okaybutton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick
             * @param v the view
             */
            @Override
            public void onClick(View v) {
                if(pdate.getText() != null){
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
                        Date date = format.parse(String.valueOf(pdate.getText()));
                        receiptFinal.setDateOfPurchase(date);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                if(pprice.getText() != null){
                    try{
                        double amount = Double.parseDouble(String.valueOf(pprice.getText()));
                        receiptFinal.setHighestPrice(amount);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if(pplace.getText() != null){
                    try{
                        receiptFinal.setBusinessName(String.valueOf(pplace.getText()));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                saveReceiptToStorage(expose, receiptFinal);
                dialog.dismiss();
            }
        });
        Button tryagain = dialog.findViewById(R.id.button_TryAgain);
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Method to populate the dialog box
     * @param receipt a receipt
     */
    void populate(Receipt receipt){
        this.receipt = receipt;
        pdate = dialog.findViewById(R.id.textView_DateOfPurchase);
        pplace = dialog. findViewById(R.id.textView_PurchaseOrgin);
        pprice = dialog.findViewById(R.id.textview_Price);
        //ptags = dialog.findViewById(R.id.textView_Tags);
        ImageView image = dialog.findViewById(R.id.imageview_ReceiptImage);
        String price;
        price = String.format("%.02f", receipt.getHighestPrice());
        price = "$" + price;
        if(receipt.getDateOfPurchase() != null && !receipt.getDateOfPurchase().equals(new Date(0))) {
            pdate.setText(receipt.getDateOfPurchase().getDay() + "/" + receipt.getDateOfPurchase().getMonth() + "/" + receipt.getDateOfPurchase().getYear());
        }
        if(receipt.getBusinessName() != "") {
            pplace.setText(receipt.getBusinessName());
        }
        if (receipt.getHighestPrice()!=-1) {
            pprice.setText(price);
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(receipt.getImage(), 1080, 1920, false);
        image.setImageBitmap(scaledBitmap);
    }

    /**
     * Method that initializes the database if not already done and saves bitmap to storage
     * and the receipt to the database
     * @param b Bitmap to be saved
     * @param receiptFinal Receipt Object to be saved
     */
    private void saveReceiptToStorage(Bitmap b, Receipt receiptFinal){
        dbSingleton.initDB(context.getApplicationContext());
        receiptFinal.parseOCRToString();
        String filename = saveImageToFileSystem(b);
        dbSingleton.commitToDB(receiptFinal, filename);
    }

    /**
     * Method that saves the bitmap to a file as a JPEG
     * @param receiptPic a bitmap
     * @return String Filepath
     */
    private String saveImageToFileSystem(Bitmap receiptPic){
        ContextWrapper appWrap = new ContextWrapper(context.getApplicationContext());
        FileOutputStream receiptStream = null;
        File dir = appWrap.getDir("receiptImages", Context.MODE_PRIVATE);
        File newReceiptImage = new File(dir, getCurrentTimeStamp());
        try{
            receiptStream = new FileOutputStream(newReceiptImage);
            receiptPic.compress(Bitmap.CompressFormat.JPEG, 100, receiptStream);
        }catch(Exception FileOpenException){
            FileOpenException.printStackTrace();
        }finally{
            try{
                if (receiptStream != null) {
                    receiptStream.close();
                }
            }catch(IOException fileCloseException){
                fileCloseException.printStackTrace();
            }
        }
        return newReceiptImage.getAbsolutePath();
    }

    /**
     * Method that returns the current time stamp
     * @return String timeStamp
     */
    private String getCurrentTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String timeStamp = simpleDateFormat.format(new Date());
        return timeStamp;
    }
}
