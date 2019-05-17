package receiptstacker.pp159333.com.receiptstacker;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BrowseFragment extends Fragment {
    Button searchButton, searchByButton;
    int max = dbSingleton.getNumberOfPhotos();
    int[] arrayOfIds = new int[max];

    public static BrowseFragment newInstance() {
        return new BrowseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }
    public static Bitmap decodeFile(File f,int WIDTH,int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String[] allImagePaths;
        File newImage;

        ImageView[] imageViews = new ImageView[1000]; // change me
        LinearLayout[] layouts = new LinearLayout[1000]; // change me
        allImagePaths = dbSingleton.loadPhotos();
        LinearLayout verticalLayout = view.findViewById(R.id.verticalLayout);
        Bitmap b = null;
        int layoutId = 0;

        int layoutNumber = 0;
        for (int i = 0; i < max; i++) {
            System.out.println(allImagePaths[i]);
            if (allImagePaths[i] != null) {
                newImage = new File(allImagePaths[i]);
            } else {
                newImage = null;
            }
            if (newImage != null) {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inSampleSize = 8; // scales down the size kinda haha
                b = BitmapFactory.decodeFile(newImage.getAbsolutePath(), o);
                imageViews[i] = new ImageView(getActivity());
                imageViews[i].setImageBitmap(b);
                imageViews[i].setId(View.generateViewId());
                int imageId = imageViews[i].getId();
                arrayOfIds[i] = imageId;
                //create a new horizontal layout for every 3 photos
                if (i % 3 == 0) {
                    //add a new layout to the scrollView
                    layouts[layoutNumber] = new LinearLayout(getActivity());
                    verticalLayout.addView(layouts[layoutNumber]);
                    layouts[layoutNumber].setId(View.generateViewId());
                    layoutId = layouts[layoutNumber].getId();
                    layoutNumber++;
                }

                //add the image to the layout
                LinearLayout currentLayout = view.findViewById(layoutId);
                currentLayout.addView(imageViews[i]);
                currentLayout.setPadding(0, 5, 0, 5);
                currentLayout.setY(0);
                currentLayout.setX(0);

                //resize the image
                ImageView currentImage = view.findViewById(imageId);
                //currently 3:4
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                currentImage.getLayoutParams().width = dm.widthPixels/3; //change me
                currentImage.getLayoutParams().height = 150; // change me

                //currentImage.setRotation(90);
                currentImage.setY(0);
                currentImage.setX(0);
                //currentImage.setPadding(0, 25, 0, 25);
            } else {
                System.out.println("Cant load image " + i);
            }
        }


        searchButton = (Button) view.findViewById(R.id.searchBtn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add the search stuff here.
            }
        });
        searchByButton = (Button) view.findViewById(R.id.searchByBtn);
        searchByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add code for select search by stuff
            }
        });

        ImageView []allImages = new ImageView[max];
        for (int indx = 0; indx < max; indx++) {
            allImages[indx] = (ImageView) view.findViewById(arrayOfIds[indx]);
            final int finalIndx = indx;
            allImages[indx].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent photoIntent = new Intent(getActivity(), PictureActivity.class);
                    // this may work haha
                    // this could be used to send across OCR data
                    photoIntent.putExtra("path",allImagePaths[finalIndx]);
                    startActivity(photoIntent);
                    // add the image to the activity

                }
            });
        }
    }

}