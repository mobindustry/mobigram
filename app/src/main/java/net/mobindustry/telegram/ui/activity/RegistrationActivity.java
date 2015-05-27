package net.mobindustry.telegram.ui.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.fragments.RegistrationMainFragment;

public class RegistrationActivity extends AppCompatActivity{

    private Fragment registrationUserPhone;
    private Fragment chooseCountry;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);


        registrationUserPhone=new RegistrationMainFragment();

        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer,registrationUserPhone);
        fragmentTransaction.commit();


    }
}
