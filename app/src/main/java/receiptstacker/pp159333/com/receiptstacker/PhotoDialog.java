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
        TextView priceView = dialog.findViewById(R.id.textView_Price);
        TextView dopView = dialog.findViewById(R.id.textView_DateOfPurchase);
        TextView productNameView = dialog.findViewById(R.id.textView_ProductName);
        TextView purchaseOriginView = dialog.findViewById(R.id.textView_PurchaseOrigin);
        TextView idView = dialog.findViewById(R.id.textView_Id);
        TextView pathView = dialog.findViewById(R.id.textView_Path);

        String price = "Price: $1.00";
        String dop = "Date of Purchase: 12/05/2018";
        String productName = "Product Name: KitKat";
        String purchaseOrigin = "Company Name: The Warehouse";
        imagePath = "Path: "+imagePath;
        String id = "Image ID: "+imageId;

        priceView.setText(price);
        dopView.setText(dop);
        productNameView.setText(productName);
        purchaseOriginView.setText(purchaseOrigin);
        pathView.setText(imagePath);
        idView.setText(id);


    }

}