package net.mobindustry.telegram.core.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import net.mobindustry.telegram.core.NotificationCreator;
import net.mobindustry.telegram.core.handlers.UpdatesHandler;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TG;

public class NotificationsService extends Service {

    private NotificationManager manager;
    private NotificationCreator notificationReceiver;
    private IntentFilter filter;

    @Override
    public void onCreate() {
        super.onCreate();
        TG.setUpdatesHandler(new UpdatesHandler(this));

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
