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
    ImageView[] displayedViews; // an array of image views gathered from a search
    int numOfPhotos = 0;        // the number of photos loaded
    TextView loadingText;       // text view used with loading photos
    Bitmap [] arrayOfBitmaps;   // an array of bitmaps
    public static BrowseFragment newInstance() {
        return new BrowseFragment();
    }

    /**
     * onCreate
     * @param savedInstanceState saved state of the last known instance of the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * onViewCreate
     * @param inflater a layout inflater
     * @param container a view group
     * @param savedInstanceState saved state of the last known instance of the activity.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    /**
     * onViewCreated
     * @param view the current view
     * @param savedInstanceState saved state of the last known instance of the activity.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageThread();
    }

    /**
     * updatePhotos
     * updates the images displayed after a search has been completed
     * @param arrayOfImagePaths an array of image paths
     */
    public void updatePhotos(String [] arrayOfImagePaths){
        int l;
        if(numberDisplayed == 0) { //on the first search remove all the views
            for (int indx = 0; indx < arrayOfIds.length; indx++) {
                l = indx / 4;
                layouts[l].removeView(imageViews[indx]);
            }
        }else{ //only remove the views from the last search
            for (int indx = 0; indx < numberDisplayed; indx++) {
                l = indx / 4;
                layouts[l].removeView(displayedViews[indx]);
            }
        }
        numberDisplayed = 0; //reset number displayed to 0
        for(int i =0; i< arrayOfImagePaths.length; i++){
            for(int j = 0; j<arrayOfIds.length; j++){
                if(allImagePaths[j].equals(arrayOfImagePaths[i])){
                    l = i/4;
                    layouts[l].addView(imageViews[j]);
                    displayedViews[numberDisplayed] = imageViews[j];
                    numberDisplayed++;
                }
            }
        }
    }

    /**
     * imageThread
     * a function that loads the images on a separate thread
     */
    void imageThread(){
        final int numberOfPhotos = dbSingleton.getNumberOfPhotos();  // the number of photos
        final int numberOfLayouts = (numberOfPhotos/3)+1;            // the number of layouts
        arrayOfBitmaps = new Bitmap[numberOfPhotos];
        arrayOfIds  = new int[numberOfPhotos];
        layouts = new LinearLayout[numberOfLayouts];
        layoutIds = new int[numberOfLayouts];
        imageViews = new ImageView[numberOfPhotos];
        displayedViews = new ImageView[numberOfPhotos];
        allImagePaths = dbSingleton.loadPhotos();
        loadingText = getView().findViewById(R.id.loadingText);
        String loadingStr = "LOADING: "+0+"/"+numberOfPhotos;
        if(loadingText != null) {
            loadingText.setText(loadingStr);
        }
        final File[] imageFiles = new File[numberOfPhotos];
        for (int i = 0; i < numberOfPhotos; i++) {
            if (allImagePaths[i] != null) {
                imageFiles[i] = new File(allImagePaths[i]);
            } else {
                imageFiles[i] = null;
            }
        }
        final BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 16;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< numberOfPhotos; i++) {
                    arrayOfBitmaps[i] = BitmapFactory.decodeFile(imageFiles[i].getAbsolutePath(), o);
                    final int finalIndex = i;
                    imageViews[i].post(new Runnable() {
                        @Override
                        public void run() {
                            imageViews[finalIndex].setImageBitmap(arrayOfBitmaps[finalIndex]);
                            numOfPhotos+=1;
                            if(numOfPhotos == numberOfPhotos){
                                try {
                                    wait(500);
                                }catch(Exception e){
                                    System.out.println(e.getMessage());
                                }
                                loadingText.setText(""); //Clear the loading text
                            }else {
                                loadingText.setText("LOADING: " + numOfPhotos + "/" + numberOfPhotos);
                            }
                        }
                    });
                }


            }
        }).start();
        LinearLayout verticalLayout = getView().findViewById(R.id.verticalLayout);
        int layoutNumber = -1;
        for (int i = 0; i < numberOfPhotos; i++) {
            if (imageFiles != null) {
                imageViews[i] = new ImageView(getActivity());
                imageViews[i].setId(View.generateViewId());
                int imageId = imageViews[i].getId();
                arrayOfIds[i] = imageId;
                if (i % 4 == 0) {
                    layoutNumber++;
                    layouts[layoutNumber] = new LinearLayout(getActivity());
                    verticalLayout.addView(layouts[layoutNumber]);
                    layouts[layoutNumber].setId(View.generateViewId());
                    layoutIds[layoutNumber] = layouts[layoutNumber].getId();
                }
                LinearLayout currentLayout = getView().findViewById(layoutIds[layoutNumber]);
                currentLayout.addView(imageViews[i]);
                currentLayout.setPadding(0, 5, 0, 5);
                currentLayout.setY(0);
                currentLayout.setX(-16); // shifts to center the layout
                ImageView currentImage = getView().findViewById(imageId);
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                currentImage.getLayoutParams().width = dm.widthPixels/4;
                currentImage.getLayoutParams().height = 150;
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
                updatePhotos(allResultingPaths);
            }
        });
        ImageView []allImages = new ImageView[numberOfPhotos];
        for (int indx = 0; indx < numberOfPhotos; indx++) {
            allImages[indx] = getView().findViewById(arrayOfIds[indx]);
            final int finalIndx = indx;
            allImages[indx].setOnClickListener(new View.OnClickListener() {
                /**
                 * onClick
                 * @param v the current view
                 */
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