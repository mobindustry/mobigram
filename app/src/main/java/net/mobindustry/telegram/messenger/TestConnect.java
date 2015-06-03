package net.mobindustry.telegram.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.ChatActivity;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

public class TestConnect extends AppCompatActivity implements Client.ResultHandler {

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        TG.setUpdatesHandler(this);
        TG.setDir(getExternalFilesDir("").getAbsolutePath());

        client = TG.getClientInstance();
        client.send(new TdApi.AuthGetState(), this);

        Button button = (Button) findViewById(R.id.send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(new TdApi.AuthGetState(), TestConnect.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TG.stopClient();
    }

    private void setName() {
        client.send(new TdApi.AuthSetName("Alexandr", "Tsymbal"), this);
    }

    private void setCode() {
        client.send(new TdApi.AuthSetCode("96394"), this);
    }

    private void chatStart() {

        Intent intent = new Intent(TestConnect.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    private void setPhoneNumber() {
        client.send(new TdApi.AuthSetPhoneNumber("+380687840906"), this);
    }

    @Override
    public void onResult(TdApi.TLObject object) {

        Log.i("123456789", "object: " + object);

        if (object instanceof TdApi.AuthStateWaitSetPhoneNumber) {
            setPhoneNumber();
        }


        if (object instanceof TdApi.AuthStateWaitSetName) {
            setName();
        }

        if (object instanceof TdApi.AuthStateWaitSetCode) {
            setCode();
        }

        if (object instanceof TdApi.AuthStateOk) {
            chatStart();
        }
    }
}
