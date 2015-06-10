package net.mobindustry.telegram.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.mobindustry.telegram.ui.fragments.MessagesFragment;
import net.mobindustry.telegram.model.Contact;

public class MessagesActivity extends AppCompatActivity {
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

            Contact contact = (Contact)getIntent().getSerializableExtra("contact");

//            details.setList(contact.getList());
          //  details.setDataForToolbar(contact);
        }
    }
}
