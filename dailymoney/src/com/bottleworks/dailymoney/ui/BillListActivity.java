package com.bottleworks.dailymoney.ui;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bottleworks.dailymoney.core.R;
import com.bottleworks.dailymoney.data.Detail;

public class BillListActivity extends Activity {
	private ListView Bill_list;
	private ArrayAdapter<String> adapter;
	private String text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill_list);
		Bill_list = (ListView) findViewById(R.id.listView1);
		adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
		
		load();
	
		
		Bill_list.setAdapter(adapter);
		
				
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
	Toast.makeText(BillListActivity.this,"Loaded",Toast.LENGTH_LONG).show();
	} catch (FileNotFoundException e) {
	e.printStackTrace();
	}
	catch (IOException e){
	return ;
	}
	}
	
}