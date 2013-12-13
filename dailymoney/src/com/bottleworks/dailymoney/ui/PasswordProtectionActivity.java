package com.bottleworks.dailymoney.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
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
 * @author dennis
 *
 */
public class PasswordProtectionActivity extends ContextsActivity implements OnClickListener{
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
        String pd = ((TextView)findViewById(R.id.pdprot_text)).getText().toString();
        if(password.equals(pd)){
           setResult(RESULT_OK);
           finish();
        }else{
            GUIs.shortToast(this,R.string.msg_wrong_password);
        }
    }
    
    private void ForgotPassword() {
    	AlertDialog.Builder dialog = new AlertDialog.Builder(PasswordProtectionActivity.this);
        dialog.setTitle("忘記密碼了！");
        dialog.setMessage("請確認手機有可以使用網路，程式會寄送一串編碼至此Android手機的Google帳號信箱中，確定寄送？");
        dialog.setPositiveButton(R.string.cact_ok,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialoginterface, int i){		            		
            	terminal=0;
                }
            });
        dialog.setNegativeButton(R.string.cact_cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialoginterface, int i){
                    }
            });
        dialog.show();
    }
    
    private int terminal=1;
    
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
    						SendForgotEmail.executeSend(account.name);
    						terminal=1;
    					}
    				}catch (Exception e) { 
    					e.printStackTrace(); 
    				} 
    			}
    		}
    	} 
    	});
}
