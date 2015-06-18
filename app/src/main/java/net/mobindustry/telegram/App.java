package net.mobindustry.telegram;

import android.app.Application;

import net.mobindustry.telegram.core.handlers.UpdatesHandler;
import net.mobindustry.telegram.model.holder.DataHolder;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TG;

public class App extends Application {

    @Override
    public void onCreate() {

        TG.setDir(Const.PATH_TO_NETELEGRAM);
        TG.setUpdatesHandler(new UpdatesHandler(this));

        DataHolder.setContext(this);
    }

}
