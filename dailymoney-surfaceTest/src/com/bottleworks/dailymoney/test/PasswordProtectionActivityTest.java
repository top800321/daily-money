package com.bottleworks.dailymoney.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.app.Instrumentation;
import android.app.AlertDialog;

import com.bottleworks.dailymoney.ui.PasswordProtectionActivity;
import com.bottleworks.dailymoney.core.R;

import android.widget.Button;
import android.widget.TextView;

public class PasswordProtectionActivityTest 
	extends ActivityInstrumentationTestCase2<PasswordProtectionActivity> 
{
	private PasswordProtectionActivity PPActivity;
	private Button oButton;
	private Button fButton;
	private TextView mText;
 
	public final static String Password = "heyhey";
 
	public PasswordProtectionActivityTest()
	{
		super(PasswordProtectionActivity.class);
	}
 
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
  
		setActivityInitialTouchMode(false);
  
		PPActivity = getActivity();
		oButton = (Button) PPActivity.findViewById(R.id.pdprot_ok);
		fButton = (Button) PPActivity.findViewById(R.id.pdprot_forgot);
		mText = (TextView) PPActivity.findViewById(R.id.pdprot_text);
	}
 
	public void testPreconditions()
	{
		assertTrue(mText.getText() != null);
	}
	
	@UiThreadTest
	public void testButton()
	{
		oButton.performClick();
		fButton.performClick();
	}
	@UiThreadTest
	public void testPassword()
	{
		mText.setText(Password);
		assertEquals(Password, mText.getText().toString());	
	}
}