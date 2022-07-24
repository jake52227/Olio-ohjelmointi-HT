package com.example.movietrackerv2.ui.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import com.example.movietrackerv2.ui.viewmodel.SelectionInterface;

// this class is adapted from an example gotten from here: https://developer.android.com/guide/topics/ui/dialogs

// a dialog fragment from where the user can select items in a multi choice list
public class MultiChoiceDialogFragment extends DialogFragment {

    private String title; // title for the view
    private SelectionInterface selectionInterface; // an interface to which events are signaled

    public MultiChoiceDialogFragment(String title, SelectionInterface selectionInterface) {
        this.title = title;
        this.selectionInterface = selectionInterface;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int size = selectionInterface.getListOfOptions().size();
        CharSequence[] genresList = selectionInterface.getListOfOptions().toArray(new CharSequence[size]);
        boolean[] checkedList = selectionInterface.getCheckedItems().getValue();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMultiChoiceItems(genresList, checkedList,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    selectionInterface.addSelectedItem(which);
                                } else if (selectionInterface.getSelectionIndices().contains(which)) {
                                    selectionInterface.removeSelectedItem(which);
                                }
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                });

        return builder.create();
    }
}
