package airbears.reneclaus.com;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import airbears.code4cal.reneclaus.com.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

/*
 * Developer : Rene Claus (reneclaus@gmail.com)
 * Date : 6/9/2012
 */

/**
 * I tried to receive just the right connection event or filter it down. I couldn't get it to work
 * reliably so the class just makes the requests for every connection matching the ssid.
 * 
 * Note: the url should be secure, otherwise there is no way to verify that the connection is correct.
 */
public class ConnectionReceiver extends BroadcastReceiver {
	
	protected static LoginInfo[] login_options ;

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context) ;
		try {
			if (sharedPrefs.getString("login_type", "Calnet ID").equals("Calnet ID")) {
				//calnet ID login
				login_options = new LoginInfo[] {new LoginInfo(context.getString(R.string.ssid),
						//URL
						new String[] {context.getString(R.string.url_calnet)},
						//POST
						new String[] {context.getString(R.string.post_calnet_1) + URLEncoder.encode(sharedPrefs.getString("username", ""), "utf-8") + context.getString(R.string.post_calnet_2) + URLEncoder.encode(sharedPrefs.getString("password", ""), "utf-8") + context.getString(R.string.post_calnet_3)})} ;
			}
			else {
				login_options = new LoginInfo[] {new LoginInfo(context.getString(R.string.ssid),
						//URL
						new String[] {context.getString(R.string.url_guest)},
						//POST
						new String[] {context.getString(R.string.post_guest_1) + URLEncoder.encode(sharedPrefs.getString("username", ""), "utf-8") + context.getString(R.string.post_guest_2) + URLEncoder.encode(sharedPrefs.getString("password", ""), "utf-8") + context.getString(R.string.post_guest_3)})} ;
			}
			
		} catch (UnsupportedEncodingException e) {}
		
		try {
			//connection established
			//get SSID
			WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE) ;
			if (wifiManager != null) {
				WifiInfo info = wifiManager.getConnectionInfo() ;
				if (info != null) {
					String ssid = info.getSSID() ;
					if (ssid != null) {
						for (int i = 0 ; i < login_options.length ; i++) {
							if (login_options[i].testSSID(ssid)) {
								if (login_options[i].connect()) {
									Toast.makeText(context, "Connected to " + ssid, Toast.LENGTH_SHORT).show() ;
								}
								break ;
							}
						}
					}
				}
			}
		}
		catch (Exception e) {}
	}
}
