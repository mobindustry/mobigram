package net.mobindustry.telegram.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.GetStateHandler;
import net.mobindustry.telegram.core.service.CreateGalleryThumbs;
import net.mobindustry.telegram.model.Enums;
import net.mobindustry.telegram.model.holder.DataHolder;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ApiClient.OnApiResultHandler {

    private SplashStart splashStart;

    private boolean stateWaitCode = true;
    private boolean hasAnswer = false;

    private TextView textCheckInternet;

    @Override
    public void onApiResult(BaseHandler output) {
        if (output.getHandlerId() == GetStateHandler.HANDLER_ID) {
            if (((GetStateHandler) output).getResponse() == Enums.StatesEnum.WaitSetPhoneNumber) {
                stateWaitCode = true;
                hasAnswer = true;
            }
            if (((GetStateHandler) output).getResponse() == Enums.StatesEnum.OK) {
                stateWaitCode = false;
                hasAnswer = true;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_menu_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        DataHolder.setThemedContext(getSupportActionBar().getThemedContext());

        Log.e("LOG", "##### Start program #####");
        startService(new Intent(this, CreateGalleryThumbs.class));
        textCheckInternet = (TextView) findViewById(R.id.text_check_internet);

        if (isOnline()) {
            if (DataHolder.isLoggedIn()) {
                hasAnswer = true;
                runStartActivity();
            } else {
                startSplash();
            }
        } else {
            textCheckInternet.setVisibility(View.VISIBLE);
            OnlineCheck onlineCheck = new OnlineCheck();
            onlineCheck.execute();
        }

        FrameLayout layout = (FrameLayout) findViewById(R.id.main_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() && hasAnswer) {
                    if (splashStart != null && !splashStart.isCancelled()) {
                        splashStart.cancel(false);
                    }
                    runStartActivity();
                }
            }
        });
    }

    public void startSplash() {
        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.titanic_tv);
        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);

        new ApiClient<>(new TdApi.AuthGetState(), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        splashStart = new SplashStart();
        splashStart.execute();
    }

    @Override
    public void onBackPressed() {
        if (splashStart != null && !splashStart.isCancelled()) {
            splashStart.cancel(false);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (splashStart != null && !splashStart.isCancelled()) {
            splashStart.cancel(false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(splashStart.isCancelled()) {
            splashStart = new SplashStart();
            splashStart.execute();
            runStartActivity();
        }
    }

    private void runStartActivity() {
        while (true) {
            if (hasAnswer) {
                if (stateWaitCode) {
                    Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
                return;
            }
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
            runStartActivity();
        }
    }

    private class OnlineCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (!isOnline()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("Log", "OnlineCheck task interrupted");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            textCheckInternet.setVisibility(View.GONE);
            startSplash();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}

