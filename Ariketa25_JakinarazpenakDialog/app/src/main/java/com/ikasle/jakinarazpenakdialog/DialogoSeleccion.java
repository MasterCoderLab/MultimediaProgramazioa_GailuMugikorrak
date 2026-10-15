package com.ikasle.jakinarazpenakdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Hizkuntza bakarra hautatzeko elkarrizketa.
 */
public class DialogoSeleccion extends DialogFragment {

    /**
     * Aukera bakarreko zerrenda eraikitzen du setItems bidez.
     *
     * @param savedInstanceState fragmentuaren aurreko egoera
     * @return sortutako elkarrizketa
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Irakasleak adierazitako hizkuntzen array tradizionala.
        final String[] items = {"Español", "Inglés", "Francés"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(R.string.izenburu_seleccion)
                .setItems(items, (dialog, item) -> {
                    // Aukeratutako hizkuntza Logcat-en erregistratzen da.
                    Log.i("Dialogos", "Opción elegida: " + items[item]);
                });

        return builder.create();
    }
}
