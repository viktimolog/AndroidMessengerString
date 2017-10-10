package edu.hometask.ServerMessengerStrings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;


public class ThreadsServer implements Runnable
{
	private Socket s;

	public ThreadsServer(Socket s)
	{
		this.s = s;
	}

	public void run() 
	{
		try
		{
			DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
			
			String chat="";
			
			while(!chat.equals("exit"))
			{
				chat=dis.readUTF();
				System.out.println(chat);
				if(!chat.equals("exit"))
				{
				dos.writeUTF(chat);
				dos.flush();
				}
			}
			dos.writeUTF("Goodbye...");
			dos.flush();
			s.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
