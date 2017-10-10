package edu.hametask.androidmessengerstrings;

import android.os.Message;

public class SaveChatThread implements Runnable
{

	@Override
	public void run() 
	{
		Message msg = MainActivity.h.obtainMessage();
		msg.what = MainActivity.HANDLER_KEYSAVE;
		MainActivity.h.sendMessageDelayed(msg,0);
	}

}
