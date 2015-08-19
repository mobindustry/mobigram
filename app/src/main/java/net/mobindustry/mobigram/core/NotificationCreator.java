package net.mobindustry.mobigram.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import net.mobindustry.mobigram.model.holder.DataHolder;
import net.mobindustry.mobigram.ui.activity.ChatActivity;
import net.mobindustry.mobigram.utils.Const;

public class NotificationCreator extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context);
        //TODO delete notifications
        if (!DataHolder.isActive() && intent.getAction().equals(Const.NEW_MESSAGE_ACTION)) {
            int id = intent.getIntExtra("message_id", 0);
            Log.e("Log", "Notification creator id " + id);
            long chat_id = intent.getLongExtra("chatId", 0);
            String message = intent.getStringExtra("message");
            Intent messageIntent = new Intent(context, ChatActivity.class);
            messageIntent.putExtra("chatId", chat_id);
            messageIntent.putExtra("message_id", id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, messageIntent, 0);

            builder.setSmallIcon(android.R.drawable.ic_dialog_email)
                    .setTicker("Message")
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setVibrate(new long[]{1000})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle("New Message")
                    .setContentText(message)
                    .setContentInfo("info")
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) chat_id, notification);
        }
    }
}
