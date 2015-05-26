package net.mobindustry.telegram;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class RegistrationActivity extends Activity{

    private Fragment registrationUserPhone;
    private Fragment chooseCountry;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);




    }
}
