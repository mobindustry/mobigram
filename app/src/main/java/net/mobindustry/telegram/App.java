package net.mobindustry.telegram;

import android.app.Application;

import net.mobindustry.telegram.model.holder.DataHolder;

public class App extends Application {

    @Override
    public void onCreate() {
        DataHolder.setContext(this);
    }

}
