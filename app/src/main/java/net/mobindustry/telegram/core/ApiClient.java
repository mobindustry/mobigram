package net.mobindustry.telegram.core;

import android.os.AsyncTask;
import android.util.TimeUtils;

import net.mobindustry.telegram.core.handlers.BaseHandler;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.concurrent.TimeUnit;

public class ApiClient<TFunction extends TdApi.TLFunction, THandler extends BaseHandler> extends AsyncTask<Void, Integer, THandler> {

    private static final long THREAD_SLEEP_TIME = 50;
    private static final int TIMEOUT = 30;
    private TFunction function;
    private THandler handler;
    private OnApiResultHandler resultHandler;

    public ApiClient(TFunction func, THandler handl, OnApiResultHandler listener) {
        function = func;
        handler = handl;
        resultHandler = listener;
    }

    @Override
    protected THandler doInBackground(Void... params) {
        Client client = TG.getClientInstance();
        if (client != null) {
            client.send(function, handler);
            for (int i = 0; i < TIMEOUT; i++) {
                if (handler.hasAnswer()) {
                    return handler;
                }
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }
        return null;
    }


    @Override
    protected void onPostExecute(THandler tOutput) {
        super.onPostExecute(tOutput);
        if (resultHandler != null) {
            resultHandler.onApiResult(tOutput);
        }
    }

    public interface OnApiResultHandler {
        void onApiResult(BaseHandler output);
    }
}

