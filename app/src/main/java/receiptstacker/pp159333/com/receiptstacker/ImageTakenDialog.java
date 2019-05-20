package receiptstacker.pp159333.com.receiptstacker;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;

/*
A dialog that shows a receipt for the user. User can decide weather or not they want to
take another photo. This can be used in the browse menu as well with small changes to the button names.
 */

public class ImageTakenDialog {

    private Dialog dialog;
    private Context context;
    private final Receipt receipt;

    // Takes a context and a receipt and creates a custom dialog.
    ImageTakenDialog(Context context, Receipt receipt) {
        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_image_taken_dialog);
        this.context = context;
        this.receipt = receipt;
        populate(receipt);
        Bitmap exposebmp = receipt.getImage();
    }


    void showDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        final Bitmap expose = receipt.getImage();
        dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);

        Button okaybutton = dialog.findViewById(R.id.button_keep);
        okaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReceiptToStorage(expose);

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
        TextView pdate = dialog.findViewById(R.id.textView_DateOfPurchase);
        TextView pplace = dialog. findViewById(R.id.textView_PurchaseOrgin);
        TextView pprice = dialog.findViewById(R.id.textview_Price);
        ImageView image = dialog.findViewById(R.id.imageview_ReceiptImage);

        String price;
        price = String.format("%.02f", receipt.getTotalPrice());
        price = "$" + price;

        //pname.setText(receipt.getProductName());
        //[changes]
        if(receipt.getDateOfPurchase() != null) {
            pdate.setText(receipt.getDateOfPurchase().getDay() + "/" + receipt.getDateOfPurchase().getMonth() + "/" + receipt.getDateOfPurchase().getYear());
        }
        pplace.setText(receipt.getBusinessName());
        pprice.setText(price);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(receipt.getImage(), 150, 150, false);
        image.setImageBitmap(scaledBitmap);
    }

    private void saveReceiptToStorage(Bitmap toSave){
        dbSingleton.initDB(context.getApplicationContext());
        String filename = saveImageToFileSystem(toSave);
        //This method will need to be changed once saveReceiptToStorage handles more then 1 string.
        dbSingleton.commitToDB(receipt, filename);
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
