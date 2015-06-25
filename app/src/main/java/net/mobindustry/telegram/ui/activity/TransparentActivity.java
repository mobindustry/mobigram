package net.mobindustry.telegram.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.fragments.ChooseFileFragment;
import net.mobindustry.telegram.ui.fragments.GalleryFragment;
import net.mobindustry.telegram.ui.fragments.LocationFragment;
import net.mobindustry.telegram.ui.fragments.NewMessageFragment;
import net.mobindustry.telegram.utils.Const;

public class TransparentActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent_activity);

        int choice = getIntent().getIntExtra("choice", 0);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (choice) {
            case Const.NEW_MESSAGE_FRAGMENT:
                NewMessageFragment newMessageFragment = new NewMessageFragment();
                fragmentTransaction.replace(R.id.transparent_content, newMessageFragment);
                break;
            case Const.FILE_CHOOSE_FRAGMENT:
                ChooseFileFragment chooseFileFragment = new ChooseFileFragment();
                fragmentTransaction.replace(R.id.transparent_content, chooseFileFragment);
                break;
            case Const.MAP_FRAGMENT:
                LocationFragment locationFragment = new LocationFragment();
                fragmentTransaction.replace(R.id.transparent_content, locationFragment);
                break;
            case Const.GALLERY_FRAGMENT:
                GalleryFragment galleryFragment = new GalleryFragment();
                fragmentTransaction.replace(R.id.transparent_content, galleryFragment);
                break;
        }
        fragmentTransaction.commit();
    }
}
