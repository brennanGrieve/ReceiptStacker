package receiptstacker.pp159333.com.receiptstacker;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import java.io.File;
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

        File newImage;
        int max = dbSingleton.getNumberOfPhotos();
        arrayOfIds  = new int[max];
        int layoutMax = (max/3)+1;
        layouts = new LinearLayout[layoutMax];
        layoutIds = new int[layoutMax];
        imageViews = new ImageView[max];
        displayedViews = new ImageView[max];

        allImagePaths = dbSingleton.loadPhotos();
        LinearLayout verticalLayout = view.findViewById(R.id.verticalLayout);
        Bitmap b = null;
        int layoutId = 0;

        int layoutNumber = -1;
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
                    layoutNumber++;
                    layouts[layoutNumber] = new LinearLayout(getActivity());
                    verticalLayout.addView(layouts[layoutNumber]);
                    layouts[layoutNumber].setId(View.generateViewId());
                    layoutIds[layoutNumber] = layouts[layoutNumber].getId(); //change me

                }

                //add the image to the layout
                LinearLayout currentLayout = view.findViewById(layoutIds[layoutNumber]);
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
        input = view.findViewById(R.id.searchText);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("text is = "+input.getText().toString());
                String [] allResultingPaths =  dbSingleton.searchDB(input.getText().toString());
                updatePhotos(allResultingPaths, v);
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
                    photoIntent.putExtra("id",arrayOfIds[finalIndx]);
                    startActivity(photoIntent);
                    // add the image to the activity

                }
            });
        }
    }
    public void updatePhotos(String [] arrayOfPaths, View v){
        int l;
        System.out.println("arr of paths = "+arrayOfPaths.length);
        System.out.println("arr of ids = "+arrayOfIds.length);
        System.out.println("arr of layouts= "+layoutIds.length);
        if(numberDisplayed == 0) {
            for (int indx = 0; indx < arrayOfIds.length; indx++) {
                l = indx / 3;
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
                    l = i/3;
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

}