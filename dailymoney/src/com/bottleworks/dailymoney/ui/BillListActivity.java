package com.bottleworks.dailymoney.ui;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bottleworks.commons.util.GUIs;
import com.bottleworks.dailymoney.core.R;
import com.bottleworks.dailymoney.data.Detail;

public class BillListActivity extends Activity {
	private ListView Bill_list;
	private ArrayAdapter<String> adapter;
	private String text;
	private JSONArray jsonArray;//server回傳發票號碼
	private JSONObject jsonData;//接收回傳資料用
	private String[] bill;
	private String[] AwardList;
	private int AWARD_STATE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill_list);
		Bill_list = (ListView) findViewById(R.id.listView1);
		adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
		
		load();
		connectDB();
		
		int len = (text.length())/11;
		bill = text.split(",");
		
		

		for(int i = 0;i < len;i++){
			//AwardNum[i] = bill[i].substring(3, 10);
			adapter.add(bill[i]);
		}
	
	
		
		Bill_list.setAdapter(adapter);
		
		Bill_list.setOnItemClickListener(new OnItemClickListener(){
		       public void onItemClick(AdapterView<?> a, View v, int position, long id){
		    	   if(AWARD_STATE == 1){
		          //Toast.makeText(v.getContext(),"你選擇的是"+ bill[position], Toast.LENGTH_SHORT).show();
		          try {
					Toast.makeText(v.getContext(),"特獎1000號碼為"+jsonData.getString("special1000"), Toast.LENGTH_SHORT).show();
		          } catch (JSONException e) {
					// TODO 自動產生的 catch 區塊
					e.printStackTrace();
		          }
		    	   }else{
		    		   Toast.makeText(v.getContext(),"讀取資料中，請稍候再試", Toast.LENGTH_SHORT).show();  
		    	   }
		    	   }
		   });
			
	}
	
    
    private void connectDB(){
    	final GUIs.IBusyRunnable job = new GUIs.BusyAdapter() {
            @Override
            public void run() {
                try {
                	String result = DBConnector.executeQuery("1020708");
                	jsonArray = new JSONArray(result);
                	jsonData = jsonArray.getJSONObject(0);
             	   AWARD_STATE = 1;
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            
        };
        GUIs.doBusy(BillListActivity.this, job);
    }

    
    
    /*
    public void GoGoPowerRanger(int i) throws JSONException {  	
    	
		Toast.makeText(null,"start!!", Toast.LENGTH_SHORT).show();
	
		check2to6(jsonData.getString("first1"),jsonData.getString("first2"),jsonData.getString("first3"),bill[i].substring(2, 10));
		check1(jsonData.getString("special200"),bill[i].substring(2, 10));
		checkBig(jsonData.getString("special1000"),bill[i].substring(2, 10));
		checkBonus(jsonData.getString("six1"),jsonData.getString("six2"),bill[i].substring(2, 10));
}


    
   public void saveAwardNum() throws JSONException{
	   AwardList[0] = jsonData.getString("special1000");
	   AwardList[1] = jsonData.getString("special200");
	   AwardList[2] = jsonData.getString("first1");
	   AwardList[3] = jsonData.getString("first2");
	   AwardList[4] = jsonData.getString("first3");
	   AwardList[5] = jsonData.getString("six1");
	   AwardList[6] = jsonData.getString("six2");
    }
*/
    private int check2to6(String num1 , String num2 , String num3 , String numCheck){
    	//二到六獎
    	int outcome = 7;	
    	for(int i = 5; i >= 1 ; i --){
    		String check1 = num1.substring(i,8);
    		String check2 = num2.substring(i,8);
    		String check3 = num3.substring(i,8);
    		String num = numCheck.substring(i,8);
    		if (num == check1 || num == check2 || num == check3){
    			outcome = (i + 1);
    		}
    	}
    	return outcome;
    }
    private int check1(String num1 , String numcheck){
    	//一獎
    	if(num1 == numcheck){
    		return 1;
    	}else return 7;
    }
    private int checkBig(String num1 , String numcheck){
    	//特獎
    	if(num1 == numcheck){
    		return 0;
    	}else return 7;
    }

    private int checkBonus(String num1 ,String num2, String numcheck){
    	//多的六獎
    	String check = numcheck.substring(5,8);
    	if(num1 == check || num2 == check){
    		return 6;
    	}else return 7;
    }
	
	public void load()
	{
	try {
	FileInputStream inStream=this.openFileInput("BILL'S LIST.txt");
	ByteArrayOutputStream stream=new ByteArrayOutputStream();
	byte[] buffer=new byte[1024];
	int length=-1;
	while((length=inStream.read(buffer))!=-1) {
	stream.write(buffer,0,length);
	}
	stream.close();
	inStream.close();
	
	text = stream.toString();
	//Toast.makeText(BillListActivity.this,"Loaded",Toast.LENGTH_LONG).show();
	} catch (FileNotFoundException e) {
	e.printStackTrace();
	}
	catch (IOException e){
	return ;
	}
	}
	
}