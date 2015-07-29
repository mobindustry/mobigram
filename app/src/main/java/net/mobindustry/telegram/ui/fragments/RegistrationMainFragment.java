package net.mobindustry.telegram.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
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
import net.mobindustry.telegram.model.holder.InfoRegistration;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneCodeEmpty;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneCodeInvalid;
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
    private InfoRegistration holder;

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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        holder = InfoRegistration.getInstance();
        chooseCountryList = new ChooseCountryList();
        chooseCountry = (TextView) getActivity().findViewById(R.id.chooseCountry);
        code = (EditText) getActivity().findViewById(R.id.code);
        phone = (EditText) getActivity().findViewById(R.id.phone);

        phone.setText(holder.getPhone());
        code.setText(holder.getCodeCountry());
        code.setSelection(holder.getCodeCountry().length());
        phone.requestFocus();

        //Check country object from ChooseCountryFragment

        if (this.holder.getCountryObject() != null) {
            Log.e("log", "Name " + holder.getCountryObject().getCountryName());
            chooseCountry.setText(holder.getCountryObject().getCountryName());
            holder.setCodeCountry(holder.getCountryObject().getCountryCode());
            code.setText(holder.getCodeCountry());
        }
        chooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getListCountryObject().getListTmp().size() < holder.getListCountryObject().getListConst().size()) {
                    holder.getListCountryObject().updateListTmp(holder.getTextFileFromAssets());
                }
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
                confirmPhone();
                return true;
            }
        });

        // If the user fills country code manually

        final List<String> codeList = new ArrayList<>();

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeList.add(String.valueOf(s));
                for (int i = 0; i < holder.getListCountryObject().getListCountries().size(); i++) {
                    if (holder.getListCountryObject().getListCountries().get(i).getCountryCode().equals(codeList.get(codeList.size() - 1))) {
                        holder.setCountryName(holder.getListCountryObject().getListCountries().get(i).getCountryName());
                        chooseCountry.setText(holder.getCountryName());
                        codeList.clear();
                        break;
                    } else {
                        chooseCountry.setText("Wrong country code");
                    }
                }
                String st = "+";
                if (s.length() == 0) {
                    code.setText(st);
                    code.setSelection(1);
                }
                if (s.toString().equals(st)) {
                    chooseCountry.setText("");
                    chooseCountry.setHint(R.string.choose_country);
                }

                if (s.length() == 5) {
                    phone.requestFocus();
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
                if (holder.getCountryObject() != null) {
                    lettersCode = holder.getCountryObject().getCountryStringCode();
                } else {
                    for (int i = 0; i < holder.getListCountryObject().getListCountries().size(); i++) {
                        if (holder.getListCountryObject().getListCountries().get(i).getCountryCode().equals(code.getText().toString())) {
                            holder.setCountryObject(null);
                            holder.setCountryObject(holder.getListCountryObject().getListCountries().get(i));
                            lettersCode = holder.getCodeCountryLetters();
                        }
                    }
                }
                String formattedNumber = PhoneNumberUtils.formatNumber(phoneNum, lettersCode);
                if (formattedNumber != null && formattedNumber.length() > phoneNum.length()) {
                    int result = formattedNumber.length() - phoneNum.length() + 1;
                    holder.setCursorPosition(holder.getCursorPosition() + result);
                } else {
                    if (phoneNum.length() == phone.getSelectionEnd()) {
                        holder.setCursorPosition(phoneNum.length());
                    } else {
                        holder.setCursorPosition(phone.getSelectionEnd());
                    }
                }

                phone.removeTextChangedListener(this);
                if (formattedNumber == null) {
                    holder.setPhone(phoneNum);
                    phone.setText(holder.getPhone());
                    phone.setSelection(holder.getCursorPosition());
                } else {
                    holder.setPhone(formattedNumber);
                    phone.setText(holder.getPhone());
                    phone.setSelection(holder.getCursorPosition());
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
                    confirmPhone();
                    return true;
                }
                return false;
            }
        });
    }

    private void confirmPhone() {
        FragmentManager fm = getFragmentManager();
        String lettersCode = code.getText().toString();
        if (isCodeCorrect(lettersCode)) {
            if (lettersCode.equals("+")) {
                DialogPhoneCodeEmpty phoneCodeEmpty = new DialogPhoneCodeEmpty();
                phoneCodeEmpty.show(fm, "PHONE_CODE_EMPTY");
            } else {
                String number = phone.getText().toString().replaceAll("\\s", "");
                phoneNumberForServer = lettersCode + number;
                holder.setCodePlusPhone(phoneNumberForServer);
                Log.e("Log", "PHONE " + phoneNumberForServer);
                holder.setPhoneForServer(phoneNumberForServer);
                ((RegistrationActivity) getActivity()).setPhoneForServer(holder.getPhoneForServer());
            }
        } else {
            DialogPhoneCodeInvalid phoneCodeInvalid = new DialogPhoneCodeInvalid();
            phoneCodeInvalid.show(fm, "PHONE_CODE_INVALID");
        }
    }

    private boolean isCodeCorrect(String lettersCode) {
        for (int i = 0; i < holder.getListCountryObject().getListCountries().size(); i++) {
            if (holder.getListCountryObject().getListCountries().get(i).getCountryCode().equals(lettersCode)) {
                return true;
            }
        }
        return false;
    }
}


