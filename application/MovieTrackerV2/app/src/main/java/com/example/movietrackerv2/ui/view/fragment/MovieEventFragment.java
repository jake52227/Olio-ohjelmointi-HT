package com.example.movietrackerv2.ui.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.movietrackerv2.ui.adapter.MovieEventRecyclerViewAdapter;
import com.example.movietrackerv2.R;
import com.example.movietrackerv2.ui.adapter.RecyclerViewOnClickInterface;
import com.example.movietrackerv2.ui.view.dialog.EventDialogFragment;
import com.example.movietrackerv2.ui.viewmodel.EventInterface;
import com.example.movietrackerv2.ui.viewmodel.LoginInterface;
import com.example.movietrackerv2.ui.viewmodel.MovieEventViewModel;
import com.example.movietrackerv2.ui.viewmodel.ScheduleInterface;
import com.example.movietrackerv2.ui.viewmodel.TheatreInterface;
import com.example.movietrackerv2.ui.viewmodel.TheatreViewModel;
import com.example.movietrackerv2.ui.viewmodel.UserScheduleViewModel;
import com.example.movietrackerv2.ui.viewmodel.UserViewModel;

// a fragment which is responsible for the "Movie Events" tab view
public class MovieEventFragment extends Fragment implements RecyclerViewOnClickInterface {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private Spinner theatreSpinner;
    private ArrayAdapter<String> theatreSpinnerAdapter;
    private EditText dateInput;
    private EditText startTimeInput;
    private EditText endTimeInput;
    private Button refreshButton;
    private RecyclerView recyclerView;
    private MovieEventRecyclerViewAdapter recyclerViewAdapter;

    private EventInterface movieEventInterface;
    private TheatreInterface theatreInterface;


    public MovieEventFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_event_list, container, false);

        theatreSpinner  = (Spinner) view.findViewById(R.id.theatreSpinner);
        dateInput       = (EditText) view.findViewById(R.id.dateInput);
        startTimeInput  = (EditText) view.findViewById(R.id.startTimeInput);
        endTimeInput    = (EditText) view.findViewById(R.id.endTimeInput);
        refreshButton   = (Button) view.findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(v -> {
            onRefreshButtonClick();
        });


        movieEventInterface =
                new ViewModelProvider(requireActivity()).get(MovieEventViewModel.class);

        theatreInterface =
                new ViewModelProvider(requireActivity()).get(TheatreViewModel.class);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        Context context = view.getContext();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        setSpinnerItems();

        return view;
    }

    // gets the event list from the movie event interface and sets it as the adapter for the recyclerview
    private void setRecyclerViewAdapter() {
        recyclerViewAdapter = new MovieEventRecyclerViewAdapter( movieEventInterface.getFilteredEventList(), this::onItemClick);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    // check if the user has given any filters. Set them and tell the interface to apply them. Then refresh the view by updating the recyclerview adapter
    private void onRefreshButtonClick() {
        String theatreName  = theatreSpinner.getSelectedItem().toString();
        String date         = dateInput.getText().toString();
        String startTime    = startTimeInput.getText().toString();
        String endTime      = endTimeInput.getText().toString();
        movieEventInterface.applyFilters(theatreName, date, startTime, endTime);
        setRecyclerViewAdapter();
    }

    // get list of theatres from theatre interface and add them to the spinner
    private void setSpinnerItems() {
        theatreSpinnerAdapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        theatreSpinnerAdapter.clear();
        theatreSpinnerAdapter.add("NO SELECTION");
        theatreSpinnerAdapter.addAll(theatreInterface.getTheatreNamesAsList());
        theatreSpinner.setAdapter(theatreSpinnerAdapter);
    }

    // this method activates when the user clicks an item in the recyclerview by showing an EventDialogFragment of the corresponding item
    @Override
    public void onItemClick(int index) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ScheduleInterface scheduleInterface = new ViewModelProvider(requireActivity()).get(UserScheduleViewModel.class);
        LoginInterface loginInterface = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        EventDialogFragment frag = new EventDialogFragment(index, scheduleInterface, loginInterface, movieEventInterface);
        frag.show(fragmentManager, null);
    }
}