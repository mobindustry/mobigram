package net.mobindustry.telegram.ui.activitys;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import net.mobindustry.telegram.ui.fragments.MessagesFragment;

public class MessagesActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            MessagesFragment details = new MessagesFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, details).commit();
        }
    }
}
