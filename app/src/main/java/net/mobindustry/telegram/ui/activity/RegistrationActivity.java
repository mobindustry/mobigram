package net.mobindustry.telegram.ui.activity;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import net.mobindustry.telegram.ui.fragments.ReseiverCodeFragment;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.utils.ListCountryObject;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

public class RegistrationActivity extends AppCompatActivity {

    private Fragment registrationUserPhone;
    private Fragment reseiverCodeFragment;
    private FragmentTransaction fragmentTransaction;
    private CountryObject countryObject;
    private ListCountryObject listCountryObject;
    private Client client;
    private Client.ResultHandler handler;
    private String phoneForServer = "";
    private String codeFromServer = "";

    public String getCodeFromServer() {
        return codeFromServer;
    }

    public void setCodeFromServer(String codeFromServer) {
        this.codeFromServer = codeFromServer;
    }

    public String getPhoneForServer() {
        return phoneForServer;
    }

    public void setPhoneForServer(String phoneForServer) {
        this.phoneForServer = phoneForServer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        handler = new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.Error) {
                    TdApi.Error error = (TdApi.Error) object;
                    if ((error.code == 400 && error.text.equals("PHONE_NUMBER_INVALID:"))) {

                    }
                    if ((error.code == 400 && error.text.equals("PHONE_NUMBER_INVALID:"))) {

                    }
                    if ((error.code == 400 && error.text.equals("PHONE_NUMBER_INVALID:"))) {

                    }
                    if ((error.code == 400 && error.text.equals("PHONE_NUMBER_INVALID:"))) {

                    }
                    if ((error.code == 400 && error.text.equals("PHONE_NUMBER_INVALID:"))) {

                    }
                }
                if (object instanceof TdApi.AuthStateWaitSetPhoneNumber) {
                    registrationUserPhone = new RegistrationMainFragment();
                    fragmentTransaction.add(R.id.fragmentContainer, registrationUserPhone);
                    fragmentTransaction.commit();
                    if (!getPhoneForServer().isEmpty()) {
                        client.send(new TdApi.AuthSetPhoneNumber(getPhoneForServer()), handler);
                    }
                }
                if (object instanceof TdApi.AuthSetCode) {
                    reseiverCodeFragment = new ReseiverCodeFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, reseiverCodeFragment);
                    fragmentTransaction.commit();
                    if (!getCodeFromServer().isEmpty()) {
                        client.send(new TdApi.AuthSetCode(getCodeFromServer()), handler);
                    }
                }
            }
        };

        TG.setUpdatesHandler(handler);
        TG.setDir(this.getFilesDir().getPath());

        client = TG.getClientInstance();
        client.send(new TdApi.AuthGetState(), handler);


    }

    public CountryObject getCountryObject() {
        return countryObject;
    }

    public void setCountryObject(CountryObject countryObject) {
        this.countryObject = countryObject;
    }

    public ListCountryObject getListCountryObject() {
        return listCountryObject;
    }

    public void setListCountryObject(ListCountryObject listCountryObject) {
        this.listCountryObject = listCountryObject;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
