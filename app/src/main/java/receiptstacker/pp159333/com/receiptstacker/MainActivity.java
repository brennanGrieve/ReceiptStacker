package receiptstacker.pp159333.com.receiptstacker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class  MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_scan:
                    return true;
                case R.id.navigation_browse:
                    return true;
                case R.id.navigation_gstcal:
                    return true;
                case R.id.navigation_options:
                    Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    return true;
            }
            return false;


        }
    };

    private ImageView.OnClickListener mOnClickListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            //This is where the camera will take a photo
            System.out.print("Camera Click");
            ImageView camera = findViewById(R.id.imageView_Camera);
            camera.setBackgroundColor(Color.BLACK);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ImageView cameraShutter = findViewById(R.id.imageViewCameraShutter);
        cameraShutter.setOnClickListener(mOnClickListener);

    }

}
