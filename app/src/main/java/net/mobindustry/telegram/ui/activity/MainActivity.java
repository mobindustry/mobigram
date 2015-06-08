package net.mobindustry.telegram.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
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

    private String userFirstLastName;
    private String userPhone;
    private TitanicTextView tv;
    private Titanic titanic;


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
        TG.setDir(this.getFilesDir().getPath());
        TG.setUpdatesHandler(resultHandler);

        client = TG.getClientInstance();

        start();
    }

    public void start() {
        if (isOnline()) {
            TitanicTextView tv = (TitanicTextView) findViewById(R.id.titanic_tv);
            Titanic titanic = new Titanic();
            titanic.start(tv);

            client.send(new TdApi.AuthGetState(), resultHandler);

            splashStart = new SplashStart();
            splashStart.execute();
        } else {
            tv = (TitanicTextView) findViewById(R.id.titanic_tv);
            titanic = new Titanic();
            titanic.start(tv);
            textCheckInternet.setVisibility(View.VISIBLE);
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    final String action = intent.getAction();
                    if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                        if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                            textCheckInternet.setVisibility(View.GONE);
                            client.send(new TdApi.AuthGetState(), resultHandler);
                            splashStart = new SplashStart();
                            splashStart.execute();
                            stopReceive();
                        }
                    }
                }
            };
            registerReceiver(receiver, new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));

        }
    }

    public void stopReceive() {
        unregisterReceiver(receiver);
    }

    public static void showActivityAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.anim_scale, R.anim.anim_scale);
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
                showActivityAnimation(MainActivity.this);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                showActivityAnimation(MainActivity.this);
                finish();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

