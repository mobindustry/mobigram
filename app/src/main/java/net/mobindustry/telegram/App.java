package net.mobindustry.telegram;

import android.app.Application;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;

import net.mobindustry.telegram.core.handlers.UpdatesHandler;
import net.mobindustry.telegram.model.holder.DataHolder;

import org.drinkless.td.libcore.telegram.TG;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        TG.setUpdatesHandler(new UpdatesHandler(this));
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data" + File.separator + getPackageName();
            TG.setDir(path);
            DataHolder.setCachePath(path);
        }
        DataHolder.setContext(this);
    }
}
