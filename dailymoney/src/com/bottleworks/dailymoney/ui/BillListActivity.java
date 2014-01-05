/**
 * 102522088 ���ؾ�
 * 102522013 ���ۺ�
 * 102522030 �����W
 * 102522108 �����
 */
package com.bottleworks.dailymoney.ui;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.bottleworks.commons.util.GUIs;
import com.bottleworks.dailymoney.context.ContextsActivity;
import com.bottleworks.dailymoney.core.R;
import com.bottleworks.dailymoney.data.Detail;
import com.bottleworks.dailymoney.data.IDataProvider;

@SuppressLint("HandlerLeak")
public class BillListActivity extends ContextsActivity  {
	protected static final int ID_USER = 0;
	private ListView Bill_list;
	private ArrayAdapter<String> adapter;
	private JSONArray jsonArray78;//server�^�ǵo�����X
	private JSONObject jsonData78;//�����^�Ǹ�ƥ�
	private JSONArray jsonArray910;//server�^�ǵo�����X
	private JSONObject jsonData910;//�����^�Ǹ�ƥ�
	private String[] bill;
	private String[] billcheck;
	private String[] check78 = new String[8];
	private String[] check910 = new String[8];
	private int AWARD_STATE = 0;
	private int WRITE_STATE = 0;
	private int[] award;
	private int count=0;
	//�ʱ�DB�O�_�w����
	private Handler mHandler;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill_list);
		adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
		Bill_list = (ListView) findViewById(R.id.listView1);           	
		mHandler = new Handler();
        mHandler.post(runnable);
		
		
		connectDB();	
     		
	}
	
    
    private void connectDB(){
    	final GUIs.IBusyRunnable job = new GUIs.BusyAdapter() {
            @Override
            public void run() {
                try {
                	String result78 = DBConnector.executeQuery("1020708");
                	jsonArray78 = new JSONArray(result78);
                	jsonData78 = jsonArray78.getJSONObject(0);
                	check78[0] = jsonData78.getString("first1");
                	check78[1] = jsonData78.getString("first2");
                	check78[2] = jsonData78.getString("first3");
                	check78[3] = jsonData78.getString("special200");
                	check78[4] = jsonData78.getString("special1000");
                	check78[5] = jsonData78.getString("six1");
                	check78[6] = jsonData78.getString("six2");
                	
                	String result910 = DBConnector.executeQuery("1020910");
                	jsonArray910 = new JSONArray(result910);
                	jsonData910 = jsonArray910.getJSONObject(0);
                	check910[0] = jsonData910.getString("first1");
                	check910[1] = jsonData910.getString("first2");
                	check910[2] = jsonData910.getString("first3");
                	check910[3] = jsonData910.getString("special200");
                	check910[4] = jsonData910.getString("special1000");
                	check910[5] = jsonData910.getString("six1");
                	check910[6] = jsonData910.getString("six2");
                	
                	
             	   	AWARD_STATE = 1;
             	    Log.v("", "DB done");
         	    
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            
        };
        GUIs.doBusy(BillListActivity.this, job);       
    }
    
    final Runnable runnable = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            // �ݭn�I���@����
        	Log.v("", "runnable");
        	while(true){
        	if (AWARD_STATE == 1) {
        		Log.v("", "�i�h�F!!");
        		writeList();
        		break;
            }
        	}
        	
        	Bill_list.setAdapter(adapter);
        	
        }
    };
    
    private void  writeList(){
    	Log.v("", "Q___Q");
		
		WRITE_STATE = 1;
		
		Log.v("", "I'm In~~~");

			List<Detail> data = null;
			final IDataProvider idp = getContexts().getDataProvider();
			data = idp.listDetail(null,null,getContexts().getPrefMaxRecords());
			bill = new String[data.size()];
			award = new int[data.size()];
			billcheck = new String[data.size()];
	          Log.v("", "FUCK YOU");
	          
	          for (Detail det : data) {	
	  			det.getQR();
	  			
	              if(!"".equals(det.getQR().trim())){
	            	  billcheck[count] = det.getQR();  
	              	bill[count] = det.getDate()+"  " + det.getQR(); 
	              	bill[count] = bill[count].substring(0, 10)+"  "+bill[count].substring(30);
	              	
	              	if(bill[count].substring(4, 7).equals("Jul")||bill[count].substring(4, 7).equals("Aug")){
	  	        	  //Toast.makeText(v.getContext(),"start!!", Toast.LENGTH_SHORT).show();
	  	        	  award[count] = check2to6(check78[0],check78[1],check78[2],billcheck[count].substring(2, 10));
	  	        	  if(check1(check78[3],billcheck[count].substring(2, 10)) != 7)
	  	        	  award[count] = check1(check78[3],billcheck[count].substring(2, 10));
	  	        	  else if(checkBig(check78[4],billcheck[count].substring(2, 10)) != 7)
	  	        	  award[count] = checkBig(check78[4],billcheck[count].substring(2, 10));
	  	        	  else if(checkBonus(check78[5],check78[6],billcheck[count].substring(2, 10)) != 7)
	  	        	  award[count] = checkBonus(check78[5],check78[6],billcheck[count].substring(2, 10));
	              	}else if(bill[count].substring(4, 7).equals("Sep")||bill[count].substring(4, 7).equals("Oct")){
	              		
	              		  award[count] = check2to6(check910[0],check910[1],check910[2],billcheck[count].substring(2, 10));
		  	        	  if(check1(check910[3],billcheck[count].substring(2, 10)) != 7)
		  	        	  award[count] = check1(check910[3],billcheck[count].substring(2, 10));
		  	        	  else if(checkBig(check910[4],billcheck[count].substring(2, 10)) != 7)
		  	        	  award[count] = checkBig(check910[4],billcheck[count].substring(2, 10));
		  	        	  else if(checkBonus(check910[5],check910[6],billcheck[count].substring(2, 10)) != 7)
		  	        	  award[count] = checkBonus(check910[5],check910[6],billcheck[count].substring(2, 10));
	              	}else{
	              		bill[count] = bill[count] + "  �|���}��";
	              	}
	              	
	              		if(award[count] == 1)
	            		bill[count] = bill[count] + "  ���A���o����200�U!!";
	            		else if(award[count] == 2)
	            		bill[count] = bill[count] + "  �K�K�A���o���� 4�U";
	            		else if(award[count] == 3)
	            		bill[count] = bill[count] + "  �K�K�A���o���� 1�U";
	            		else if(award[count] == 4)
	            		bill[count] = bill[count] + "  �K�K�A���o���� 4000��";
	            		else if(award[count] == 5)
	            		bill[count] = bill[count] + "  �K�K�A���o���� 1000��";
	            		else if(award[count] == 6)
	            		bill[count] = bill[count] + "  �K�K�A���o���� 200��";
	            		else if(award[count] == 7)
	                	bill[count] = bill[count] + "  Q�fQ�S����";
	            		else if(award[count] == 8)
	                	bill[count] = bill[count] + "  YOOOOOO 1000�U!!";
	            		else if(award[count] == 9)
		                bill[count] = bill[count] + "  �K�K�A���o���� 20�U";
	             	
	              	count ++;
	              }             
	          }
		
		if(WRITE_STATE == 1){
			Log.v("", "YOOOOOOOOOOOOO");
			for(int i =0;i<count;i++)
   			adapter.add(bill[i]);
		}else{
			Log.v("", "NOOOOOOOOOOOOO");
			Toast.makeText(Bill_list.getContext(),"Q__QŪ����Ƥ��A�еy�ԦA��", Toast.LENGTH_SHORT).show();
		}
		
     	    Log.v("", "�A���\���A��");
     	    
    }
    
    private int check2to6(String num1 , String num2 , String num3 , String numCheck){
    	//�G�줻��
    	int outcome = 7;	
    	for(int i = 0; i <= 5 ; i ++ ){
    		String check1 = num1.substring(i,8);
    		String check2 = num2.substring(i,8);
    		String check3 = num3.substring(i,8);
    		String num = numCheck.substring(i,8);
    		if (num.equals(check1)|| num.equals(check2) || num.equals(check3)){
    			 if(i == 0){
    			     outcome = 9;
    			     break;
    			       }else{
    			       outcome = (i + 1);
    			       break;
    			          }
    		}
    	}
    	return outcome;
    }
    private int check1(String num1 , String numcheck){
    	//�@��
    	if(num1.equals(numcheck)){
    		return 1;
    	}else return 7;
    }
    private int checkBig(String num1 , String numcheck){
    	//�S��
    	if(num1.equals(numcheck)){
    		return 8;
    	}else return 7;
    }
    
    
    private int checkBonus(String num1 ,String num2, String numcheck){
    	//�h������
    	String check = numcheck.substring(5,8);
    	if(num1.equals(check)|| num2.equals(check)){
    		return 6;
    	}else return 7;
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mHandler != null) {
            mHandler.removeCallbacks(runnable);
        }
    }
	
	
}