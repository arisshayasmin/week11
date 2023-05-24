package com.example.week5_new;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        SmsMessage[] incomingMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        // loop thru message
        for (int i = 0; i < incomingMessage.length; i++)
        {
            SmsMessage current = incomingMessage[i];
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("sendData"); //want to focus on this broadcast
            broadcastIntent.putExtra("bookData", current.getMessageBody()); // message we want to send
            context.sendBroadcast(broadcastIntent);
        }
    }
}
