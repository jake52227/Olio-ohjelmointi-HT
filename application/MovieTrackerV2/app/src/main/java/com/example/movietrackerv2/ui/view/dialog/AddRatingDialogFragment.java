package com.example.movietrackerv2.ui.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.Movie;
import com.example.movietrackerv2.data.component.Rating;
import com.example.movietrackerv2.ui.viewmodel.LoginInterface;
import com.example.movietrackerv2.ui.viewmodel.MovieSearchInterface;
import com.example.movietrackerv2.ui.viewmodel.RatingInterface;


// source for all things dialog: https://developer.android.com/guide/topics/ui/dialogs

// A dialog which shows the user a window for entering info for a new rating
public class AddRatingDialogFragment extends DialogFragment {

    private MovieSearchInterface movieInterface;
    private RatingInterface ratingInterface;
    private LoginInterface loginInterface;

    public AddRatingDialogFragment(MovieSearchInterface movieSearchInterface, RatingInterface ratingInterface, LoginInterface loginInterface) {
        this.movieInterface = movieSearchInterface;
        this.ratingInterface = ratingInterface;
        this.loginInterface = loginInterface;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.add_rating_dialog, null);

        RatingBar ratingBar  = (RatingBar) layout.findViewById(R.id.movieRatingBar);
        Spinner movieSpinner = (Spinner) layout.findViewById(R.id.movieChoiceSpinner);
        EditText editText    = (EditText) layout.findViewById(R.id.commentEditText);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        // get movie titles from the movie interface and set up the adapter
        try {
            adapter.addAll(movieInterface.getListOfTitles());
        } catch (NullPointerException e) {
            Log.e(null, "Null pointer exception at AddRatingDialogFragment");
        }

        if (!adapter.isEmpty())
            movieSpinner.setAdapter(adapter);

        builder.setView(layout)
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            float stars      = ratingBar.getRating();
                            Movie movie      = movieInterface.getMovieByTitle(movieSpinner.getSelectedItem().toString());
                            String comment   = editText.getText().toString();
                            int ratingId     = ratingInterface.getNextRatingId();
                            // signal the rating interface that we want to add a new rating
                            ratingInterface.addRating( new Rating( ratingId,  stars, movie, comment), loginInterface.getLoggedInUser().getValue() );
                        } catch (NullPointerException e) {
                            Log.e(null, "null pointer exception at AddRatingDialog");
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddRatingDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

}
