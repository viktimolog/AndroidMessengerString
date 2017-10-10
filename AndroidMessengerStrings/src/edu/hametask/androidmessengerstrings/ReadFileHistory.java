package edu.hametask.androidmessengerstrings;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

import android.widget.Toast;

public class ReadFileHistory implements Runnable
{
	private String filePath;
	private String history;
	
	public ReadFileHistory(String filePath)
	{
		this.filePath = filePath;
		this.history = "";
	}

	@Override
	public void run()
	{
		try 
		{
			Scanner sc = new Scanner(new FileInputStream(filePath));
			while(sc.hasNextLine())
			{
				history+=sc.nextLine()+"\n";
			}
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		MainActivity.h.sendMessage(
				MainActivity.h.obtainMessage(
						MainActivity.HANDLER_KEYREADHISTORY, history.toString()));
	}
}
