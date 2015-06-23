package net.mobindustry.telegram.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.Enums;
import net.mobindustry.telegram.core.handlers.GetStateHandler;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements ApiClient.OnApiResultHandler {

    private SplashStart splashStart;

    private boolean stateWaitCode = true;

    private TitanicTextView titanicTextView;
    private Titanic titanic;
    private TextView textCheckInternet;

    @Override
    public void onApiResult(BaseHandler output) {

        if (output.getHandlerId() == GetStateHandler.HANDLER_ID) {
            if (((GetStateHandler) output).getResponse() == Enums.StatesEnum.WaitSetPhoneNumber) {
                stateWaitCode = true;
            }
            if (((GetStateHandler) output).getResponse() == Enums.StatesEnum.OK) {
                stateWaitCode = false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textCheckInternet = (TextView) findViewById(R.id.text_check_internet);

        Log.e("LOG", "##### Start program #####");

        if (isOnline()) {
            start();
        } else {
            textCheckInternet.setVisibility(View.VISIBLE);
            OnlineCheck onlineCheck = new OnlineCheck();
            onlineCheck.execute();
        }
    }

    public void start() {
        titanicTextView = (TitanicTextView) findViewById(R.id.titanic_tv);
        titanic = new Titanic();
        titanic.start(titanicTextView);

        new ApiClient<>(new TdApi.AuthGetState(), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        splashStart = new SplashStart();
        splashStart.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (splashStart != null && !splashStart.isCancelled()) {
            splashStart.cancel(false);
        }
    }

    private class SplashStart extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Log.e("Log", "SplashStart task interrupted");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (stateWaitCode) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private class OnlineCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (!isOnline()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("Log", "SplashStart task interrupted");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            textCheckInternet.setVisibility(View.GONE);
            start();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}

