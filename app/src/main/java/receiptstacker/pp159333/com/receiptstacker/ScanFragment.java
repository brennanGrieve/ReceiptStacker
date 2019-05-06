package receiptstacker.pp159333.com.receiptstacker;
import android.Manifest;
<<<<<<< HEAD
=======
import android.app.AlertDialog;
>>>>>>> 01668107cda7a6fb5c424496ba7cea833abf2043
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
<<<<<<< HEAD
=======
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
>>>>>>> 01668107cda7a6fb5c424496ba7cea833abf2043
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
<<<<<<< HEAD
=======
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;
>>>>>>> 01668107cda7a6fb5c424496ba7cea833abf2043


public class ScanFragment extends Fragment {
    SurfaceView cameraView;
    static Context appContext;
    final int RequestCameraPermissionID = 1001;
    TextView textView;
    static CameraSource cameraSource;
    private String rawOCRString;



    public static ScanFragment newInstance(Context context) {
        ScanFragment fragment = new ScanFragment();
        appContext = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView cameraShutter = view.findViewById(R.id.imageViewCameraShutter);
        cameraShutter.setOnClickListener(mOnClickListener);
        cameraView = view.findViewById(R.id.surfaceView_Camera);
        getCamera(getView());


    }

    private String getCurrentTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String timeStamp = simpleDateFormat.format(new Date());
        return timeStamp;
    }

    private String saveImageToFileSystem(Bitmap receiptPic){
        ContextWrapper appWrap = new ContextWrapper(getActivity().getApplicationContext());
        FileOutputStream receiptStream = null;
        File dir = appWrap.getDir("receiptImages", Context.MODE_PRIVATE);
        File newReceiptImage = new File(dir, getCurrentTimeStamp());
        try{
            receiptStream = new FileOutputStream(newReceiptImage);
            receiptPic.compress(Bitmap.CompressFormat.PNG, 100, receiptStream);
        }catch(Exception FileOpenException){
            FileOpenException.printStackTrace();
        }finally{
            try{
                receiptStream.close();
            }catch(IOException fileCloseException){
                fileCloseException.printStackTrace();
            }
        }
        Log.d(TAG, "saveImageToFileSystem: File Path is:" + newReceiptImage.getAbsolutePath());
        return newReceiptImage.getAbsolutePath();
    }

    CameraSource.PictureCallback pCallBack = new CameraSource.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes) {
            //handle loading the image into the receipt here
            Bitmap pic = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            //MUST be run before Accessing Database!
              dbSingleton.initDB(getActivity().getApplicationContext());
              String filename = saveImageToFileSystem(pic);
              dbSingleton.commitToDB(filename, rawOCRString);

            //ImageView image = new ImageView(getActivity());

   //         image.setImageBitmap(pic);
//
//            AlertDialog.Builder builder =
//                    new AlertDialog.Builder(getActivity()).
//                            setMessage("Message above the image").
//                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).
//                            setView(image);
//            builder.create().show();
        }
    };

    private ImageView.OnClickListener mOnClickListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Camera takes a picture here
            cameraSource.takePicture(null, pCallBack);
        }
    };

    public void getCamera(View v) {
        cameraView = v.findViewById(R.id.surfaceView_Camera);
        textView = v.findViewById(R.id.textView);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(appContext).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Dependencies are not available");

        } else {
            cameraSource = new CameraSource.Builder(appContext, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1080, 1920)
                    .setRequestedFps(60)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
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

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                        if (items.size() != 0) {
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i = 0; (i < items.size()); i++) {
                                        TextBlock item = items.valueAt(i);
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