package net.mobindustry.telegram.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.model.holder.InfoRegistration;
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
    private CountryObject countryObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setRetainInstance(true);
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
        setRetainInstance(true);
        final InfoRegistration infoRegistration = InfoRegistration.getInstance();

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
        countryObject = activity.getCountryObject();

        if (countryObject == null && !infoRegistration.getCountryName().isEmpty()) {
            String name = infoRegistration.getCountryName();
            List<CountryObject> list = ((RegistrationActivity) getActivity()).getListCountryObject().getListCountries();
            System.out.println("CountryListSize " + list.size());
            for (int i = 0; i < list.size(); i++) {
                CountryObject obj = list.get(i);
                if (obj.getCountryName().contains(name)) {
                    System.out.println("Found obj " + obj.toString());
                    countryObject = obj;
                }
            }
        }
        chooseCountry = (TextView) getActivity().findViewById(R.id.chooseCountry);
        code = (EditText) getActivity().findViewById(R.id.code);
        phone = (EditText) getActivity().findViewById(R.id.phone);

        phone.setText(infoRegistration.getPhone());
        code.setText(infoRegistration.getCodeCountry());
        code.setSelection(infoRegistration.getCodeCountry().length());

        //Check country object from ChooseCountryFragment

        if (countryObject != null) {
            Log.e("log", "Name " + countryObject.getCountryName());
            chooseCountry.setText(countryObject.getCountryName());
            infoRegistration.setCodeCountry(countryObject.getCountryCode());
            code.setText(infoRegistration.getCodeCountry());
        }
        chooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, chooseCountryList);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(chooseCountry.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //Create toolbar

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_phone);
        toolbar.inflateMenu(R.menu.ok);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String lettersCode = code.getText().toString();
                String number = phone.getText().toString().replaceAll("\\s", "");
                phoneNumberForServer = lettersCode + number;
                infoRegistration.setCodePlusPhone(infoRegistration.getCodeCountry() + " " + infoRegistration.getPhone());
                Log.e("Log", "PHONE " + phoneNumberForServer);
                activity.setPhoneForServer(phoneNumberForServer);
                return true;
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
                        infoRegistration.setCountryName(list.get(i).getCountryName());
                        chooseCountry.setText(infoRegistration.getCountryName());
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


        final TextWatcher watcher = new TextWatcher() {
            List<String> phoneList = new ArrayList<>();
            String phoneNum = "";
            String lettersCode = "";

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
                if (countryObject != null) {
                    lettersCode = countryObject.getCountryStringCode();
                } else {
                    for (int i = 0; i < countries.getListCountries().size(); i++) {
                        if (countries.getListCountries().get(i).getCountryCode().equals(code.getText().toString())) {
                            activity.setCountryObject(countries.getListCountries().get(i));
                            countryObject = activity.getCountryObject();
                            lettersCode = countryObject.getCountryStringCode();
                        }
                    }

                }
                String formattedNumber = PhoneNumberUtils.formatNumber(phoneNum, lettersCode);

                phone.removeTextChangedListener(this);
                if (formattedNumber == null) {
                    infoRegistration.setPhone(phoneNum);
                    phone.setText(infoRegistration.getPhone());
                    phone.setSelection(infoRegistration.getPhone().length());
                } else {
                    infoRegistration.setPhone(formattedNumber);
                    phone.setText(infoRegistration.getPhone());
                    phone.setSelection(infoRegistration.getPhone().length());
                }
                phone.addTextChangedListener(this);
            }
        };

        phone.addTextChangedListener(watcher);
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_DONE &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    String lettersCode = code.getText().toString();
                    String number = phone.getText().toString().replaceAll("\\s", "");
                    phoneNumberForServer = lettersCode + number;
                    infoRegistration.setCodePlusPhone(infoRegistration.getCodeCountry() + " " + infoRegistration.getPhone());
                    Log.e("Log", "PHONE " + phoneNumberForServer);
                    activity.setPhoneForServer(phoneNumberForServer);

                    return true;
                }
                return false;

            }
        });
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
}


