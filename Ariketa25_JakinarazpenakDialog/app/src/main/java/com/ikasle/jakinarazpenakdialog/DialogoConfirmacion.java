package com.ikasle.jakinarazpenakdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Ekintza bat onartzeko edo bertan behera uzteko elkarrizketa.
 */
public class DialogoConfirmacion extends DialogFragment {

    /**
     * Bi botoiko berrespen-elkarrizketa eraikitzen du.
     *
     * @param savedInstanceState fragmentuaren aurreko egoera
     * @return sortutako elkarrizketa
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), AlertDialog.THEME_HOLO_LIGHT);

        builder.setMessage(R.string.mezua_confirmacion)
                .setTitle(R.string.izenburu_confirmacion)
                .setPositiveButton("Aceptar", (dialog, id) -> {
                    // Baiezko erantzuna Logcat-en erregistratzen da.
                    Log.i("Dialogos", "Confirmacion Aceptada.");
                    dialog.cancel();
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    // Ezezko erantzuna Logcat-en erregistratzen da.
                    Log.i("Dialogos", "Confirmacion Cancelada.");
                    dialog.cancel();
                });

        return builder.create();
    }
}
