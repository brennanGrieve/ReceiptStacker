package receiptstacker.pp159333.com.receiptstacker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



/*
A dialog that shows a receipt for the user. User can decide weather or not they want to
take another photo. This can be used in the browse menu as well with small changes to the button names.
 */

public class PhotoDialog{

    private Dialog dialog;
    private Context context;
    // could send receipt if you wanted to
    private String name;

    // Takes a context and a receipt and creates a custom dialog.
    PhotoDialog(Context context, String name, String imagePath, int id) {
        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_photo_dialog);
        this.context = context;
        populate(name, imagePath, id);
    }


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

    private void populate(String OCR, String imagePath, int imageId){
        OCR = "";
        // add more to this later
        String [] arr = dbSingleton.getData(imagePath);

        TextView priceView = dialog.findViewById(R.id.textView_Price);
        TextView dopView = dialog.findViewById(R.id.textView_DateOfPurchase);
        TextView productNameView = dialog.findViewById(R.id.textView_ProductName);
        TextView purchaseOriginView = dialog.findViewById(R.id.textView_PurchaseOrigin);
        TextView idView = dialog.findViewById(R.id.textView_Id);
        TextView pathView = dialog.findViewById(R.id.textView_Path);
        String purchaseOrigin;
        String price;
        String dop;
        String productName;
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
            productName = "Unknown";
        }else{
            productName = arr[2];
        }
        if(arr[3] == null){
            price = "Unknown";
        }else{
            price = arr[3];
        }
        
        String id = ""+imageId;

        priceView.setText(price);
        dopView.setText(dop);
        productNameView.setText(productName);
        purchaseOriginView.setText(purchaseOrigin);
        pathView.setText(imagePath);
        idView.setText(id);


    }

}