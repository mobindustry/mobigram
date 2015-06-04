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
import java.util.concurrent.TimeUnit;

public class ReceiverCodeFragment extends Fragment implements Serializable {
    private EditText codeFromUser;
    private RegistrationActivity activity;
    private TextView countDownTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receiver_code_fragment, container, false);
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

        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                //getDurationBreakdown(millisUntilFinished);

                countDownTimer.setText("We will call to you after: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countDownTimer.setText("Calling you!");

            }
        }.start();







    }
    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return(sb.toString());
    }


}
