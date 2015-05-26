package net.mobindustry.telegram.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import net.mobindustry.telegram.R;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TitanicTextView tv = (TitanicTextView) findViewById(R.id.titanic_tv);
        Titanic titanic = new Titanic();
        titanic.start(tv);

        SplashStart splashStart = new SplashStart();
        splashStart.execute();

    }

    private class SplashStart extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
