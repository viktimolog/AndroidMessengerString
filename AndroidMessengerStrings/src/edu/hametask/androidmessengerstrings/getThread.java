package edu.hametask.androidmessengerstrings;

import android.os.Message;
import android.widget.Toast;

public class getThread implements Runnable
{
	private MainActivity ma;
	public getThread(MainActivity ma)
	{
		this.ma = ma;
	}
	@Override
	public void run()
	{
		Message msg = MainActivity.h.obtainMessage();
		msg.what = MainActivity.HANDLER_KEYGET;
		MainActivity.h.sendMessageDelayed(msg,0);
		
	}

}
