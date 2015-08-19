package net.mobindustry.telegram.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ErrorHandler;
import net.mobindustry.telegram.core.handlers.GetStateHandler;
import net.mobindustry.telegram.core.handlers.UserHandler;
import net.mobindustry.telegram.model.Enums;
import net.mobindustry.telegram.model.LocationFromIP;
import net.mobindustry.telegram.model.holder.InfoRegistration;
import net.mobindustry.telegram.model.holder.UserInfoHolder;
import net.mobindustry.telegram.ui.fragments.ChooseCountryList;
import net.mobindustry.telegram.ui.fragments.ReceiverCodeFragment;
import net.mobindustry.telegram.ui.fragments.RegistrationMainFragment;
import net.mobindustry.telegram.ui.fragments.YourNameFragment;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.model.ListCountryObject;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class RegistrationActivity extends AppCompatActivity implements ApiClient.OnApiResultHandler {

    private ListCountryObject listCountryObject;
    private InfoRegistration holder;
    boolean activityClosed = false;
    private LocationAsync locationAsync;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        new ApiClient<>(new TdApi.AuthGetState(), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onApiResult(BaseHandler output) {
        if (output.hasErrors()) {
            new ErrorHandler(getSupportFragmentManager(), output.getError());
        }
        if (output.getHandlerId() == UserHandler.HANDLER_ID) {
            UserInfoHolder holder = UserInfoHolder.getInstance();
            UserInfoHolder.setUser((TdApi.User) output.getResponse());
        }
        if (output.getHandlerId() == GetStateHandler.HANDLER_ID) {
            GetStateHandler handler = (GetStateHandler) output;
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (handler.getResponse() == Enums.StatesEnum.WaitSetPhoneNumber) {
                locationAsync = new LocationAsync();
                if (!activityClosed) {
                    locationAsync.execute();
                }
            }
            if (handler.getResponse() == Enums.StatesEnum.OK) {
                new ApiClient<>(new TdApi.GetMe(), new UserHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                Intent intent = new Intent(RegistrationActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
            if (handler.getResponse() == Enums.StatesEnum.WaitSetCode) {
                ReceiverCodeFragment receiverCodeFragment = new ReceiverCodeFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, receiverCodeFragment);
                fragmentTransaction.addToBackStack(null);
            }
            if (handler.getResponse() == Enums.StatesEnum.WaitSetName) {
                YourNameFragment yourNameFragment = new YourNameFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, yourNameFragment);
                fragmentTransaction.addToBackStack(null);
            }
            if (!activityClosed) {
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationAsync != null) {
            locationAsync.cancel(true);
        }
        activityClosed = true;
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

    public static String convertStreamToString(InputStream is)
            throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }

    private void setCountryName() {
        for (int i = 0; i < listCountryObject.getListCountries().size(); i++) {
            if (listCountryObject.getListCountries().get(i).getCountryStringCode().equals(holder.getCodeCountryLetters())) {
                holder.setCountryObject(listCountryObject.getListCountries().get(i));
            }
        }
    }

    public class LocationAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                AndroidHttpClient httpClient = new AndroidHttpClient(Const.IP_API);
                httpClient.setMaxRetries(5);
                ParameterMap param = httpClient.newParams();
                HttpResponse httpResponse = httpClient.get("", param);
                if (httpResponse.getBodyAsString() != null) {
                    return httpResponse.getBodyAsString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new GsonBuilder().create();
            LocationFromIP location = gson.fromJson(s, LocationFromIP.class);
            String code = location.getCountryCode();
            holder = InfoRegistration.getInstance();
            if (code != null) {
                holder.setCodeCountryLetters(code);
            }
            String textFileFromAssets = null;
            try {
                InputStream is = getResources().getAssets().open("countries.txt");
                textFileFromAssets = convertStreamToString(is);
            } catch (IOException e) {
                // do nothing
            }
            holder.setTextFileFromAssets(textFileFromAssets);
            listCountryObject = new ListCountryObject(textFileFromAssets);
            holder.setListCountryObject(listCountryObject);
            setCountryName();
            Fragment registrationUserPhone = new RegistrationMainFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, registrationUserPhone);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activityClosed = false;
        new ApiClient<>(new TdApi.AuthGetState(), new GetStateHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof ChooseCountryList) {
            RegistrationMainFragment registrationMainFragment = new RegistrationMainFragment();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, registrationMainFragment);
            fragmentTransaction.commit();
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof YourNameFragment) {
            setAuthReset();
        } else {
            super.onBackPressed();
        }


    }
}
