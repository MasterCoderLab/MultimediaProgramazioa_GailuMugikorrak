package com.ikasle.jakinarazpenakdialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Informazio-mezua eta OK botoia dituen alerta-elkarrizketa.
 */
public class DialogoAlerta extends DialogFragment {

    /**
     * Alerta-elkarrizketa sortzen du AlertDialog.Builder erabiliz.
     *
     * @param savedInstanceState fragmentuaren aurreko egoera
     * @return sortutako elkarrizketa
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), AlertDialog.THEME_HOLO_LIGHT);

        builder.setMessage(R.string.mezua_alerta)
                .setTitle(R.string.izenburu_alerta)
                .setPositiveButton("OK", (dialog, id) -> {
                    // OK sakatzean, elkarrizketa ixten da.
                    dialog.cancel();
                });

        return builder.create();
    }
}
