package edu.hametask.androidmessengerstrings;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import com.google.gson.Gson;

public class getThread implements Runnable
{
	private Connection con;
	private Gson gson;
	private String from;
	
	public getThread(Connection con)
	{
		this.con = con;
		gson = new Gson();
	}
	@Override
	public void run()
	{
		MyObject res = null;
		SealedObject tmp = null;
		try 
		{
			while ((from = con.getDis().readUTF()) != null) 
			{
				tmp = gson.fromJson(from, SealedObject.class);
				res = (MyObject)con.getCipher().myDecrypt(tmp);
				
				MainActivity.h.sendMessage(
						MainActivity.h.obtainMessage(
								MainActivity.HANDLER_KEYGET, res.getData().toString()));
			}
		} 
		catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) 
		{
			e.printStackTrace();
		}
	}
}
