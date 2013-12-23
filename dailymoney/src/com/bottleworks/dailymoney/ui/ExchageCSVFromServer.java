package com.bottleworks.dailymoney.ui;

/* 102522030
 * top800321
 */

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class ExchageCSVFromServer {
	public static void checkfolder(String account) {
		try {
        	HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://140.115.52.112:8080/SEProject/CheckFolder.php");
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("account", account));
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            Log.v("log_tag", account);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.v("log_tag", "HTTP成功");
        } catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
	}
	
	public static void UploadCSV(String uploadFilePath,String uploadFileName,String upLoadServerUri) {
		try { 
			int serverResponseCode = 0;
			String sourceFileUri = uploadFilePath+uploadFileName;
			String fileName = sourceFileUri;
			
			File sourceFile = new File(sourceFileUri);
			
			HttpURLConnection conn = null;
	        DataOutputStream dos = null;  
	        String lineEnd = "\r\n";
	        String twoHyphens = "--";
	        String boundary = "*****";
	        int bytesRead, bytesAvailable, bufferSize;
	        byte[] buffer;
	        int maxBufferSize = 1 * 1024 * 1024; 
	        
	        
            // open a URL connection to the Servlet
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(upLoadServerUri);
           
			// Open a HTTP  connection to  the URL
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", fileName); 
           
			dos = new DataOutputStream(conn.getOutputStream());
 
			dos.writeBytes(twoHyphens + boundary + lineEnd); 
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                    + fileName + "\"" + lineEnd);
           
			dos.writeBytes(lineEnd);
 
			// create a buffer of  maximum size
			bytesAvailable = fileInputStream.available(); 
 
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
 
			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
             
			while (bytesRead > 0) {
               
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
            	bufferSize = Math.min(bytesAvailable, maxBufferSize);
            	bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
             
			}
 
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
          
			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();
            
			Log.i("uploadFile", "HTTP Response is : "
                  + serverResponseMessage + ": " + serverResponseCode);    
           
			//close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();
            
		} catch (Exception e) {}
		}
	
	public static void DownloadCSV(String downloadFilePath,String downloadName,String downloadServerUri) {
		try {
	        //set the download URL, a url that points to a file on the internet
	        //this is the file to be downloaded
	        URL url = new URL(downloadServerUri);

	        //create the new connection
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

	        //set up some things on the connection
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);

	        //and connect!
	        urlConnection.connect();
	        
	        //create a new file, specifying the path, and the filename
	        //which we want to save the file as.
	        File file = new File(downloadFilePath,downloadName);

	        //this will be used to write the downloaded data into the file we created
	        FileOutputStream fileOutput = new FileOutputStream(file);

	        //this will be used in reading the data from the internet
	        InputStream inputStream = urlConnection.getInputStream();

	        //this is the total size of the file
	        int totalSize = urlConnection.getContentLength();
	        //variable to store total downloaded bytes
	        int downloadedSize = 0;

	        //create a buffer...
	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; //used to store a temporary size of the buffer

	        //now, read through the input buffer and write the contents to the file
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                //add the data in the buffer to the file in the file output stream (the file on the sd card
	                fileOutput.write(buffer, 0, bufferLength);
	                //add up the size so we know how much is downloaded
	                downloadedSize += bufferLength;
	                //this is where you would do something to report the prgress, like this maybe
	                //updateProgress(downloadedSize, totalSize);

	        }
	        //close the output stream when done
	        fileOutput.close();
	        Log.v("log_tag", "下載成功");
	//catch some possible errors...
	}catch (IOException e) {
	        e.printStackTrace();
	}
	}
}
