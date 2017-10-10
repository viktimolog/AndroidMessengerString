package edu.hametask.androidmessengerstrings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.Scanner;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	static String KEY = "mySPkey";
	
	public static final int ChoiceIPServer = 456789;
	public static final int HANDLER_KEYGET = 34451;
	public static final int HANDLER_KEYSEND = 6763092;
	public static final int HANDLER_KEYREADHISTORY = 3112092;
	
	public static Handler h;
	
	private edu.hametask.androidmessengerstrings.Connection con;
	
	private String filePath;
	
	TextView tvTextPC;
	EditText et;
	Button btnSend, btnReadHistory;
	EditText etServerName;
	
	SharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        filePath = getFilesDir().getPath().toString() + "/MyChat";
        
     /*   File file = new File(filePath);
        file.delete();*/
        
        tvTextPC = (TextView) findViewById(R.id.tvTextPC);
        et = (EditText) findViewById(R.id.et);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnReadHistory = (Button) findViewById(R.id.btnReadHistory);
        
        btnSend.setOnClickListener(this);
        btnReadHistory.setOnClickListener(this);
        
        con = new edu.hametask.androidmessengerstrings.Connection(null, "", 3571);
        
	    View view = View.inflate(this, R.layout.serverchoice, null);
	    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
	    alert.setView(view);
	    alert.setTitle(R.string.serverchoicetitle);
//	    alert.setMessage(R.string.personName);
	    alert.setCancelable(false);
	    
	    etServerName = (EditText) view.findViewById(R.id.dialog1EditText);
	    
	    alert.setPositiveButton(R.string.newServer, new DialogInterface.OnClickListener()
        {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				MainActivity.h.sendMessage(
						MainActivity.h.obtainMessage(
								MainActivity.ChoiceIPServer, etServerName.getText().toString()));
				dialog.cancel();
			}
		});
	    
	    alert.setNegativeButton(R.string.oldServer, new DialogInterface.OnClickListener()
        {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				sp = getSharedPreferences("SelectedServer", MODE_PRIVATE);
				con.setIPServer(sp.getString(MainActivity.KEY, ""));
				
				Toast.makeText(getApplicationContext(), con.getIPServer(), Toast.LENGTH_LONG).show();
				
				   new Thread(new CreateSocketThread(con)).start(); 
			}
		});
	    
	    AlertDialog ad = alert.create();
	    ad.show();

        h = new Handler()
        {
			@Override
			public void handleMessage(Message msg)
			{
				if(msg.what == MainActivity.HANDLER_KEYREADHISTORY)
				{
					tvTextPC.setText(msg.obj.toString());
				}
				
				if(msg.what == MainActivity.ChoiceIPServer)
				{
					con.setIPServer(msg.obj.toString());
					
					sp = getSharedPreferences("SelectedServer", MODE_PRIVATE);
					Editor edit = sp.edit();
					edit.putString(MainActivity.KEY, con.getIPServer());
					edit.commit();
					
					  Toast.makeText(getApplicationContext(), con.getIPServer(), Toast.LENGTH_LONG).show();
					  
					   new Thread(new CreateSocketThread(con)).start(); 
				}
				
				if(msg.what == MainActivity.HANDLER_KEYGET)
				{
					if(msg.obj.toString().equals("Goodbye...")) finish();
					
					new Thread(new SaveChatThread(filePath, msg.obj.toString())).start();
					
					String tmp = tvTextPC.getText().toString();
					tmp+="\n"+"Decrypt res: " + msg.obj.toString();
					tvTextPC.setText(tmp);
				}
				
				if(msg.what == MainActivity.HANDLER_KEYSEND)
				{
					String tmp = tvTextPC.getText().toString();
					tmp+="\n"+"Encrypt res: " + msg.obj.toString();
					tvTextPC.setText(tmp);
					
					et.setText("");
					
					new Thread(new getThread(con)).start();
				}
			}
        	
        };
    }

	@Override
	public void onClick(View v) 
	{
		
		switch(v.getId())
		{
		case R.id.btnSend:
			con.setMessage(et.getText().toString());
			new Thread(new SaveChatThread(filePath, con.getMessage())).start();
			new Thread(new SendThread(con)).start();
			break;
		case R.id.btnReadHistory:
			new Thread(new ReadFileHistory(filePath)).start();
			break;			
		}
	}
}
