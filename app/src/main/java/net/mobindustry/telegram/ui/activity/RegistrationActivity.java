package net.mobindustry.telegram.ui.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;


import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.fragments.RegistrationMainFragment;
import net.mobindustry.telegram.utils.CountryObject;

public class RegistrationActivity extends AppCompatActivity {

    private Fragment registrationUserPhone;
    private FragmentTransaction fragmentTransaction;
    private CountryObject countryObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        registrationUserPhone = new RegistrationMainFragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, registrationUserPhone);
        fragmentTransaction.commit();

    }

    public CountryObject getCountryObject() {
        return countryObject;
    }

    public void setCountryObject(CountryObject countryObject) {
        this.countryObject = countryObject;
    }
}
