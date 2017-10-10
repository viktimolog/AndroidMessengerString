package edu.hametask.androidmessengerstrings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;

import com.google.gson.Gson;

public class CreateSocketThread implements Runnable
{
	private edu.hametask.androidmessengerstrings.Connection con;
	
	public CreateSocketThread(edu.hametask.androidmessengerstrings.Connection con)
	{
		this.con = con;
	}

	@Override
	public void run() 
	{
        	con.createSocket();
        
/*        MainActivity.hMain.sendMessage(
				MainActivity.hMain.obtainMessage(
						MainActivity.HANDLER_KEYCREATESOCKET, con.getS()));*/
	}

}
