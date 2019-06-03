package receiptstacker.pp159333.com.receiptstacker;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

/**
 * Fragment Class that handles the Camera Capture Fragment Display and underlying logic.
 */

public class ScanFragment extends Fragment {
    SurfaceView cameraView;
    static Context appContext;
    final int RequestCameraPermissionID = 1001;
    TextView textView;
    static CameraSource cameraSource;
    private String rawOCRString;
    private SparseArray<TextBlock> OCRItems;
    private boolean multiCapFlag = false;
    private int multiCapSequence = 1;
    ImageView multiCapIndicator;
    private Receipt multiCapReceipt;


    /**
     * Method to Create and return an instance of the ScanFragment class for use.
     * @param context Application Context to create Fragment With
     * @return Fragment instance to be used.
     */

    public static ScanFragment newInstance(Context context) {
        ScanFragment fragment = new ScanFragment();
        appContext = context;
        return fragment;
    }

    /**
     * Method Executed whenever the Scan Fragment is created.
     * Also initializes the Database for use.
     * @param savedInstanceState saved state of the last known instance of the activity.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbSingleton.initDB(getActivity().getApplicationContext());

    }

    /**
     * Method Executed whenever a View is constructed for the Fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }


    /**
     * Method executed when a fragment View is created. Populates view and fetches the Device Camera for use.
     * @param view View that Android has created.
     * @param savedInstanceState State of the last Instance of the fragment.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView cameraShutter = view.findViewById(R.id.imageViewCameraShutter);
        ImageView multiCap = view.findViewById(R.id.multiCapView);
        multiCapIndicator = view.findViewById(R.id.imageViewMulticapActiveIndicator);
        cameraShutter.setOnClickListener(mOnClickListener);
        multiCap.setOnClickListener(mMultiCapOnClickListener);
        cameraView = view.findViewById(R.id.surfaceView_Camera);
        getCamera(getView());


    }


    CameraSource.PictureCallback pMultiCallBack = new CameraSource.PictureCallback() {
        /**
         * onPictureTaken
         * @param bytes the bytes
         */
        @Override
        public void onPictureTaken(byte[] bytes) {
            Bitmap multiPic = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Bitmap rotatedPic;
            if (multiPic.getWidth()>multiPic.getHeight()){
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                rotatedPic = Bitmap.createBitmap(multiPic, 0, 0, multiPic.getWidth(), multiPic.getHeight(), matrix, true);
            } else {
                rotatedPic = multiPic;
            }
            if(multiCapSequence == 1) {
                ++multiCapSequence;
                if(multiCapReceipt == null) {
                    multiCapReceipt = new Receipt(OCRItems, rotatedPic);
                }else{
                    multiCapReceipt.reset();
                    multiCapReceipt.reinitialize(OCRItems, rotatedPic);
                }
            }
            else{
                ++multiCapSequence;
                multiCapReceipt.mergeBitmaps(rotatedPic);
                multiCapReceipt.addNewOCR(OCRItems);
                multiCapReceipt.updateDynamicDerivedValues();
            }
        }
    };

    CameraSource.PictureCallback pCallBack = new CameraSource.PictureCallback() {
        /**
         * onPictureTaken
         * @param bytes the bytes
         */
        @Override
        public void onPictureTaken(byte[] bytes) {
            Bitmap pic = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Bitmap rotatedPic;
            if (pic.getWidth()>pic.getHeight()){
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                rotatedPic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(), matrix, true);
            } else {
                rotatedPic = pic;
            }
            Receipt receipt = new Receipt(OCRItems, rotatedPic);
            ImageTakenDialog imageTakenDialog = new ImageTakenDialog(getContext(), receipt);
            imageTakenDialog.showDialog();
        }
    };

    /**
     * Action Listener for the Multi-Capture Button.
     */
    private ImageView.OnClickListener mMultiCapOnClickListener = new ImageView.OnClickListener(){
        /**
         * On Click Listener to fire Logic for the MultiCap button. Behavior depends on current MultiCap state.
         * @param v the current view
         */
        @Override
        public void onClick(View v){
            if(!multiCapFlag) {
                multiCapFlag = true;
                multiCapIndicator.setVisibility(View.VISIBLE);
            }
            else{
                //finalize image stitching and commit to db
                multiCapFlag = false;
                if(multiCapReceipt != null) {
                    ImageTakenDialog imageTakenDialog = new ImageTakenDialog(getContext(), multiCapReceipt);
                    imageTakenDialog.showDialog();
                }
                multiCapSequence = 1;
                multiCapIndicator.setVisibility(View.INVISIBLE);
            }
        }
    };


    /**
     * Event Listener for the Camera Capture Button.
     */
    private ImageView.OnClickListener mOnClickListener = new ImageView.OnClickListener() {
        /**
         * onClick
         * @param v the current view
         */
        @Override
        public void onClick(View v) {
            //Camera takes a picture here
            if(!multiCapFlag) {
                cameraSource.takePicture(null, pCallBack);
            }
            else if(multiCapFlag){
                cameraSource.takePicture(null, pMultiCallBack);
            }
        }
    };

    /**
     * Function to retrieve the device Camera for image capture.
     * @param v the current view
     */
    public void getCamera(View v) {
        cameraView = v.findViewById(R.id.surfaceView_Camera);
        textView = v.findViewById(R.id.textView);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(appContext).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Dependencies are not available");
        } else {
            cameraSource = new CameraSource.Builder(appContext, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1920, 1080)
                    .setRequestedFps(30)
                    .setAutoFocusEnabled(true)
                    .build();


            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                /**
                 * surfaceCreated
                 * @param surfaceHolder the surface holder
                 */
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                /**
                 * gathers the detections for the raw OCR string
                 * @param detections the detections
                 */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    OCRItems = detections.getDetectedItems();
                    if (OCRItems.size() != 0) {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; (i < OCRItems.size()); i++) {
                                    TextBlock item = OCRItems.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append(" ");
                                }
                                rawOCRString = stringBuilder.toString();
                                textView.setText(rawOCRString);
                            }
                        });
                    }
                }
            });
        }
    }

}