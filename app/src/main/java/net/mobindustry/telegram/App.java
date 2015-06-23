package net.mobindustry.telegram;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import net.mobindustry.telegram.core.handlers.UpdatesHandler;
import net.mobindustry.telegram.model.holder.DataHolder;

import org.drinkless.td.libcore.telegram.TG;

public class App extends Application {

    @Override
    public void onCreate() {
        Fabric.with(this, new Crashlytics());

        TG.setDir(getFilesDir().toString());
        TG.setUpdatesHandler(new UpdatesHandler(this));

        DataHolder.setContext(this);
    }

}
