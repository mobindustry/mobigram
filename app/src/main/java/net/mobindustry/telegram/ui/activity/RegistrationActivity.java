package net.mobindustry.telegram.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.InfoRegistration;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogAuthKeyInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogAuthKeyUnregistered;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogFirstNameInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogFloodWait;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogLastNameInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneCodeEmpty;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneCodeExpired;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneCodeInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneNumberInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneNumberOccupied;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneNumberUnoccupied;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogSessionExpired;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogSessionRevoked;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogUserDeactivated;
import net.mobindustry.telegram.ui.fragments.RegistrationMainFragment;
import net.mobindustry.telegram.ui.fragments.ReceiverCodeFragment;
import net.mobindustry.telegram.ui.fragments.YourNameFragment;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.model.holder.UserMeHolder;
import net.mobindustry.telegram.utils.ListCountryObject;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    private LocationManager locationManager;

    public String getPhoneForServer() {
        return phoneForServer;
    }

    public String getCodeFromServer() {
        return codeFromServer;
    }

    public void setCodeFromServer(String codeFromServer) {
        this.codeFromServer = codeFromServer;
        if (!codeFromServer.isEmpty()) {
            client.send(new TdApi.AuthSetCode(codeFromServer), handler);
        }
    }

    public void setPhoneForServer(String phoneForServer) {
        this.phoneForServer = phoneForServer;
        if (!phoneForServer.isEmpty()) {
            client.send(new TdApi.AuthSetPhoneNumber(phoneForServer), handler);
        }
    }

    public void setFirstLastName(String firstName, String lastName) {
        if (!firstName.isEmpty()) {
            client.send(new TdApi.AuthSetName(firstName, lastName), handler);
        }
    }

    public void setAuthReset() {
        client.send(new TdApi.AuthReset(), handler);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        InfoRegistration holder = InfoRegistration.getInstance();

        try {
            String country = getLastKnownCountry();
            if (country != null) {
                holder.setCountryName(getLastKnownCountry());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler = new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

                Log.e("Log", "OBJECT " + object);

                if (object instanceof TdApi.Error) {
                    TdApi.Error error = (TdApi.Error) object;


                    if ((error.code == 400 && error.text.contains("PHONE_NUMBER_INVALID"))) {
                        DialogPhoneNumberInvalid dialogPhoneNumberInvalid = new DialogPhoneNumberInvalid();
                        FragmentManager fm = getSupportFragmentManager();
                        dialogPhoneNumberInvalid.show(fm, "PHONE_NUMBER_INVALID");
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_CODE_HASH_EMPTY"))) {
                        Log.e("Log", "PHONE_CODE_HASH_EMPTY " + object);
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_CODE_EMPTY"))) {
                        DialogPhoneCodeEmpty phoneCodeEmpty = new DialogPhoneCodeEmpty();
                        FragmentManager fm = getSupportFragmentManager();
                        phoneCodeEmpty.show(fm, "PHONE_CODE_EMPTY");
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_CODE_EXPIRED"))) {
                        DialogPhoneCodeExpired phoneCodeExpired = new DialogPhoneCodeExpired();
                        FragmentManager fm = getSupportFragmentManager();
                        phoneCodeExpired.show(fm, "PHONE_CODE_EXPIRED");
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_NUMBER_OCCUPIED"))) {
                        DialogPhoneNumberOccupied phoneNumberOccupied = new DialogPhoneNumberOccupied();
                        FragmentManager fm = getSupportFragmentManager();
                        phoneNumberOccupied.show(fm, "PHONE_NUMBER_OCCUPIED");
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_NUMBER_UNOCCUPIED"))) {
                        DialogPhoneNumberUnoccupied phoneNumberUnoccupied = new DialogPhoneNumberUnoccupied();
                        FragmentManager fm = getSupportFragmentManager();
                        phoneNumberUnoccupied.show(fm, "PHONE_NUMBER_UNOCCUPIED");
                    }

                    if ((error.code == 400 && error.text.contains("FIRSTNAME_INVALID"))) {
                        DialogFirstNameInvalid firstNameInvalid = new DialogFirstNameInvalid();
                        FragmentManager fm = getSupportFragmentManager();
                        firstNameInvalid.show(fm, "FIRSTNAME_INVALID");
                    }

                    if ((error.code == 400 && error.text.contains("LASTNAME_INVALID"))) {
                        DialogLastNameInvalid lastNameInvalid = new DialogLastNameInvalid();
                        FragmentManager fm = getSupportFragmentManager();
                        lastNameInvalid.show(fm, "LASTNAME_INVALID");
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_CODE_INVALID"))) {
                        DialogPhoneCodeInvalid phoneCodeInvalid = new DialogPhoneCodeInvalid();
                        FragmentManager fm = getSupportFragmentManager();
                        phoneCodeInvalid.show(fm, "PHONE_CODE_INVALID");

                    }

                    if ((error.code == 400 && error.text.contains("AUTH_KEY_UNREGISTERED"))) {
                        DialogAuthKeyUnregistered authKeyUnregistered = new DialogAuthKeyUnregistered();
                        FragmentManager fm = getSupportFragmentManager();
                        authKeyUnregistered.show(fm, "AUTH_KEY_UNREGISTERED");
                    }
                    if ((error.code == 401 && error.text.contains("AUTH_KEY_INVALID"))) {
                        DialogAuthKeyInvalid dialogAuthKeyInvalid = new DialogAuthKeyInvalid();
                        FragmentManager fm = getSupportFragmentManager();
                        dialogAuthKeyInvalid.show(fm, "AUTH_KEY_INVALID");
                    }


                    if ((error.code == 401 && error.text.contains("USER_DEACTIVATED"))) {
                        DialogUserDeactivated userDeactivated = new DialogUserDeactivated();
                        FragmentManager fm = getSupportFragmentManager();
                        userDeactivated.show(fm, "USER_DEACTIVATED");
                    }

                    if ((error.code == 401 && error.text.contains("SESSION_REVOKED"))) {
                        DialogSessionRevoked sessionRevoked = new DialogSessionRevoked();
                        FragmentManager fm = getSupportFragmentManager();
                        sessionRevoked.show(fm, "SESSION_REVOKED");
                    }

                    if ((error.code == 401 && error.text.contains("SESSION_EXPIRED"))) {
                        DialogSessionExpired sessionExpired = new DialogSessionExpired();
                        FragmentManager fm = getSupportFragmentManager();
                        sessionExpired.show(fm, "SESSION_EXPIRED");
                    }

                    if ((error.code == 420 && error.text.contains("FLOOD_WAIT"))) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        receiverCodeFragment = new ReceiverCodeFragment();
                        fragmentTransaction.replace(R.id.fragmentContainer, receiverCodeFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        DialogFloodWait dialogFloodWait = new DialogFloodWait();
                        FragmentManager fm = getSupportFragmentManager();
                        dialogFloodWait.show(fm, "FLOOD_WAIT");
                    }

                    if ((error.code == 401 && error.text.contains("ACTIVE_USER_REQUIRED"))) {
                        Log.e("Log", "ACTIVE_USER_REQUIRED " + object);
                    }

                    if ((error.code == 401 && error.text.contains("ACTIVE_USER_REQUIRED"))) {
                        Log.e("Log", "AUTH_KEY_PERM_EMPTY " + object);
                    }

                }
                if (object instanceof TdApi.AuthStateWaitSetPhoneNumber) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    registrationUserPhone = new RegistrationMainFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, registrationUserPhone);
                    fragmentTransaction.commit();
                }


                if (object instanceof TdApi.AuthStateWaitSetCode) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    receiverCodeFragment = new ReceiverCodeFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, receiverCodeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                if (object instanceof TdApi.AuthStateWaitSetName) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    yourNameFragment = new YourNameFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, yourNameFragment);
                    fragmentTransaction.commit();
                }

                if (object instanceof TdApi.AuthStateOk) {

                    client.send(new TdApi.GetMe(), new Client.ResultHandler() {
                        @Override
                        public void onResult(TdApi.TLObject object) {
                            UserMeHolder holder = UserMeHolder.getInstance();
                            holder.setUser((TdApi.User) object);
                        }
                    });

                    Intent intent = new Intent(RegistrationActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

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

    private String getLastKnownCountry() throws IOException {
        Location location = getLastKnownLocation();
        if (location != null) {
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
        }
        return null;
    }

    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }
}
