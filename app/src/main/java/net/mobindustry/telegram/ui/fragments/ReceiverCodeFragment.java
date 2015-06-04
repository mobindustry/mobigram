package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;

import java.io.Serializable;

public class ReceiverCodeFragment extends Fragment implements Serializable {
    private EditText codeFromUser;
    private RegistrationActivity activity;
    private TextView countDownTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reseiver_code_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity=(RegistrationActivity)getActivity();

        codeFromUser=(EditText)getView().findViewById(R.id.code_from_user);
        countDownTimer=(TextView)getView().findViewById(R.id.countdown_timer);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_code);
        toolbar.inflateMenu(R.menu.ok);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                activity.setCodeFromServer(codeFromUser.getText().toString());
                return true;
            }
        });

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countDownTimer.setText("done!");
            }
        }.start();







    }


}
