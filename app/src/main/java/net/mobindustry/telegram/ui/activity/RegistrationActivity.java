package net.mobindustry.telegram.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.model.Enums;
import net.mobindustry.telegram.core.handlers.ErrorHandler;
import net.mobindustry.telegram.core.handlers.GetStateHandler;
import net.mobindustry.telegram.core.handlers.UserHandler;
import net.mobindustry.telegram.model.holder.InfoRegistration;
import net.mobindustry.telegram.model.holder.UserInfoHolder;
import net.mobindustry.telegram.ui.fragments.ReceiverCodeFragment;
import net.mobindustry.telegram.ui.fragments.RegistrationMainFragment;
import net.mobindustry.telegram.ui.fragments.YourNameFragment;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.utils.ListCountryObject;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity implements ApiClient.OnApiResultHandler {

    private CountryObject countryObject;
    private ListCountryObject listCountryObject;
    private LocationManager locationManager;

    @Override
    public void onApiResult(BaseHandler output) {

        if (output.hasErrors()) {
            new ErrorHandler(getSupportFragmentManager(), output.getError());
        }

        if (output.getHandlerId() == UserHandler.HANDLER_ID) {
            UserInfoHolder holder = UserInfoHolder.getInstance();
            holder.setUser((TdApi.User) output.getResponse());
        }

        if (output.getHandlerId() == GetStateHandler.HANDLER_ID) {
            GetStateHandler handler = (GetStateHandler) output;

            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (handler.getResponse() == Enums.StatesEnum.WaitSetPhoneNumber) {

                Fragment registrationUserPhone = new RegistrationMainFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, registrationUserPhone);
            }
            if (handler.getResponse() == Enums.StatesEnum.OK) {
                new ApiClient<>(new TdApi.GetMe(), new UserHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                Intent intent = new Intent(RegistrationActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
            if (handler.getResponse() == Enums.StatesEnum.WaitSetCode) {
                Fragment receiverCodeFragment = new ReceiverCodeFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, receiverCodeFragment);
                fragmentTransaction.addToBackStack(null);
            }
            if (handler.getResponse() == Enums.StatesEnum.WaitSetName) {
                Fragment yourNameFragment = new YourNameFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, yourNameFragment);
            }
            fragmentTransaction.commit();

        }
    }

    public void setCodeFromServer(String codeFromServer) {
        if (!codeFromServer.isEmpty()) {
            new ApiClient<>(new TdApi.AuthSetCode(codeFromServer), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }

    public void setPhoneForServer(String phoneForServer) {
        if (!phoneForServer.isEmpty()) {
            new ApiClient<>(new TdApi.AuthSetPhoneNumber(phoneForServer), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }

    public void setFirstLastName(String firstName, String lastName) {
        if (!firstName.isEmpty()) {
            new ApiClient<>(new TdApi.AuthSetName(firstName, lastName), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }

    public void setAuthReset() {
        new ApiClient<>(new TdApi.AuthReset(), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        new ApiClient<>(new TdApi.AuthGetState(), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        InfoRegistration holder = InfoRegistration.getInstance();

        locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        try {
            String country = getLastKnownCountry();
            if (country != null) {
                holder.setCountryName(country);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
