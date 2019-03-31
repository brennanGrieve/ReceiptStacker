package receiptstacker.pp159333.com.receiptstacker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class  MainActivity extends AppCompatActivity {

    private SharedPreferences.OnSharedPreferenceChangeListener sUpdateThemeView = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            recreate();
        }
    };

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_scan:
                    return true;

                case R.id.navigation_browse:
                    startActivity(new Intent(MainActivity.this, BrowseActivity.class));
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(sUpdateThemeView);
        String currentTheme = preferences.getString("themeSelection", "default");
        switch(currentTheme){
                case "pinkTheme":
                    setTheme(R.style.pinkTheme);
                    break;

                case "silverTheme":
                    setTheme(R.style.SilverTheme);
                    break;

                case "snowTheme":
                    setTheme(R.style.snowTheme);
                    break;

                case "greenTheme":
                    setTheme(R.style.greenTheme);
                    break;

                case "redTheme":
                    setTheme(R.style.redTheme);
                    break;

                case "goldTheme":
                    setTheme(R.style.goldTheme);
                break;
        }
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switch(currentTheme){
            case "pinkTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.pink);
                navigation.setItemBackgroundResource(R.color.pink);
                break;

            case "silverTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.silver);
                navigation.setItemBackgroundResource(R.color.silver);
                break;

            case "snowTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.snow);
                navigation.setItemBackgroundResource(R.color.snow);
                break;

            case "greenTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.green);
                navigation.setItemBackgroundResource(R.color.green);
                break;

            case "redTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.red);
                navigation.setItemBackgroundResource(R.color.red);
                break;

            case "goldTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.gold);
                navigation.setItemBackgroundResource(R.color.gold);
                break;
        }
        ImageView cameraShutter = findViewById(R.id.imageViewCameraShutter);
        cameraShutter.setOnClickListener(mOnClickListener);

    }

}
