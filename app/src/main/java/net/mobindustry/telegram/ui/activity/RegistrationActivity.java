package net.mobindustry.telegram.ui.activity;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.fragments.RegistrationMainFragment;
import net.mobindustry.telegram.ui.fragments.ReceiverCodeFragment;
import net.mobindustry.telegram.ui.fragments.YourNameFragment;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.utils.ListCountryObject;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

public class RegistrationActivity extends AppCompatActivity {

    private Fragment registrationUserPhone;
    private Fragment receiverCodeFragment;
    private Fragment yourNameFragment;
    private FragmentTransaction fragmentTransaction;
    private CountryObject countryObject;
    private ListCountryObject listCountryObject;
    private Client client;
    private Client.ResultHandler handler;
    private String phoneForServer = "";
    private String codeFromServer = "";


    public void setCodeFromServer(String codeFromServer) {
        this.codeFromServer = codeFromServer;
        //client.send(new TdApi.AuthSetCode(getCodeFromServer()), handler);

    }

    public void setPhoneForServer(String phoneForServer) {
        this.phoneForServer = phoneForServer;
        if (!phoneForServer.isEmpty()) {
            client.send(new TdApi.AuthSetPhoneNumber(phoneForServer), handler);
        }
    }

    public void setFirstLastName(String firstName, String lastName) {
        if (firstName.isEmpty()) {
            client.send(new TdApi.AuthSetName(firstName, lastName), handler);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        handler = new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

                Log.e("Log", "AFTER PHONE " + object);

                if (object instanceof TdApi.Error) {
                    TdApi.Error error = (TdApi.Error) object;

                    if ((error.code == 400 && error.text.equals("PHONE_NUMBER_INVALID:"))) {
                        //todo
                    }
                    if ((error.code == 400 && error.text.equals("PHONE_CODE_HASH_EMPTY:"))) {

                    }
                    if ((error.code == 400 && error.text.equals("PHONE_CODE_EMPTY:"))) {

                    }
                    if ((error.code == 400 && error.text.equals("PHONE_CODE_EXPIRED:"))) {

                    }
                    if ((error.code == 400 && error.text.equals("PHONE_NUMBER_OCCUPIED:"))) {

                    }
                    if ((error.code == 400 && error.text.equals("PHONE_NUMBER_UNOCCUPIED:"))) {

                    }


                }
                if (object instanceof TdApi.AuthStateWaitSetPhoneNumber) {
                    registrationUserPhone = new RegistrationMainFragment();
                    fragmentTransaction.add(R.id.fragmentContainer, registrationUserPhone);
                    fragmentTransaction.commit();
                }
                if (object instanceof TdApi.AuthStateWaitSetCode) {
                    Log.e("Log", "AFTER PHONE IN" + object);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    receiverCodeFragment = new ReceiverCodeFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, receiverCodeFragment);
                    fragmentTransaction.commit();
                }
                if (object instanceof TdApi.AuthStateWaitSetName) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    yourNameFragment = new YourNameFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, yourNameFragment);
                    fragmentTransaction.commit();
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
