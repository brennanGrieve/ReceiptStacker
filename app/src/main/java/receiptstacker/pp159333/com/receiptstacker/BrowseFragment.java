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
    int max = dbSingleton.getNumberOfPhotos();
    int[] arrayOfIds = new int[max];
    String[] allImagePaths;
    LinearLayout[] layouts = new LinearLayout[1000]; // change me
    int[] layoutIds = new int[(max/3) +1]; // change me defo

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

        ImageView[] imageViews = new ImageView[1000]; // change me

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
                    layoutIds[layoutNumber] = layouts[layoutNumber].getId(); //change me
                    layoutNumber++;
                }

                //add the image to the layout
                LinearLayout currentLayout = view.findViewById(layoutIds[i/3]); //change me
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
        int l =0;
        for(int i =0; i<arrayOfPaths.length; i++){
            System.out.println(arrayOfPaths[i]);
        }
        System.out.println("arr of paths = "+arrayOfPaths.length);
        System.out.println("arr of ids = "+arrayOfIds.length);
        System.out.println("arr of layouts= "+layoutIds.length);
        for(int i =0; i< arrayOfPaths.length-1; i++){
            l =0;
            for(int j = 0; j<arrayOfIds.length-1; j++){
                //System.out.println("NUmber of like searches"+i);
                if(allImagePaths[j] == arrayOfPaths[i]){
                    System.out.println("testing");
                    if(j %3 == 0){ l++;}
                    ImageView imgV = v.findViewById(arrayOfIds[j]);

                    LinearLayout layout = v.findViewById(layoutIds[l]); // haha i shouldnt be doing this :P
                    if(layout != null) {
                        System.out.println("removing the trash");
                        layout.removeView(imgV);
                    }else{

                    }
                }
            }
        }
    }

}