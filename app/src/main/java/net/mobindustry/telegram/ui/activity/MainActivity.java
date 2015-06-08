package net.mobindustry.telegram.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import net.mobindustry.telegram.R;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private SplashStart splashStart;

    private Client client;
    private Client.ResultHandler resultHandler;
    private boolean stateWaitCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
            checkWiFi();
        }
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

    public void checkWiFi() {
        if (!this.isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet is not enable");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
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
