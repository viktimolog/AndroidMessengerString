package edu.hametask.androidmessengerstrings;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import com.google.gson.Gson;

public class SendThread  implements Runnable
{
	private Connection con;
	private Gson gson;
	
	public SendThread(Connection con)
	{
		this.con = con;
		gson = new Gson();
	}
	@Override
	public void run() 
	{
		SealedObject sObj = null;
		MyObject toObj = new MyObject(con.getMessage());
		
		try
		{
			sObj = con.getCipher().myEncrypt(toObj);
		}
		catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e1)
		{
			e1.printStackTrace();
		}
		
		try 
		{
			con.getDos().writeUTF(gson.toJson(sObj)); 
			con.getDos().flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		MainActivity.h.sendMessage(
				MainActivity.h.obtainMessage(
						MainActivity.HANDLER_KEYSEND, sObj.toString()));
	}
}
