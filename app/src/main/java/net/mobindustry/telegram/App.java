package net.mobindustry.telegram;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import net.mobindustry.telegram.core.handlers.UpdatesHandler;
import net.mobindustry.telegram.model.holder.DataHolder;

import org.drinkless.td.libcore.telegram.TG;

import java.io.File;

public class App extends Application {

    @Override
    public void onCreate() {
        Fabric.with(this, new Crashlytics());

        TG.setUpdatesHandler(new UpdatesHandler(this));
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            TG.setDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NeTelegram");
        } //TODO else!!!
        DataHolder.setContext(this);
    }
}
