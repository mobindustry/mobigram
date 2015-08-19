package net.mobindustry.mobigram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import net.mobindustry.mobigram.R;
import net.mobindustry.mobigram.model.holder.InfoRegistration;
import net.mobindustry.mobigram.ui.activity.RegistrationActivity;
import net.mobindustry.mobigram.ui.fragments.fragmentDialogs.DialogCodeEmpty;

import java.io.Serializable;


public class ReceiverCodeFragment extends Fragment implements Serializable {

    private EditText codeFromUser;
    private RegistrationActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receiver_code_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InfoRegistration infoRegistration = InfoRegistration.getInstance();
        infoRegistration.setPhone("");

        activity = (RegistrationActivity) getActivity();

        codeFromUser = (EditText) getActivity().findViewById(R.id.code_from_user);
        codeFromUser.requestFocus();
        TextView textForUser = (TextView) getActivity().findViewById(R.id.text_with_user_phone);
        TextView wrongNumber = (TextView) getActivity().findViewById(R.id.wrong_number);

        wrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                Fragment registrationUserPhone = new RegistrationMainFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, registrationUserPhone);
                activity.getSupportFragmentManager().popBackStack();
                fragmentTransaction.commit();
            }
        });

        textForUser.setText(activity.getString(R.string.we_send_sms_message) + infoRegistration.getCodePlusPhone());

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_code);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.ok);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setCode();
                return true;
            }
        });

        codeFromUser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_DONE && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    setCode();
                    return true;
                }
                return false;
            }
        });
    }

    private void setCode() {
        if(codeFromUser.getText().toString().isEmpty()) {
            FragmentManager fm = getFragmentManager();
            DialogCodeEmpty codeEmpty = new DialogCodeEmpty();
            codeEmpty.show(fm, "CONFIRM_CODE_EMPTY");
        } else {
            activity.setCodeFromServer(codeFromUser.getText().toString().trim());
        }
    }
}
