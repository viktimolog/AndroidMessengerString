package edu.hametask.androidmessengerstrings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
	
	public static final int HANDLER_KEYSAVE = 3;
	static int HANDLER_KEYGET = 1;
	static int HANDLER_KEYSEND = 2;
	
	public static Handler h;
	
	private MainClass cipher;
	private Gson gson;
	
	
	private String from, filePath;
	private String to;
	private Socket s;
	private String IPServer;
	
	private DataInputStream dis;
	private DataOutputStream dos;
	
	TextView tvTextPC;
	EditText et;
	Button btnSend;
	EditText etServerName;
	
	SharedPreferences sp;
	
	public void setIPServer(String str)
	{
		IPServer = str;
	}

    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        cipher = new MainClass();
        
        gson = new Gson();
        
	    IPServer="";
        
        filePath = getFilesDir().getPath().toString() + "/MyChat";
        
        tvTextPC = (TextView) findViewById(R.id.tvTextPC);
        et = (EditText) findViewById(R.id.et);
        btnSend = (Button) findViewById(R.id.btnSend);
        
        btnSend.setOnClickListener(this);
        
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
				IPServer = etServerName.getText().toString();
//				IPServer = "10.1.100.78"; не работает, надо отдавать в Handler
				dialog.cancel();
				
				sp = getSharedPreferences("SelectedServer", MODE_PRIVATE);
				Editor edit = sp.edit();
				//edit.putString(MainActivity.KEY, IPServer);
				edit.putString(MainActivity.KEY, "10.1.100.78");
				edit.commit();
				
				  Toast.makeText(getApplicationContext(), IPServer, Toast.LENGTH_LONG).show();
			}
		});
	    
	    alert.setNegativeButton(R.string.oldServer, new DialogInterface.OnClickListener()
        {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				sp = getSharedPreferences("SelectedServer", MODE_PRIVATE);
				IPServer = sp.getString(MainActivity.KEY, "");
				Toast.makeText(getApplicationContext(), IPServer, Toast.LENGTH_LONG).show();
			}
		});
	    
	    AlertDialog ad = alert.create();
	    ad.show();

	    
        try
		{
        	IPServer = "10.1.100.78";
        	
			s = new Socket(IPServer.toString(),3571);
			
			dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
			
		}
		catch (IOException e)
		{
			
			IPServer = "192.168.1.104";
			try 
			{
				s = new Socket(IPServer.toString(),3571);
			} 
			catch (UnknownHostException e1) 
			{
				e1.printStackTrace();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
        
        h = new Handler()
        {
			@Override
			public void handleMessage(Message msg)
			{
				if(msg.what == MainActivity.HANDLER_KEYSAVE)
				{
					try {
						PrintWriter pw = new PrintWriter(
														new BufferedOutputStream(
														new FileOutputStream(filePath,true)));
						pw.write("You wrote: " +  et.getText().toString()+"\r\n"
								+ "PC answered: "+tvTextPC.getText().toString()+"\r\n\n");
						pw.flush();
					} catch (FileNotFoundException e) 
					{
						e.printStackTrace();
					}
				}
				
				if(msg.what == MainActivity.HANDLER_KEYGET)
				{
					MyObject temp = null;
					try 
					{
						from = dis.readUTF();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					
					SealedObject tmp = gson.fromJson(from, SealedObject.class);
					
					try {
						temp = (MyObject)cipher.myDecrypt(tmp);
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//					if(from.equals("Goodbye...")) finish();
//					tvTextPC.setText(from);
					
					if(temp.toString().equals("Goodbye...")) finish();
					tvTextPC.setText(temp.getData());
					
					new Thread(new SaveChatThread()).start();
				}
				
				if(msg.what == MainActivity.HANDLER_KEYSEND)
				{
					SealedObject sObj = null;
					to = et.getText().toString();//+""
					
					MyObject toObj = new MyObject(to);
					
					try {
						sObj = cipher.myEncrypt(toObj);
					} catch (InvalidKeyException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchPaddingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try 
					{
						dos.writeUTF(gson.toJson(sObj));
						dos.flush();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					new Thread(new getThread(MainActivity.this)).start();
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
			new Thread(new SendThread(MainActivity.this)).start();
			break;
		}
		
	}
}
