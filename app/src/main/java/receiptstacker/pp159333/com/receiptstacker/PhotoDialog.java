package receiptstacker.pp159333.com.receiptstacker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Photo Dialog
 * a class that displays a photo dialog for receipts
 */
public class PhotoDialog{
    private Dialog dialog;      // the dialog
    private Context context;    // the current context

    /**
     * PhotoDialog constructor
     * @param context the context
     * @param imagePath the receipts image path
     * @param id the id of the image
     */
    PhotoDialog(Context context, String imagePath, int id) {
        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_photo_dialog);
        this.context = context;
        populate(imagePath, id);
    }

    /**
     * showDialog
     * a function used to create the dialog box and the close dialog button
     */
    void showDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if(dialog.getWindow() != null) {
            dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
        }

        Button closeButton = dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * populate
     * a function used to populate all the information for the dialog box.
     * @param imagePath the image path
     * @param imageId the image id
     */
    private void populate(String imagePath, int imageId){
        String [] arr = dbSingleton.getData(imagePath);
        TextView priceView = dialog.findViewById(R.id.textView_TotalPrice);
        TextView dopView = dialog.findViewById(R.id.textView_DateOfPurchase);
        TextView purchaseOriginView = dialog.findViewById(R.id.textView_PurchaseOrigin);
        TextView OCRView = dialog.findViewById(R.id.textView_OCR);
        TextView idView = dialog.findViewById(R.id.textView_Id);
        TextView pathView = dialog.findViewById(R.id.textView_Path);
        String purchaseOrigin;
        String dop;
        String totalPrice;
        String OCRText;
        if(arr[0] == null) {
            purchaseOrigin = "Unknown";
        }else{
            purchaseOrigin = arr[0];
        }

        if(arr[1] == null){
            dop = "Unknown";
        }else{
            dop = arr[1];
        }

        if(arr[2] == null){
            totalPrice = "Unknown";
        }else{
            totalPrice = arr[2];
        }
        if(arr[3] == null){
            OCRText = "Unknown";
        }else{
            OCRText = arr[3];
        }
        String id = ""+imageId;
        dopView.setText(dop);
        purchaseOriginView.setText(purchaseOrigin);
        pathView.setText(imagePath);
        idView.setText(id);
        priceView.setText(totalPrice);
        OCRView.setText(OCRText);
    }
}