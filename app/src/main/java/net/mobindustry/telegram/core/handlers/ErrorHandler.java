package net.mobindustry.telegram.core.handlers;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.fragments.FoursquareListFragment;
import net.mobindustry.telegram.ui.fragments.LocationFragment;
import net.mobindustry.telegram.ui.fragments.ReceiverCodeFragment;
import net.mobindustry.telegram.ui.fragments.RegistrationMainFragment;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogAuthKeyInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogAuthKeyUnregistered;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogCodeUnexpected;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogFirstNameInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogFloodWait;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogLastNameInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneCodeEmpty;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneCodeExpired;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneCodeInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneNumberInvalid;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneNumberOccupied;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogPhoneNumberUnoccupied;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogSessionExpired;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogSessionRevoked;
import net.mobindustry.telegram.ui.fragments.fragmentDialogs.DialogUserDeactivated;

import org.drinkless.td.libcore.telegram.TdApi;

public class ErrorHandler {

    private FragmentManager fm;
    private TdApi.Error error;

    public ErrorHandler(FragmentManager fm, TdApi.Error error) {
        this.fm = fm;
        this.error = error;
        handle();
    }

    private void handle() {
        Log.e("Log", "ErrorHandler: " + error.toString());

        if ((error.code == 8 && error.text.contains("UNEXPECTED"))) {
            DialogCodeUnexpected dialogCodeUnexpected = new DialogCodeUnexpected();
            dialogCodeUnexpected.show(fm, "UNEXPECTED");
        }

        if ((error.code == 400 && error.text.contains("PHONE_NUMBER_INVALID"))) {
            DialogPhoneNumberInvalid dialogPhoneNumberInvalid = new DialogPhoneNumberInvalid();
            dialogPhoneNumberInvalid.show(fm, "PHONE_NUMBER_INVALID");
        }

        if ((error.code == 400 && error.text.contains("PHONE_CODE_HASH_EMPTY"))) {
            Log.e("Log", "PHONE_CODE_HASH_EMPTY " + error);
        }

        if ((error.code == 400 && error.text.contains("PHONE_CODE_EMPTY"))) {
            DialogPhoneCodeEmpty phoneCodeEmpty = new DialogPhoneCodeEmpty();
            phoneCodeEmpty.show(fm, "PHONE_CODE_EMPTY");
        }

        if ((error.code == 400 && error.text.contains("PHONE_CODE_EXPIRED"))) {
            DialogPhoneCodeExpired phoneCodeExpired = new DialogPhoneCodeExpired();
            phoneCodeExpired.show(fm, "PHONE_CODE_EXPIRED");
        }

        if ((error.code == 400 && error.text.contains("PHONE_NUMBER_OCCUPIED"))) {
            DialogPhoneNumberOccupied phoneNumberOccupied = new DialogPhoneNumberOccupied();
            phoneNumberOccupied.show(fm, "PHONE_NUMBER_OCCUPIED");
        }

        if ((error.code == 400 && error.text.contains("PHONE_NUMBER_UNOCCUPIED"))) {
            DialogPhoneNumberUnoccupied phoneNumberUnoccupied = new DialogPhoneNumberUnoccupied();
            phoneNumberUnoccupied.show(fm, "PHONE_NUMBER_UNOCCUPIED");
        }

        if ((error.code == 400 && error.text.contains("FIRSTNAME_INVALID"))) {
            DialogFirstNameInvalid firstNameInvalid = new DialogFirstNameInvalid();
            firstNameInvalid.show(fm, "FIRSTNAME_INVALID");
        }

        if ((error.code == 400 && error.text.contains("LASTNAME_INVALID"))) {
            DialogLastNameInvalid lastNameInvalid = new DialogLastNameInvalid();
            lastNameInvalid.show(fm, "LASTNAME_INVALID");
        }

        if ((error.code == 400 && error.text.contains("PHONE_CODE_INVALID"))) {
            DialogPhoneCodeInvalid phoneCodeInvalid = new DialogPhoneCodeInvalid();
            phoneCodeInvalid.show(fm, "PHONE_CODE_INVALID");

        }

        if ((error.code == 400 && error.text.contains("AUTH_KEY_UNREGISTERED"))) {
            DialogAuthKeyUnregistered authKeyUnregistered = new DialogAuthKeyUnregistered();
            authKeyUnregistered.show(fm, "AUTH_KEY_UNREGISTERED");
        }
        if ((error.code == 401 && error.text.contains("AUTH_KEY_INVALID"))) {
            DialogAuthKeyInvalid dialogAuthKeyInvalid = new DialogAuthKeyInvalid();
            dialogAuthKeyInvalid.show(fm, "AUTH_KEY_INVALID");
        }


        if ((error.code == 401 && error.text.contains("USER_DEACTIVATED"))) {
            DialogUserDeactivated userDeactivated = new DialogUserDeactivated();
            userDeactivated.show(fm, "USER_DEACTIVATED");
        }

        if ((error.code == 401 && error.text.contains("SESSION_REVOKED"))) {
            DialogSessionRevoked sessionRevoked = new DialogSessionRevoked();
            sessionRevoked.show(fm, "SESSION_REVOKED");
        }

        if ((error.code == 401 && error.text.contains("SESSION_EXPIRED"))) {
            DialogSessionExpired sessionExpired = new DialogSessionExpired();
            sessionExpired.show(fm, "SESSION_EXPIRED");
        }

        if ((error.code == 420 && error.text.contains("FLOOD_WAIT"))) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            ReceiverCodeFragment receiverCodeFragment = new ReceiverCodeFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, receiverCodeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            DialogFloodWait dialogFloodWait = new DialogFloodWait();
            dialogFloodWait.show(fm, "FLOOD_WAIT");
        }

        if ((error.code == 401 && error.text.contains("ACTIVE_USER_REQUIRED"))) {
            Log.e("Log", "ACTIVE_USER_REQUIRED " + error);
        }

        if ((error.code == 401 && error.text.contains("ACTIVE_USER_REQUIRED"))) {
            Log.e("Log", "AUTH_KEY_PERM_EMPTY " + error);
        }
    }

}
