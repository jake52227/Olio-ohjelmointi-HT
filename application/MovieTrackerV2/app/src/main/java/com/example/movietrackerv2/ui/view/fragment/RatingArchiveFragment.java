package com.example.movietrackerv2.ui.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.ui.adapter.RatingArchiveRecyclerViewAdapter;
import com.example.movietrackerv2.ui.adapter.RecyclerViewOnClickInterface;
import com.example.movietrackerv2.ui.view.dialog.AddRatingDialogFragment;
import com.example.movietrackerv2.ui.view.dialog.CheckRatingDialogFragment;
import com.example.movietrackerv2.ui.viewmodel.LoginInterface;
import com.example.movietrackerv2.ui.viewmodel.MovieSearchInterface;
import com.example.movietrackerv2.ui.viewmodel.MovieViewModel;
import com.example.movietrackerv2.ui.viewmodel.RatingInterface;
import com.example.movietrackerv2.ui.viewmodel.RatingListViewModel;
import com.example.movietrackerv2.ui.viewmodel.UserViewModel;

// a fragment which is responsible for the "My Ratings" tab view
public class RatingArchiveFragment extends Fragment implements RecyclerViewOnClickInterface {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private Button addRatingButton;
    private Button refreshButton;
    private RatingArchiveRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    private RatingInterface ratingInterface;
    private LoginInterface loginInterface;
    private MovieSearchInterface movieSearchInterface;

    private NavController navController;

    public RatingArchiveFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loginInterface = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Observe the logged in user. If there isn't one, navigate to log in fragment
        loginInterface.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                navController.navigate(R.id.nav_login_fragment);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rating_archive_list, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        ratingInterface =
                new ViewModelProvider(requireActivity()).get(RatingListViewModel.class);
        loginInterface =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        movieSearchInterface =
                new ViewModelProvider(requireActivity()).get(MovieViewModel.class);


        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        addRatingButton = (Button) view.findViewById(R.id.addRatingButton);
        refreshButton = (Button) view.findViewById(R.id.ratingArchiveRefreshButton);

        addRatingButton.setOnClickListener(v -> {
            onClickAddRating();
        });

        refreshButton.setOnClickListener(v -> {
            setRecyclerViewAdapter();
        });

        setRecyclerViewAdapter();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // shows a dialog where the user can enter info for a new rating.
    private void onClickAddRating() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AddRatingDialogFragment frag = new AddRatingDialogFragment(movieSearchInterface, ratingInterface, loginInterface);
        frag.show(fragmentManager, null);
    }

    // gets a list of ratings from the ratings view model and adds the list to the recyclerview adapter
    private void setRecyclerViewAdapter() {
        adapter = new RatingArchiveRecyclerViewAdapter(ratingInterface.getRatingsList(loginInterface.getLoggedInUser().getValue()), this::onItemClick);
        recyclerView.setAdapter(adapter);
    }

    // when the user clicks a rating item on the list, create and show a dialog fragment which displays the information of that item
    @Override
    public void onItemClick(int index) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        CheckRatingDialogFragment frag = new CheckRatingDialogFragment(index, ratingInterface, loginInterface);
        frag.show(fragmentManager, null);
    }
}