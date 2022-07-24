package com.example.movietrackerv2.ui.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.MovieEvent;
import com.example.movietrackerv2.data.component.User;
import com.example.movietrackerv2.ui.viewmodel.LoginInterface;
import com.example.movietrackerv2.ui.viewmodel.EventInterface;
import com.example.movietrackerv2.ui.viewmodel.MovieEventViewModel;
import com.example.movietrackerv2.ui.viewmodel.ScheduleInterface;
import com.example.movietrackerv2.ui.viewmodel.UserScheduleViewModel;
import com.example.movietrackerv2.ui.viewmodel.UserViewModel;

// a dialog fragment class displaying event information and a button for adding the event to the user's schedule calendar
public class EventDialogFragment extends DialogFragment {

    int index;
    private ScheduleInterface scheduleInterface;
    private LoginInterface loginInterface;
    private EventInterface eventInterface;

    public EventDialogFragment(int index, ScheduleInterface scheduleInterface, LoginInterface loginInterface, EventInterface eventInterface) {
        this.index = index;
        this.scheduleInterface = scheduleInterface;
        this.loginInterface = loginInterface;
        this.eventInterface = eventInterface;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.event_fragment_dialog, null);

        MovieEvent selectedEvent = eventInterface.getEventByIndex(index);

        TextView movieTitle     = (TextView) layout.findViewById(R.id.movie_title_text);
        TextView theatreName    = (TextView) layout.findViewById(R.id.theatre_name_text);
        TextView startTime      = (TextView) layout.findViewById(R.id.start_time_text);
        TextView endTime        = (TextView) layout.findViewById(R.id.end_time_text);
        TextView date           = (TextView) layout.findViewById(R.id.date_text);
        Button calendarButton   = ( Button ) layout.findViewById(R.id.add_to_calendar_button);


        movieTitle.setText(new StringBuilder().append(movieTitle.getText().toString()).append(" ").append(selectedEvent.getMovieTitle()).toString());
        theatreName.setText(new StringBuilder().append(theatreName.getText().toString()).append(" ").append(selectedEvent.getTheatreName()).toString());
        startTime.setText(new StringBuilder().append(startTime.getText().toString()).append(" ").append(selectedEvent.getStartTime()).toString());
        endTime.setText(new StringBuilder().append(endTime.getText().toString()).append(" ").append(selectedEvent.getEndTime()).toString());
        date.setText(new StringBuilder().append(date.getText().toString()).append(" ").append(selectedEvent.getDate()).toString());


        calendarButton.setOnClickListener(v -> {
            addToCalendar(loginInterface.getLoggedInUser().getValue(), selectedEvent);
        });

        builder.setView(layout)
                .setPositiveButton("done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                });

        return builder.create();
    }

    // calls schedule interface to add the selected event to the currently logged in user's schedule list
    private void addToCalendar(User loggedInUser, MovieEvent selectedEvent) {
        if (loggedInUser == null) {
            showPleaseLoginMessage();
            return;
        }
        boolean success = scheduleInterface.addEvent(selectedEvent, loggedInUser);
        if (success) {
            showSuccessfullyAddedMessage();
        } else {
            showAlreadyAddedMessage();
        }
    }

    private void showAlreadyAddedMessage() {
        Toast toast = new Toast(getContext());
        toast.setText(getResources().getText(R.string.already_added_toast));
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    private void showPleaseLoginMessage() {
        Toast toast = new Toast(getContext());
        toast.setText(getResources().getText(R.string.please_login_toast));
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    private void showSuccessfullyAddedMessage() {
        Toast toast = new Toast(getContext());
        toast.setText(getResources().getText(R.string.successfully_added_toast));
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

}
