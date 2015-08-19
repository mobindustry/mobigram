package net.mobindustry.mobigram.core.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import net.mobindustry.mobigram.core.NotificationCreator;
import net.mobindustry.mobigram.utils.Const;

public class NotificationsService extends Service {

    private NotificationManager manager;
    private NotificationCreator notificationReceiver;
    private IntentFilter filter;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationReceiver = new NotificationCreator();
        filter = new IntentFilter();
        filter.addAction(Const.NEW_MESSAGE_ACTION);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(notificationReceiver, filter);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

    }
}
