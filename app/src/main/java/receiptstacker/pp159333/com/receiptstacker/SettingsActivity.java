package receiptstacker.pp159333.com.receiptstacker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {


    /**
     * Event Listener to cause a recreation when the Theme setting is changed, to make effects immediate.
     */
    private SharedPreferences.OnSharedPreferenceChangeListener sUpdateThemeView = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
           recreate();
        }
    };


    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * onCreate
     * @param savedInstanceState saved state of the last known instance of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = preferences.getString("themeSelection", "default");
        preferences.registerOnSharedPreferenceChangeListener(sUpdateThemeView);
        switch(currentTheme){
            case "default":
                break;

            case "pinkTheme":
                setTheme(R.style.pinkTheme);

                break;

            case "titaniumTheme":
                setTheme(R.style.titaniumTheme);

                break;

            case "crystalTheme":
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
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * when a menu item is selected
     * @param featureId the feature id
     * @param item  a menu item
     * @return superclass handling of selected item.
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || ScanSettingsPreferenceFragment.class.getName().equals(fragmentName)
                || PersonalizationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows Scan Settings. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ScanSettingsPreferenceFragment extends PreferenceFragment {
        /**
         * onCreate
         * @param savedInstanceState saved state of the last known instance of the activity.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_scan_settings);

            setHasOptionsMenu(true);

        }

        /**
         * when an options item is selected.
         * @param item the selected item.
         * @return superclass handling of selected item.
         */
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows personalization preferences. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PersonalizationPreferenceFragment extends PreferenceFragment {
        /**
         * onCreate
         * @param savedInstanceState saved state of the last known instance of the activity.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_personalize);
            setHasOptionsMenu(true);
        }

        /**
         * when an options item is selected.
         * @param item a menu item
         * @return superclass handling of selected item.
         */
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
