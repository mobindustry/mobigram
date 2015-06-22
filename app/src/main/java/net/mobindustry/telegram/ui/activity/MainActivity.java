package net.mobindustry.telegram.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
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

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private SplashStart splashStart;

    private Client client;
    private Client.ResultHandler resultHandler;
    private boolean stateWaitCode = true;

    private TitanicTextView tv;
    private Titanic titanic;
    private TextView textCheckInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textCheckInternet = (TextView) findViewById(R.id.text_check_internet);

        Log.e("LOG", "##### Start program #####");

        resultHandler = new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

                if (object instanceof TdApi.AuthStateOk) {
                    stateWaitCode = false;
                }

                if (object instanceof TdApi.AuthStateWaitSetPhoneNumber) {
                    stateWaitCode = true;
                }
            }
        };
        client = TG.getClientInstance();

        if (isOnline()) {
            start();
        } else {
            textCheckInternet.setVisibility(View.VISIBLE);
            OnlineCheck onlineCheck = new OnlineCheck();
            onlineCheck.execute();
        }
    }

    public void start() {
        tv = (TitanicTextView) findViewById(R.id.titanic_tv);
        titanic = new Titanic();
        titanic.start(tv);

        client.send(new TdApi.AuthGetState(), resultHandler);
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

