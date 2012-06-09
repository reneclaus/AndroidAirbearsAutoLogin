package airbears.reneclaus.com;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/*
 * Developer : Rene Claus (reneclaus@gmail.com)
 * Date : 6/9/2012
 */

public class LoginInfo {
	
	private String ssid ;
	private String[] urls ;
	private String[] posts ;
	
	public LoginInfo (String ssid, String[] urls, String[] posts) {
		if (urls == null || posts == null || urls.length != posts.length)
			throw new IllegalArgumentException ();
		this.ssid = ssid ;
		this.urls = urls ;
		this.posts = posts ;
	}
	
	/**
	 * 
	 * @param ssid The SSID that the phone just connected to.
	 * @return true if the SSID matches, false if it doesn't
	 */
	public boolean testSSID (String ssid) {
		return ssid.equals(this.ssid) ;
	}
	
	/**
	 * Errors can occur if the connection is broken or if the server didn't validate correctly for https
	 * (this allows you to send the password outright - a fake server won't let the https connection go through)
	 * 
	 * @param client An HTTPClient that can be used to make the login requests
	 * @return true if the login requests didn't return any errors, false if any error occured
	 * @throws UnsupportedEncodingException
	 */
	public boolean connect () {
		try {
			DefaultHttpClient client = new DefaultHttpClient () ;
			for (int i = 0; i < urls.length ; i++) {
				if (posts[i] == null) { //GET request
					HttpGet httpget = new HttpGet (urls[i]) ;
					HttpResponse res = client.execute(httpget) ;
					if (res == null)
						return false ;
					
				}
				else { //POST request
					HttpPost httppost = new HttpPost (urls[i]) ;
					httppost.setEntity(new StringEntity(posts[i])) ;
					httppost.setHeader("Content-Type", "application/x-www-form-urlencoded") ;
					HttpResponse res = client.execute(httppost) ;
					if (res == null)
						return false ;
				}
			}
		} catch (UnsupportedEncodingException e) {
			return false ;
		} catch (ClientProtocolException e) {
			return false ;
		} catch (IOException e) {
			return false ;
		}
		return true;
	}
}
