package receiptstacker.pp159333.com.receiptstacker;

import android.content.Intent;
<<<<<<< HEAD
import android.graphics.Color;
=======
>>>>>>> origin/BrowseScreen
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
<<<<<<< HEAD
                case R.id.navigation_browse:
=======
                case R.id.navigation_dashboard:
                    startActivity(new Intent(MainActivity.this, BrowseActivity.class));
>>>>>>> origin/BrowseScreen
                    return true;
                case R.id.navigation_gstcal:
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

    public void openSettingsActivity(View v){
        Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
    }


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
