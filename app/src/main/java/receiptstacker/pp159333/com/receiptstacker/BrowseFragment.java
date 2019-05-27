package receiptstacker.pp159333.com.receiptstacker;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BrowseFragment extends Fragment {
    Button searchButton;
    EditText input;
    int[] arrayOfIds;
    String[] allImagePaths;
    LinearLayout[] layouts;
    int[] layoutIds;
    ImageView[] imageViews;
    int numberDisplayed = 0;
    ImageView[] displayedViews;
    int amount = 0;
    TextView loadingText;
    Bitmap [] arrayOfBitmaps;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageThread();
    }
    public void updatePhotos(String [] arrayOfPaths, View v){
        int l;
        if(numberDisplayed == 0) {
            for (int indx = 0; indx < arrayOfIds.length; indx++) {
                l = indx / 4;
                System.out.println("indx = " + indx + " l = " + l);
                System.out.println("lay id = " + layoutIds[l]);
                layouts[l].removeView(imageViews[indx]);
            }
        }else{
            for (int indx = 0; indx < numberDisplayed; indx++) {
                l = indx / 3;
                layouts[l].removeView(displayedViews[indx]);
            }
        }




        numberDisplayed = 0;
        for(int i =0; i< arrayOfPaths.length; i++){
            System.out.println("path "+i+arrayOfPaths[i]);
            for(int j = 0; j<arrayOfIds.length; j++){
                if(allImagePaths[j].equals(arrayOfPaths[i])){
                    l = i/4;
                    //ImageView imgV = v.findViewById(arrayOfIds[j]);
                    //LinearLayout layout = v.findViewById(layoutIds[l]); // haha i shouldnt be doing this :P
                    System.out.println("l = "+ l+ " i = "+ i );

                    try {
                        layouts[l].addView(imageViews[j]);
                        displayedViews[numberDisplayed] = imageViews[j];
                        numberDisplayed++;
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }


    //[CHANGE ME]
    //https://developer.android.com/topic/performance/graphics/load-bitmap#java
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //[CHANGE ME]
    // https://developer.android.com/topic/performance/graphics/load-bitmap#java
    public static Bitmap decodeSampledBitmapFromFile(String file,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    void imageThread(){
        int max = dbSingleton.getNumberOfPhotos();
        arrayOfBitmaps = new Bitmap[max];
        arrayOfIds  = new int[max];
        int layoutMax = (max/3)+1;
        final File[] newImage = new File[max];
        layouts = new LinearLayout[layoutMax];
        layoutIds = new int[layoutMax];
        imageViews = new ImageView[max];
        displayedViews = new ImageView[max];

        allImagePaths = dbSingleton.loadPhotos();
        LinearLayout verticalLayout = getView().findViewById(R.id.verticalLayout);
        int layoutNumber = -1;
        loadingText = getView().findViewById(R.id.loadingText);
        loadingText.setText("LOADING: "+1+"/"+max);

        for (int i = 0; i < max; i++) {
            if (allImagePaths[i] != null) {
                newImage[i] = new File(allImagePaths[i]);
            } else {
                newImage[i] = null;
            }
        }
        final File[] finalNewImage = newImage;
        //final int finalIndex = i;
        final int finalMax = max;
        final BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 24;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< finalMax; i++) {
                    arrayOfBitmaps[i] = decodeSampledBitmapFromFile(finalNewImage[i].getAbsolutePath(), 100, 100);
                    //arrayOfBitmaps[i] = BitmapFactory.decodeFile(finalNewImage[i].getAbsolutePath(), o);
                    final int finalIndex = i;
                    imageViews[i].post(new Runnable() {
                        @Override
                        public void run() {
                            imageViews[finalIndex].setImageBitmap(arrayOfBitmaps[finalIndex]);
                            amount+=1;
                            if(amount == finalMax){
                                try {
                                    wait(finalMax*100);
                                }catch(Exception e){
                                    System.out.println(e.getMessage());
                                }
                                loadingText.setText("");
                            }else {
                                loadingText.setText("LOADING: " + amount + "/" + finalMax);
                            }
                        }
                    });
                }


            }
        }).start();

        for (int i = 0; i < max; i++) {
            System.out.println(allImagePaths[i]);
            if (newImage != null) {
                imageViews[i] = new ImageView(getActivity());
                imageViews[i].setId(View.generateViewId());
                int imageId = imageViews[i].getId();
                arrayOfIds[i] = imageId;
                //create a new horizontal layout for every 3 photos

                if (i % 4 == 0) {
                    //add a new layout to the scrollView
                    layoutNumber++;
                    layouts[layoutNumber] = new LinearLayout(getActivity());
                    verticalLayout.addView(layouts[layoutNumber]);
                    layouts[layoutNumber].setId(View.generateViewId());
                    layoutIds[layoutNumber] = layouts[layoutNumber].getId(); //change me

                }

                //add the image to the layout
                LinearLayout currentLayout = getView().findViewById(layoutIds[layoutNumber]);
                currentLayout.addView(imageViews[i]);
                currentLayout.setPadding(0, 5, 0, 5);
                currentLayout.setY(0);
                currentLayout.setX(-16);

                //resize the image
                ImageView currentImage = getView().findViewById(imageId);
                //currently 3:4
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                currentImage.getLayoutParams().width = dm.widthPixels/4; //change me
                currentImage.getLayoutParams().height = 150; // change me

                //currentImage.setRotation(90);
                currentImage.setY(0);
                currentImage.setX(0);
                //currentImage.setPadding(80, 0, 0, 0);
            } else {
                System.out.println("Cant load image " + i);
            }
        }


        searchButton = (Button) getView().findViewById(R.id.searchBtn);
        input = getView().findViewById(R.id.searchText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("text is = "+input.getText().toString());
                String [] allResultingPaths =  dbSingleton.searchDB(input.getText().toString());
                updatePhotos(allResultingPaths, v);
            }
        });
        //loadingText.setText("");
        ImageView []allImages = new ImageView[max];
        for (int indx = 0; indx < max; indx++) {
            allImages[indx] = (ImageView) getView().findViewById(arrayOfIds[indx]);
            final int finalIndx = indx;
            allImages[indx].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoIntent = new Intent(getActivity(), PictureActivity.class);
                    photoIntent.putExtra("path",allImagePaths[finalIndx]);
                    photoIntent.putExtra("id",arrayOfIds[finalIndx]);
                    startActivity(photoIntent);
                }
            });
        }

    }


}