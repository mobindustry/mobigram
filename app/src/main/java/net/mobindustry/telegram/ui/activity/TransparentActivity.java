package net.mobindustry.telegram.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.fragments.NewMessageFragment;
import net.mobindustry.telegram.utils.Const;

public class TransparentActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_activity);

        int choice = getIntent().getIntExtra("choice", 0);

        switch (choice) {
            case Const.NEW_MESSAGE_FRAGMENT:
                NewMessageFragment newMessageFragment = new NewMessageFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.transparent_content, newMessageFragment);
                ft.commit();
                break;

        }
    }
}
