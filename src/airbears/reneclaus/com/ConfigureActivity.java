package airbears.reneclaus.com;

import airbears.code4cal.reneclaus.com.R;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

/*
 * Developer : Rene Claus (reneclaus@gmail.com)
 * Date : 6/9/2012
 */

/**
 * This class provides the preference panel. When enabled is clicked the broadcast listener is enabled/disabled.
 */
public class ConfigureActivity extends PreferenceActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		PackageManager pm = getApplicationContext()
				.getPackageManager();
		ComponentName componentName = new ComponentName(
				ConfigureActivity.this,
				ConnectionReceiver.class);
		
		Preference enabledPrefferenceButton = (Preference) findPreference("enabled");
		enabledPrefferenceButton.setDefaultValue(pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) ;
		
        enabledPrefferenceButton.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newVal) {
						PackageManager pm = getApplicationContext()
								.getPackageManager();
						ComponentName componentName = new ComponentName(
								ConfigureActivity.this,
								ConnectionReceiver.class);
						if ((Boolean) newVal) {
							// is now enabled, turn on BroadcastListener
							pm.setComponentEnabledSetting(
									componentName,
									PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
									PackageManager.DONT_KILL_APP);
						} else {
							// is now disabled, turn off BroadcastListener
							pm.setComponentEnabledSetting(
									componentName,
									PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
									PackageManager.DONT_KILL_APP);
						}

						return true;
					}

            });
    }
}