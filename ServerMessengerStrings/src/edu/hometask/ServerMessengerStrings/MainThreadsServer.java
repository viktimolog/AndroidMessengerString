package edu.hometask.ServerMessengerStrings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import edu.hometask.ServerMessengerStrings.ThreadsServer;

public class MainThreadsServer 
{

	public static void main(String[] args)
	{
		System.out.println("starting server on port 3571");
		Scanner sc = new Scanner(System.in);
		ServerSocket ss = null;
		try
		{
			ss = new ServerSocket(3571);
		}
		catch (IOException e)
		{
			System.out.println("Please select new port. this one is busy...");
			int port = sc.nextInt();
			try
			{
				ss = new ServerSocket(port);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

		while(true)
		{
			/*if(i%9==0) 
			{
				System.out.println("do you want to continue? y/n");
				
			}*/
			try
			{
				Socket s = ss.accept();
				ThreadsServer thS = new ThreadsServer(s);
				Thread t = new Thread(thS);
				t.start();
//				i++;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

}
