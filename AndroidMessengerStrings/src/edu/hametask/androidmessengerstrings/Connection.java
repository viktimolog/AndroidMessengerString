package edu.hametask.androidmessengerstrings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

public class Connection 
{
	private MainClass cipher;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket s;
	private String ipServer;
	private int port;
	private String message;

	public Connection(Socket s, String ipServer, int port)
	{
		this.s = s;
		this.ipServer = ipServer;
		this.port = port;
		cipher = new MainClass();
	}
	
	
	public void createSocket()
	{
		try 
		{
			s = new Socket(InetAddress.getByName(ipServer),port);
			dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public MainClass getCipher() {
		return cipher;
	}


	public void setCipher(MainClass cipher) {
		this.cipher = cipher;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public DataInputStream getDis() {
		return dis;
	}


	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}


	public DataOutputStream getDos() {
		return dos;
	}


	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}


	public String getIPServer() {
		return ipServer;
	}

	public void setIPServer(String ipServer) {
		this.ipServer = ipServer;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}
}
