package edu.hametask.androidmessengerstrings;

import android.os.Message;

public class SendThread  implements Runnable
{
	private MainActivity ma;
	public SendThread(MainActivity ma)
	{
		this.ma = ma;
	}
	@Override
	public void run() 
	{
		Message msg = MainActivity.h.obtainMessage();
		msg.what = MainActivity.HANDLER_KEYSEND;
		MainActivity.h.sendMessageDelayed(msg,0);
	}

}
