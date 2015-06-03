package net.mobindustry.telegram.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.utils.ListCountryObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class RegistrationMainFragment extends Fragment {


    private String phoneNumberForServer = "";
    private TextView chooseCountry;
    private EditText code;
    private EditText phone;
    private ChooseCountryList chooseCountryList;
    private FragmentTransaction fragmentTransaction;
    private ListCountryObject countries;
    private RegistrationActivity activity;

    @Override
    public void onResume() {
        super.onResume();
        checkWiFi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_main_fragment, container, false);
        chooseCountryList = new ChooseCountryList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Take file countries.txt from assets folder and parse it to String extFileFromAssets.
        String textFileFromAssets = null;

        InputStream is = null;
        try {
            is = getResources().getAssets().open("countries.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            textFileFromAssets = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        countries = new ListCountryObject(textFileFromAssets);
        activity = (RegistrationActivity) getActivity();
        activity.setListCountryObject(countries);


        //Set country object from ChooseCountryFragment

        final CountryObject countryObject = activity.getCountryObject();

        chooseCountry = (TextView) getActivity().findViewById(R.id.chooseCountry);
        code = (EditText) getActivity().findViewById(R.id.code);
        phone = (EditText) getActivity().findViewById(R.id.phone);

        //Check country object from ChooseCountryFragment

        if (countryObject != null) {
            Log.e("log", "Name " + countryObject.getCountryName());
            chooseCountry.setText(countryObject.getCountryName());
            code.setText(countryObject.getCountryCode());
        }
        chooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, chooseCountryList);
                fragmentTransaction.commit();
            }
        });

        //Create toolbar

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_phone);
        toolbar.inflateMenu(R.menu.ok);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String lettersCode = code.getText().toString();
                String number = phone.getText().toString().replaceAll("\\s", "");
                phoneNumberForServer = lettersCode + number;
                if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumberForServer)) {
                    Log.e("Log", "NUMBER " + phoneNumberForServer);

                } else {
                    //Log.e("Log", "NUMBER " + phoneNumberForServer);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Phone number is incorrect");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return false;
            }
        });


        // If the user fills country code manually

        final List<String> codeList = new ArrayList<>();

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeList.add(String.valueOf(s));
                List<CountryObject> list = ((RegistrationActivity) getActivity()).getListCountryObject().getListCountries();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCountryCode().equals(codeList.get(codeList.size() - 1))) {
                        chooseCountry.setText(list.get(i).getCountryName());
                        codeList.clear();
                        break;
                    } else {
                        chooseCountry.setText("Wrong country code");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // The user enters the phone number

        final List<String> phoneList = new ArrayList<>();

        final TextWatcher watcher = new TextWatcher() {
            String phoneNum = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneList.add(String.valueOf(s));
                phoneNum = phoneList.get(phoneList.size() - 1);
                String lettersCode = countryObject.getCountryStringCode();
                String formattedNumber = PhoneNumberUtils.formatNumber(phoneNum, lettersCode);

                phone.removeTextChangedListener(this);
                if (formattedNumber == null) {
                    phone.setText(phoneNum);
                    phone.setSelection(phoneNum.length());
                } else {
                    phone.setText(formattedNumber);
                    phone.setSelection(formattedNumber.length());
                }
                phone.addTextChangedListener(this);
            }
        };

        phone.addTextChangedListener(watcher);


    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        Writer writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
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

    public void checkWiFi() {
        if (activity.isOnline() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Internet is not enable");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}


