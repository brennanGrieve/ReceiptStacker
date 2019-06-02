package receiptstacker.pp159333.com.receiptstacker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * class for the main activity for the application
 */
public class  MainActivity extends AppCompatActivity {
    int  currentFrag; //0 Scan, 1 Browse, 2 GstCalc
    private SharedPreferences.OnSharedPreferenceChangeListener sUpdateThemeView = new SharedPreferences.OnSharedPreferenceChangeListener() {
        /**
         * onSharedPreferenceChanged
         * @param sharedPreferences the shared preferences
         * @param key a key
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            recreate();
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        /**
         * onNavigationItemSelected
         * @param item a menu item
         * @return weather successful
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_scan:
                    if (currentFrag==0) {
                        return false;
                    }
                    transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                    currentFrag=0;
                    selectedFragment = ScanFragment.newInstance(getApplicationContext());
                    break;
                case R.id.navigation_browse:
                    if (currentFrag==1){
                        return false;
                    }
                    if(currentFrag==0){
                        transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                    }
                    if(currentFrag==2){
                        transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                    }
                    currentFrag=1;
                    selectedFragment = BrowseFragment.newInstance();
                    break;
                case R.id.navigation_gstcal:
                    if (currentFrag==2){
                        return false;
                    }
                    currentFrag=2;
                    transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);

            }
            if (selectedFragment != null) {
                transaction.replace(R.id.frame_layout, selectedFragment);
            }
            transaction.commit();
            return true;


        }
    };

    /**
     * onCreate
     * @param savedInstanceState saved state of the last known instance of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ScanFragment.newInstance(getApplicationContext()));
        transaction.commit();
        currentFrag=0;
        getSupportActionBar().hide();
        changeTheme(navigation);

    }

    /**
     * Changes theme to the preferred user theme
     * @param navigation a bottom navigation view
     */
    void changeTheme (BottomNavigationView navigation) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(sUpdateThemeView);
        String currentTheme = preferences.getString("themeSelection", "default");
        switch(currentTheme){
            case "pinkTheme":
                setTheme(R.style.pinkTheme);
                break;

            case "titaniumTheme":
                setTheme(R.style.titaniumTheme);
                break;

            case "snowTheme":
                setTheme(R.style.crystalTheme);
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
        switch(currentTheme){
            case "pinkTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.pink);
                navigation.setItemBackgroundResource(R.color.pink);
                break;

            case "titaniumTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.titanium);
                navigation.setItemBackgroundResource(R.color.titanium);
                break;

            case "crystalTheme":
                navigation.setBackgroundResource(0);
                navigation.setBackgroundResource(R.color.crystal);
                navigation.setItemBackgroundResource(R.color.crystal);
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

    }

    /**
     * opens the settings activity
     * @param v the current view
     */
    public void openSettingsActivity(View v){
        Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
    }
}
