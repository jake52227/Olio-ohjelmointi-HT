package com.example.movietrackerv2.ui.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.movietrackerv2.R;


import java.util.ArrayList;
import java.util.List;

// dialog which shows information about a event in the user's calendar when clicked
public class ScheduledEventDialogFragment extends DialogFragment {

    private List<String> items;

    public ScheduledEventDialogFragment(List<String> items) {
        this.items = items;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.scheduled_event_fragment_dialog, null);

        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText(getContext().getResources().getText(R.string.scheduled_event_dialog_title_text));

        ListView listView = (ListView) layout.findViewById(R.id.scheduledEventsListView);
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.user_fragment_schedule_listview, items);
        listView.setAdapter(adapter);

        builder.setView(layout)
                .setPositiveButton(getResources().getString(R.string.genres_alert_dialog_ok_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                });

        return builder.create();
    }
}
