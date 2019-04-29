package receiptstacker.pp159333.com.receiptstacker;
import android.content.Intent;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Surface;
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
import java.util.logging.Logger;


public class ScanFragment extends Fragment {
    SurfaceView cameraView;
    static Context appContext;
    final int RequestCameraPermissionID = 1001;
    TextView textView;


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
        ImageView cameraView = view.findViewById(R.id.surfaceView_Camera);
        getCamera(getView());


    }

    private ImageView.OnClickListener mOnClickListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*This is where the camera will take a photo
            System.out.print("Camera Click");
            ImageView camera = getView().findViewById(R.id.surfaceView_Camera);
            camera.setBackgroundColor(Color.BLACK);*/
        }
    };

    public void getCamera(View v) {
        cameraView = v.findViewById(R.id.surfaceView_Camera);
        textView = v.findViewById(R.id.textView);
        final CameraSource cameraSource;

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
                                    String rawString = stringBuilder.toString();
                                    textView.setText(rawString);
                                }
                            });
                        }
                }
            });
        }
    }


}