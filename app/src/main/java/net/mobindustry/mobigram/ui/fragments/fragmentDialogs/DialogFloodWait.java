package net.mobindustry.mobigram.ui.fragments.fragmentDialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import net.mobindustry.mobigram.R;

public class DialogFloodWait extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.error_dialog_title))
                .setMessage(getActivity().getString(R.string.flood))
                .setPositiveButton(getActivity().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }
}
