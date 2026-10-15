package com.ikasle.jakinarazpenakdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Hainbat hizkuntza hautatzeko elkarrizketa.
 */
public class DialogoSeleccionMultiple extends DialogFragment {

    /**
     * Aukera anitzeko zerrenda eraikitzen du kontrol-laukiekin.
     *
     * @param savedInstanceState fragmentuaren aurreko egoera
     * @return sortutako elkarrizketa
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Hautagaiak: hasieran ez dago aukerarik markatuta (null).
        final String[] items = {"Español", "Inglés", "Francés"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(R.string.izenburu_seleccion)
                .setMultiChoiceItems(items, null, (dialog, item, isChecked) -> {
                    // Hautatutako hizkuntza eta bere egoera erregistratzen dira.
                    Log.i("Dialogos", "Opción elegida: " + items[item]
                            + ", marcada: " + isChecked);
                });

        return builder.create();
    }
}
