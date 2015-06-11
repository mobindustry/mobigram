package net.mobindustry.telegram.ui.fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;
import net.mobindustry.telegram.utils.InfoRegistration;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class ReceiverCodeFragment extends Fragment implements Serializable {
    private EditText codeFromUser;
    private RegistrationActivity activity;
    private TextView countDownTimer;
    private TextView textForUser;
    private TextView wrongNumber;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receiver_code_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InfoRegistration infoRegistration=InfoRegistration.getInstance();

        activity = (RegistrationActivity) getActivity();

        codeFromUser = (EditText) getActivity().findViewById(R.id.code_from_user);
        countDownTimer = (TextView) getActivity().findViewById(R.id.countdown_timer);
        textForUser = (TextView) getActivity().findViewById(R.id.text_with_user_phone);
        wrongNumber = (TextView) getActivity().findViewById(R.id.wrong_number);

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

        textForUser.setText("We send on SMS with an activation code to your phone " + infoRegistration.getCodePlusPhone());

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_code);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.inflateMenu(R.menu.ok);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                activity.setCodeFromServer(codeFromUser.getText().toString());
                return true;
            }
        });

        new CountDownTimer(120000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                countDownTimer.setText("We will call you " + String.format("%d min %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                countDownTimer.setText("Calling you");
            }
        }.start();


    }


}
