package receiptstacker.pp159333.com.receiptstacker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;

/*
A dialog that shows a receipt for the user. User can decide weather or not they want to
take another photo. This can be used in the browse menu as well with small changes to the button names.
 */

public class ImageTakenDialog{

    private Dialog dialog;
    private Context context;
    private Receipt receipt;

    Boolean priceChange;
    Boolean dateChange;
    Boolean bussinessChange;
    Boolean descriptionChange;

    EditText pdate;
    EditText pplace;
    EditText pprice;
    EditText ptags;
    ImageView pimage;

    // Takes a context and a receipt and creates a custom dialog.
    ImageTakenDialog(Context context, Receipt receipt) {
        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_image_taken_dialog);
        this.context = context;
        this.receipt = receipt;
        priceChange = false;
        dateChange = false;
        bussinessChange = false;
        descriptionChange = false;
        populate(receipt);
    }


    void showDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        final Bitmap expose = receipt.getImage();
        final Receipt receiptFinal = receipt;
        //dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);
        dialog.getWindow().setLayout(width-74, height-100);

        Button okaybutton = dialog.findViewById(R.id.button_keep);
        okaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReceiptToStorage(expose, receiptFinal);
                /*if(dateChange){
                    try {
                        receipt.setDateOfPurchase(new Date(String.valueOf(pdate.getText())));
                    }catch (Exception e){
                        //Deal with wrong date format
                    }
                }
                if(priceChange){
                    try{
                       receipt.setHighestPrice(Double.parseDouble(String.valueOf(pprice.getText())));
                    } catch (Exception e) {

                    }
                }
                if(bussinessChange){
                    receipt.setBusinessName(String.valueOf(pplace.getText()));
                }
                if(descriptionChange){

                }*/
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

    void populate(Receipt receipt){
        this.receipt = receipt;
        pdate = dialog.findViewById(R.id.textView_DateOfPurchase);
        pplace = dialog. findViewById(R.id.textView_PurchaseOrgin);
        pprice = dialog.findViewById(R.id.textview_Price);
        ptags = dialog.findViewById(R.id.textView_Tags);
        ImageView image = dialog.findViewById(R.id.imageview_ReceiptImage);
        ConstraintLayout c = dialog.findViewById(R.id.ConstraintLayout_Main);

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
        /*
        pdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChange = true;
            }
        });
        pprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceChange = true;
            }
        });
        pplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bussinessChange = true;
            }
        });
        ptags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 descriptionChange = true;
            }
        });*/



        //Bitmap scaledBitmap = Bitmap.createScaledBitmap(receipt.getImage(), 1080, 1920, false);
        Bitmap test = receipt.getImage();
        image.setImageBitmap(test);
    }

    private void saveReceiptToStorage(Bitmap toSave, Receipt receiptFinal){
        dbSingleton.initDB(context.getApplicationContext());
        String filename = saveImageToFileSystem(toSave);
        //This method will need to be changed once saveReceiptToStorage handles more then 1 string.
        dbSingleton.commitToDB(receiptFinal, filename);
    }

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
        Log.d(TAG, "saveImageToFileSystem: File Path is:" + newReceiptImage.getAbsolutePath());
        return newReceiptImage.getAbsolutePath();
    }



    private String getCurrentTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String timeStamp = simpleDateFormat.format(new Date());
        return timeStamp;
    }
}
