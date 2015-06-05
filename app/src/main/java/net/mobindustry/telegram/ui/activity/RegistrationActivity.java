package net.mobindustry.telegram.ui.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.fragments.DialogPhoneCodeEmpty;
import net.mobindustry.telegram.ui.fragments.DialogPhoneCodeExpired;
import net.mobindustry.telegram.ui.fragments.DialogPhoneNumberInvalid;
import net.mobindustry.telegram.ui.fragments.RegistrationMainFragment;
import net.mobindustry.telegram.ui.fragments.ReceiverCodeFragment;
import net.mobindustry.telegram.ui.fragments.YourNameFragment;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.utils.ListCountryObject;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

public class RegistrationActivity extends AppCompatActivity {

    private Fragment registrationUserPhone;
    private Fragment receiverCodeFragment;
    private Fragment yourNameFragment;
    private FragmentTransaction fragmentTransaction;
    private CountryObject countryObject;
    private ListCountryObject listCountryObject;
    private Client client;
    private Client.ResultHandler handler;
    private String phoneForServer = "";
    private String codeFromServer = "";

    private String userFirstLastName;
    private String userPhone;

    public String getPhoneForServer() {
        return phoneForServer;
    }

    public String getCodeFromServer() {
        return codeFromServer;
    }

    public void setCodeFromServer(String codeFromServer) {
        this.codeFromServer = codeFromServer;
        if (!codeFromServer.isEmpty()) {
            client.send(new TdApi.AuthSetCode(codeFromServer), handler);
        }
    }

    public void setPhoneForServer(String phoneForServer) {
        this.phoneForServer = phoneForServer;
        if (!phoneForServer.isEmpty()) {
            client.send(new TdApi.AuthSetPhoneNumber(phoneForServer), handler);
        }
    }

    public void setFirstLastName(String firstName, String lastName) {
        if (!firstName.isEmpty()) {
            client.send(new TdApi.AuthSetName(firstName, lastName), handler);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        handler = new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

                Log.e("Log", "OBJECT " + object);

                if (object instanceof TdApi.Error) {
                    TdApi.Error error = (TdApi.Error) object;


                    if ((error.code == 400 && error.text.contains("PHONE_NUMBER_INVALID"))) {
                        DialogPhoneNumberInvalid dialogPhoneNumberInvalid = new DialogPhoneNumberInvalid();
                        FragmentManager fm = getSupportFragmentManager();
                        dialogPhoneNumberInvalid.show(fm, "PHONE_NUMBER_INVALID");
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_CODE_HASH_EMPTY"))) {
                        Log.e("Log", "PHONE_CODE_HASH_EMPTY " + object);
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_CODE_EMPTY"))) {
                        DialogPhoneCodeEmpty phoneCodeEmpty=new DialogPhoneCodeEmpty();
                        FragmentManager fm = getSupportFragmentManager();
                        phoneCodeEmpty.show(fm, "PHONE_CODE_EMPTY");
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_CODE_EXPIRED"))) {
                        DialogPhoneCodeExpired phoneCodeExpired=new DialogPhoneCodeExpired();
                        FragmentManager fm = getSupportFragmentManager();
                        phoneCodeExpired.show(fm, "PHONE_CODE_EXPIRED");
                    }

                    if ((error.code == 400 && error.text.contains("PHONE_NUMBER_OCCUPIED"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The phone number is already in use");
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

                    if ((error.code == 400 && error.text.contains("PHONE_NUMBER_UNOCCUPIED"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The phone number is not yet being used");
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

                    if ((error.code == 400 && error.text.contains("FIRSTNAME_INVALID"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The first name is invalid");
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
                    if ((error.code == 400 && error.text.contains("LASTNAME_INVALID"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The last name is invalid");
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
                    if ((error.code == 400 && error.text.contains("AUTH_KEY_UNREGISTERED"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The key is not registered in the system");
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
                    if ((error.code == 401 && error.text.contains("AUTH_KEY_INVALID"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The key is invalid");
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

                    if ((error.code == 401 && error.text.contains("AUTH_KEY_INVALID"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The key is invalid");
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

                    if ((error.code == 401 && error.text.contains("USER_DEACTIVATED"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The user has been deleted/deactivated");
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

                    if ((error.code == 401 && error.text.contains("SESSION_REVOKED"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The authorization has been invalidated \n because of the user terminating all sessions");
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

                    if ((error.code == 401 && error.text.contains("SESSION_EXPIRED"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("The authorization has expired");
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

                    if ((error.code == 420 && error.text.contains("FLOOD_WAIT"))) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        receiverCodeFragment = new ReceiverCodeFragment();
                        fragmentTransaction.replace(R.id.fragmentContainer, receiverCodeFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        Toast.makeText(getApplicationContext(), "The code has been sent to your " +
                                "phone wait little bit please ", Toast.LENGTH_SHORT).show();
                    }

                    if ((error.code == 401 && error.text.contains("ACTIVE_USER_REQUIRED"))) {
                        Log.e("Log", "ACTIVE_USER_REQUIRED " + object);
                    }

                    if ((error.code == 401 && error.text.contains("ACTIVE_USER_REQUIRED"))) {
                        Log.e("Log", "AUTH_KEY_PERM_EMPTY " + object);
                    }

                }
                if (object instanceof TdApi.AuthStateWaitSetPhoneNumber) {
                    registrationUserPhone = new RegistrationMainFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, registrationUserPhone);
                    fragmentTransaction.commit();
                }


                if (object instanceof TdApi.AuthStateWaitSetCode) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    receiverCodeFragment = new ReceiverCodeFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, receiverCodeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                if (object instanceof TdApi.AuthStateWaitSetName) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    yourNameFragment = new YourNameFragment();
                    fragmentTransaction.replace(R.id.fragmentContainer, yourNameFragment);
                    fragmentTransaction.commit();
                }

                if (object instanceof TdApi.AuthStateOk) {
                    Intent intent = new Intent(RegistrationActivity.this, ChatActivity.class);
                    intent.putExtra("firstLastName", userFirstLastName);
                    intent.putExtra("userPhone", userPhone);
                    startActivity(intent);
                    finish();
                }
                if (object instanceof TdApi.UpdateUserName) {
                    TdApi.UpdateUserName updateUserName = (TdApi.UpdateUserName) object;
                    userFirstLastName = updateUserName.firstName + " " + updateUserName.lastName;
                }
                if (object instanceof TdApi.UpdateUserPhoneNumber) {
                    TdApi.UpdateUserPhoneNumber updateUserPhoneNumber = (TdApi.UpdateUserPhoneNumber) object;
                    userPhone = updateUserPhoneNumber.phoneNumber;
                }

            }
        };

        TG.setUpdatesHandler(handler);
        TG.setDir(this.getFilesDir().getPath());

        client = TG.getClientInstance();
        client.send(new TdApi.AuthGetState(), handler);


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

}
