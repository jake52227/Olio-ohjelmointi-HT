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
import android.widget.Button;
import android.widget.EditText;

import com.example.movietrackerv2.ui.adapter.MovieSearchRecyclerViewAdapter;
import com.example.movietrackerv2.R;
import com.example.movietrackerv2.ui.adapter.RecyclerViewOnClickInterface;
import com.example.movietrackerv2.ui.view.dialog.MultiChoiceDialogFragment;
import com.example.movietrackerv2.ui.view.dialog.MovieInfoDialogFragment;
import com.example.movietrackerv2.ui.viewmodel.MovieSearchInterface;
import com.example.movietrackerv2.ui.viewmodel.MovieViewModel;


// this fragment is responsible for the "search movies" tab in the app
public class MovieSearchFragment extends Fragment implements RecyclerViewOnClickInterface {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private MovieSearchInterface movieSearchInterface;

    private RecyclerView recyclerView;
    private MovieSearchRecyclerViewAdapter adapter;

    private EditText movieTitle;
    private EditText actorFirstName;
    private EditText actorLastName;
    private EditText directorFirstName;
    private EditText directorLastName;

    public MovieSearchFragment() {}

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
        View view = inflater.inflate(R.layout.fragment_movie_search_list, container, false);

        movieTitle          = (EditText) view.findViewById(R.id.movieTitleEditText);
        actorFirstName      = (EditText) view.findViewById(R.id.actorFirstNameEditText);
        actorLastName       = (EditText) view.findViewById(R.id.actorLastNameEditText);
        directorFirstName   = (EditText) view.findViewById(R.id.directorFirstNameEditText);
        directorLastName    = (EditText) view.findViewById(R.id.directorLastNameEditText);

        Button genreDialogButton = (Button) view.findViewById(R.id.genreDialogButton);
        Button refreshButton = (Button) view.findViewById(R.id.refreshViewButton);

        movieSearchInterface =
                new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        // Set the adapter
        if (recyclerView != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            setRecyclerViewAdapter();
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        refreshButton.setOnClickListener(v -> {
            onRefreshButtonClick();
        });

        genreDialogButton.setOnClickListener(v -> {
            onGenreDialogButtonClick();
        });

        return view;
    }

    // gets a filtered list of movie objects from the movie search interface and sets it for the recycler view adapter.
    private void setRecyclerViewAdapter() {
        adapter = new MovieSearchRecyclerViewAdapter(movieSearchInterface.geFilteredListOfMovies(), this::onItemClick);
        recyclerView.setAdapter(adapter);
    }

    // gets the user given filters from and calls movie search interface to set them. Calls setRecyclerView to update the list
    private void onRefreshButtonClick() {
        String title            = movieTitle.getText().toString();
        String actorFirst       = actorFirstName.getText().toString();
        String actorLast        = actorLastName.getText().toString();
        String directorFirst    = directorFirstName.getText().toString();
        String directorLast     = directorLastName.getText().toString();

        movieSearchInterface.setSearchFilters(title, actorFirst, actorLast, directorFirst, directorLast);
        setRecyclerViewAdapter();
    }

    // creates and shows a GenreAlertDialogFragment where the user can select genres which they would like to see
    private void onGenreDialogButtonClick() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        MultiChoiceDialogFragment frag = new MultiChoiceDialogFragment( getResources().getString(R.string.genres_alert_dialog_title_text) ,new ViewModelProvider(requireActivity()).get(MovieViewModel.class));
        frag.show(fragmentManager, null);
    }

    // when the user clicks an item in the recyclerview, this method activates, creates a MovieInfoDialogFragment and shows it.
    @Override
    public void onItemClick(int index) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        MovieInfoDialogFragment frag = new MovieInfoDialogFragment(index, movieSearchInterface);
        frag.show(fragmentManager, null);
    }
}