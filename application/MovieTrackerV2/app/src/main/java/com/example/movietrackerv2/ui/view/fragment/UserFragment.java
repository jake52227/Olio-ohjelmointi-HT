package com.example.movietrackerv2.ui.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.User;
import com.example.movietrackerv2.ui.view.dialog.ConfirmationDialogFragment;
import com.example.movietrackerv2.ui.view.dialog.ScheduledEventDialogFragment;
import com.example.movietrackerv2.ui.viewmodel.LoginInterface;
import com.example.movietrackerv2.ui.viewmodel.ScheduleInterface;
import com.example.movietrackerv2.ui.viewmodel.UserScheduleViewModel;
import com.example.movietrackerv2.ui.viewmodel.UserViewModel;

import java.util.List;

// a fragment responsible for the "User" tab of the app
public class UserFragment extends Fragment {

    private TextView helloText;

    private LoginInterface loginInterface;
    private ScheduleInterface scheduleInterface;

    private NavController navController;
    private ListView listView;
    private TextView textView;
    private CalendarView calendarView;

    public UserFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        listView     = (ListView) view.findViewById(R.id.userScheduleListView);
        textView     = (TextView) view.findViewById(R.id.scheduleInfoText);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        helloText    = (TextView) view.findViewById(R.id.hello_text);

        Button logOutButton = (Button) view.findViewById(R.id.logOutButton);
        Button deleteUserButton = (Button) view.findViewById(R.id.deleteUserButton);

        loginInterface =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        scheduleInterface =
                new ViewModelProvider(requireActivity()).get(UserScheduleViewModel.class);

        logOutButton.setOnClickListener(v -> {
            onLogOutClick();
        });

        deleteUserButton.setOnClickListener(v -> {
            showAreYouSureMessage();
        });

        // observe the user view model's logged in user status. If its null, navigate to login fragment
        loginInterface.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                helloText.setText(new StringBuilder().append( getResources().getString(R.string.user_fragment_welcome_text) ).append(" ").append(loginInterface.getLoggedInUser().getValue().getUsername()).toString());
                initList(loginInterface.getLoggedInUser().getValue());
            } else {
                navController.navigate(R.id.nav_login_fragment);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                onDateClick(year, month, dayOfMonth);
            }
        });
    }

    private void onDateClick(int year, int month, int dayOfMonth) {
        setListItems(year, month, loginInterface.getLoggedInUser().getValue());
        checkAndShowEventDialog(year, month, dayOfMonth);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    // calls login interface to log out the currently logged in user
    private void onLogOutClick() {
        loginInterface.logOutUser();
    }

    // displays a confirmation dialog asking if the user is sure about deleting the current profile
    private void showAreYouSureMessage() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        String title = getResources().getString(R.string.are_you_sure);
        String message = getResources().getString(R.string.do_you_want_to);
        ConfirmationDialogFragment frag = new ConfirmationDialogFragment(title, message, new ViewModelProvider(requireActivity()).get(UserViewModel.class));
        frag.show(fragmentManager, null);
    }

    // gets a list of the user's scheduled events based on month and year and sets these to the list adapter
    private void setListItems(int year, int month, User loggedInUser) {
        List<String> content = scheduleInterface.getListOfEventDatesByMonth(year, month, loggedInUser);
        if (content == null) {
            return;
        }
        if (content.isEmpty()) {
            textView.setText(getResources().getText(R.string.user_fragment_calendar_no_events_text));
        } else {
            textView.setText(getResources().getText(R.string.user_fragment_calendar_next_event_text));
        }
        setAdapterContent(content);
    }

    // sets an adapter with the given list of strings for the list view.
    private void setAdapterContent(List<String> content) {
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.user_fragment_schedule_listview, content);
        listView.setAdapter(adapter);
    }

    // initializes the schedule list view based on the current month and year
    private void initList(User loggedInUser) {
        List<String> content = scheduleInterface.getInitialContent(loggedInUser);
        if (content == null) {
            return;
        }
        setAdapterContent(content);
    }

    // gets the user's event based on the exact date. If the event list is not empty, shows information about the events in a dialog fragment
    private void checkAndShowEventDialog(int year, int month, int dayOfMonth) {
        List<String> events = scheduleInterface.getEventsAsStringByDate(year, month + 1, dayOfMonth);
        if (events.isEmpty()) {
            return;
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ScheduledEventDialogFragment frag = new ScheduledEventDialogFragment(events);
        frag.show(fragmentManager, null);
    }
}