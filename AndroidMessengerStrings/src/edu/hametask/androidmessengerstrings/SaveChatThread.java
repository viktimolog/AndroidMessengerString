package edu.hametask.androidmessengerstrings;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class SaveChatThread implements Runnable
{
	private String filePath;
	private String history;
	
	public SaveChatThread(String filePath, String history)
	{
		this.filePath = filePath;
		this.history = history;
	}

	@Override
	public void run() 
	{
		try {
			PrintWriter pw = new PrintWriter(
											new BufferedOutputStream(
											new FileOutputStream(filePath,true)));
			pw.write(history+"\r\n");
			pw.flush();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
}
