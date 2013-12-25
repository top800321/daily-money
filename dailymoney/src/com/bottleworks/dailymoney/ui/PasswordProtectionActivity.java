package com.bottleworks.dailymoney.ui;

import java.util.Random;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bottleworks.commons.util.GUIs;
import com.bottleworks.dailymoney.context.ContextsActivity;
import com.bottleworks.dailymoney.core.R;

/**
 * 
 * loginID : top800321
 * @author 軟體工程 102522030 王竣鋒
 *
 */
public class PasswordProtectionActivity extends ContextsActivity implements OnClickListener{
	
	private int terminal=1;
	private String backuppassword;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdprotection);
        findViewById(R.id.pdprot_ok).setOnClickListener(this);
        findViewById(R.id.pdprot_forgot).setOnClickListener(this);
        mThread.start();
    }
    

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pdprot_ok) {
            doPasswordOk();
        }
        else if(v.getId() == R.id.pdprot_forgot) {
            ForgotPassword();
        }
    }

    private void doPasswordOk() {
        String password = getContexts().getPrefPassword();
        String passcode = getContexts().getBackUpPassword();
        
        String pd = ((TextView)findViewById(R.id.pdprot_text)).getText().toString();
        
        if(password.equals(pd)){
           setResult(RESULT_OK);
           finish();
        }
        else if(passcode.equals(pd)){
        	setResult(RESULT_OK);
        	GUIs.shortToast(this,R.string.msg_password_reset);
            finish();
        }
        else{
            GUIs.shortToast(this,R.string.msg_wrong_password);
        }
    }
    
    private void ForgotPassword() {
    	AlertDialog.Builder dialog = new AlertDialog.Builder(PasswordProtectionActivity.this);
        dialog.setTitle("忘記密碼了！");
        dialog.setMessage("請確認手機有可以使用網路，程式會寄送一串編碼至此Android手機的Google帳號信箱中，確定寄送？");
        dialog.setPositiveButton(R.string.cact_ok,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialoginterface, int i){
            	RandomCode();
            	getContexts().reloadBackUpPassword(backuppassword);
            	terminal=0;
                }
            });
        dialog.setNegativeButton(R.string.cact_cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialoginterface, int i){
                    }
            });
        dialog.show();
    }
    
	Thread mThread = new Thread(new Runnable() { 
     	 
    	public void run() { 
    		while(true){
    			if(terminal!=1){
    				try{ 
    					AccountManager accountManager = AccountManager.get(getBaseContext());
    					// 取得指定 type 的 Account
    					Account[] accounts = accountManager.getAccountsByType("com.google");
    					for(Account account : accounts){
    						//Log.i("--Get Account Example--", account.name);
    						//Log.i("--Get Account Example--", account.type);
    						SendForgotEmail.executeSend(account.name,backuppassword);
    						terminal=1;
    					}
    				}catch (Exception e) { 
    					e.printStackTrace(); 
    				} 
    			}
    		}
    	} 
    	});
	
	private void RandomCode(){
		Random generator = new Random();
	    StringBuilder randomStringBuilder = new StringBuilder();
	    char tempChar;
	    for (int i = 0; i < 8; i++){
	        tempChar = (char) (generator.nextInt(96) + 32);
	        randomStringBuilder.append(tempChar);
	    }
	    backuppassword = randomStringBuilder.toString();
	}
}
