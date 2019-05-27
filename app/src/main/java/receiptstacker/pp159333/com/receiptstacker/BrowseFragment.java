package receiptstacker.pp159333.com.receiptstacker;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;

/**
 * BrowseFragment
 * a class used to display and browse a grid of receipts
 */
public class BrowseFragment extends Fragment {
    Button searchButton;        // the search button
    EditText input;             // the search input
    int[] arrayOfIds;           // an array of image ids
    String[] allImagePaths;     // an array of all the image paths
    LinearLayout[] layouts;     // an array of layouts
    int[] layoutIds;            // an array of layout ids
    ImageView[] imageViews;     // an array of image views
    int numberDisplayed = 0;    // the current number of images displayed
    ImageView[] displayedViews; // the current number of image views displayed
    int numOfPhotos = 0;        // the number of photos loaded
    TextView loadingText;       // text view used with loading photos
    Bitmap [] arrayOfBitmaps;   // an array of bitmaps

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
                layouts[l].removeView(imageViews[indx]);
            }
        }else{
            for (int indx = 0; indx < numberDisplayed; indx++) {
                l = indx / 4;
                layouts[l].removeView(displayedViews[indx]);
            }
        }
        numberDisplayed = 0;
        for(int i =0; i< arrayOfPaths.length; i++){
            for(int j = 0; j<arrayOfIds.length; j++){
                if(allImagePaths[j].equals(arrayOfPaths[i])){
                    l = i/4;

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

    void imageThread(){
        final int max = dbSingleton.getNumberOfPhotos();
        final int layoutMax = (max/3)+1;
        arrayOfBitmaps = new Bitmap[max];
        arrayOfIds  = new int[max];
        layouts = new LinearLayout[layoutMax];
        layoutIds = new int[layoutMax];
        imageViews = new ImageView[max];
        displayedViews = new ImageView[max];
        allImagePaths = dbSingleton.loadPhotos();
        loadingText = getView().findViewById(R.id.loadingText);
        String loadingStr = "LOADING: "+0+"/"+max;
        loadingText.setText(loadingStr);
        final File[] imageFiles = new File[max];
        for (int i = 0; i < max; i++) {
            if (allImagePaths[i] != null) {
                imageFiles[i] = new File(allImagePaths[i]);
            } else {
                imageFiles[i] = null;
            }
        }
        //final int finalMax = max;
        final BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 16;        // change me to adjust how much the images are scaled down

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< max; i++) {
                    arrayOfBitmaps[i] = BitmapFactory.decodeFile(imageFiles[i].getAbsolutePath(), o);
                    final int finalIndex = i;
                    imageViews[i].post(new Runnable() {
                        @Override
                        public void run() {
                            imageViews[finalIndex].setImageBitmap(arrayOfBitmaps[finalIndex]);
                            numOfPhotos+=1;
                            if(numOfPhotos == max){
                                try {
                                    wait(500);
                                }catch(Exception e){
                                    System.out.println(e.getMessage());
                                }
                                loadingText.setText("");
                            }else {
                                loadingText.setText("LOADING: " + numOfPhotos + "/" + max);
                            }
                        }
                    });
                }


            }
        }).start();
        LinearLayout verticalLayout = getView().findViewById(R.id.verticalLayout);
        int layoutNumber = -1;
        for (int i = 0; i < max; i++) {
            System.out.println(allImagePaths[i]);
            if (imageFiles != null) {
                imageViews[i] = new ImageView(getActivity());
                imageViews[i].setId(View.generateViewId());
                int imageId = imageViews[i].getId();
                arrayOfIds[i] = imageId;
                //create a new horizontal layout for every 3 photos
                if (i % 4 == 0) {
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
                currentLayout.setX(-16); // shifts to center the layout

                //resize the image
                ImageView currentImage = getView().findViewById(imageId);
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                currentImage.getLayoutParams().width = dm.widthPixels/4; //change me
                currentImage.getLayoutParams().height = 150;            // change me
                currentImage.setY(0);
                currentImage.setX(0);
            } else {
                System.out.println("Cant load image number " + i);
            }
        }
        searchButton = getView().findViewById(R.id.searchBtn);
        input = getView().findViewById(R.id.searchText);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] allResultingPaths =  dbSingleton.searchDB(input.getText().toString());
                updatePhotos(allResultingPaths, v);
            }
        });
        ImageView []allImages = new ImageView[max];
        for (int indx = 0; indx < max; indx++) {
            allImages[indx] = getView().findViewById(arrayOfIds[indx]);
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