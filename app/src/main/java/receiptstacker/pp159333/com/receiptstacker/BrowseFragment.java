package receiptstacker.pp159333.com.receiptstacker;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.File;

public class BrowseFragment extends Fragment {
    Button searchButton, selectButton, deleteButton;
    ImageView image1;
    ScrollView scrollView;
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
        String [] allPhotos;
        int [] arrayOfIds = new int [1000]; // change me
        //arrayOfIds[0] = R.id.imageView0;
        File currentImage;
        ImageView [] imageViews = new ImageView[1000]; // change me
        allPhotos = dbSingleton.loadPhotos();
        LinearLayout layout1 = view.findViewById(R.id.layoutRow1);
        LinearLayout layout2 = view.findViewById(R.id.linearLayout);
        for(int i =0; i< 5; i++){ // change me
            System.out.println(allPhotos[i]);
            currentImage = new  File(allPhotos[i]);
            if(currentImage.exists()){
                Bitmap b = BitmapFactory.decodeFile(currentImage.getAbsolutePath());
                imageViews[i] = new ImageView(getActivity());
                imageViews[i].setImageBitmap(b);
                imageViews[i].setMaxHeight(20);
                imageViews[i].setMaxWidth(20);
                //for testing purposes
                if(i <3) {
                    layout1.addView(imageViews[i]);
                }else{
                    layout2.addView(imageViews[i]);
                }
                //image.setImageBitmap(b);
            }else{
                System.out.println("Cant load image "+ i);
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
        image1 = (ImageView)view.findViewById(R.id.imageView12);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(getActivity(), PhotoActivity.class);
                startActivity(photoIntent);
            }
        });
        scrollView= view.findViewById(R.id.scrollView2);

    }


}