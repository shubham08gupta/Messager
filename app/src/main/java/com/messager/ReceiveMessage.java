package com.messager;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/***
 *
 *  A class which works whenever a new message arrives
 */
public class ReceiveMessage extends BroadcastReceiver {

    private static final String TAG = "RECEIVE MESSAGE";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                String phoneNumber = null, receivedMessage = null;
                for (SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    if (message == null) {
                        Log.e(TAG, "message is null");
                        break;
                    }
                    phoneNumber = message.getDisplayOriginatingAddress();
                    receivedMessage = message.getDisplayMessageBody();
                }

                // to show the message when notification is clicked
                Intent openMessage = new Intent(context, ShowReceivedMessage.class);
                openMessage.putExtra("phoneNumber", phoneNumber);
                openMessage.putExtra("receivedMessage", receivedMessage);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openMessage, 0);
                Notification.Builder builder = new Notification.Builder(context);

                builder.setAutoCancel(true);
                builder.setContentTitle(phoneNumber);
                builder.setContentText(receivedMessage);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentIntent(pendingIntent);
                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                builder.setLights(Color.RED, 3000, 3000);
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = builder.build();
                manager.notify(0, notification);

            }

        } catch (Exception e) {
            Log.e(TAG, "Exception smsReceiver: " + e);

        }
    }
}
