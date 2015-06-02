package net.mobindustry.telegram.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import net.mobindustry.telegram.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private SplashStart splashStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TitanicTextView tv = (TitanicTextView) findViewById(R.id.titanic_tv);
        Titanic titanic = new Titanic();
        titanic.start(tv);

        splashStart = new SplashStart();
        splashStart.execute();

    }

    public static void showActivityAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
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
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Log.i("MainActivityTag", "SplashStart task interrupted");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(MainActivity.this, ChatActivity .class);
            startActivity(intent);
            showActivityAnimation(MainActivity.this);
            finish();
        }
    }
}
