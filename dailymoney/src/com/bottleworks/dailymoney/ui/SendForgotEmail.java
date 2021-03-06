package com.bottleworks.dailymoney.ui;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * 
 * loginID : top800321
 * @author 軟體工程 102522030 王竣鋒
 *
 */

public class SendForgotEmail {
	    public static void executeSend(String email,String passcode) {
	        try {
	        	HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://140.115.52.112:8080/SEProject/SendForgotEmail.php");
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("passcode", passcode));
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                Log.v("log_tag", email);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.v("log_tag", "HTTP成功");
	        } catch(Exception e) {
	            Log.e("log_tag", e.toString());
	        }
	 }
}
