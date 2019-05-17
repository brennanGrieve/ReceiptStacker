package receiptstacker.pp159333.com.receiptstacker;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.io.File;

public class BrowseFragment extends Fragment {
    Button searchButton, selectButton, deleteButton;
    ImageView image1;
    ScrollView scrollView;
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] allPhotos;
        File newImage;

        ImageView[] imageViews = new ImageView[1000]; // change me
        LinearLayout[] layouts = new LinearLayout[1000]; // change me

        allPhotos = dbSingleton.loadPhotos();
        LinearLayout verticalLayout = view.findViewById(R.id.verticalLayout);
        int layoutId = 0;

        int layoutNumber = 0;
        for (int i = 0; i < max; i++) {
            System.out.println(allPhotos[i]);
            if (allPhotos[i] != null) {
                newImage = new File(allPhotos[i]);
            } else {
                newImage = null;
            }
            if (newImage != null) {
                Bitmap b = BitmapFactory.decodeFile(newImage.getAbsolutePath());
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
                currentImage.getLayoutParams().width = 150; //change me
                currentImage.getLayoutParams().height = 150; // change me
                currentImage.setRotation(90);
                currentImage.setY(0);
                currentImage.setX(0);
                currentImage.setPadding(0, 25, 0, 25);
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
        selectButton = (Button) view.findViewById(R.id.selectBtn);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add code for selecting photos
            }
        });
        deleteButton = (Button) view.findViewById(R.id.deleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add code for deleting photos
            }
        });

        ImageView []allImages = new ImageView[max];
        for (int i = 0; i < max; i++) {

            allImages[i] = (ImageView) view.findViewById(arrayOfIds[i]);

            allImages[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoIntent = new Intent(getActivity(), PhotoActivity.class);
                    startActivity(photoIntent);

                }
            });
        }
    }

}