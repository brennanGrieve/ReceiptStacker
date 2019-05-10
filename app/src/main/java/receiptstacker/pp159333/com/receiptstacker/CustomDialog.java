package receiptstacker.pp159333.com.receiptstacker;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*
A dialog that shows a receipt for the user. User can decide weather or not they want to
take another photo. This can be used in the browse menu as well with small changes to the button names.
 */

public class CustomDialog{

    private Dialog dialog;
    private Context context;

    // Takes a context and a receipt and creates a custom dialog.
    CustomDialog(Context context, Receipt receipt) {
        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_custom_dialog);
        this.context = context;
        populate(receipt);
    }


    void showDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);

        Button okaybutton = dialog.findViewById(R.id.button_keep);
        okaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Commit
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
        TextView pname = dialog.findViewById(R.id.textView_ProudctName);
        TextView pdate = dialog.findViewById(R.id.textView_DateOfPurchase);
        TextView pplace = dialog. findViewById(R.id.textView_PurchaseOrgin);
        TextView pprice = dialog.findViewById(R.id.textview_Price);
        ImageView image = dialog.findViewById(R.id.imageview_ReceiptImage);

        String price;
        price = String.format("%.02f", receipt.getPrice());
        price = "$" + price;

        pname.setText(receipt.getProductName());
        pdate.setText(receipt.getDateOfPurchase().toString());
        pplace.setText(receipt.getBussinessName());
        pprice.setText(price);
        image.setImageBitmap(receipt.getImage());
    }
}
