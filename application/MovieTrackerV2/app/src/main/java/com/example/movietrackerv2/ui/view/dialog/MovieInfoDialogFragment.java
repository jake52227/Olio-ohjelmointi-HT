package com.example.movietrackerv2.ui.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.Movie;
import com.example.movietrackerv2.ui.viewmodel.MovieSearchInterface;

// a dialog displaying information about a movie
public class MovieInfoDialogFragment extends DialogFragment {

    private int index;
    private MovieSearchInterface movieSearchInterface;

    public MovieInfoDialogFragment(int index, MovieSearchInterface movieSearchInterface) {
        this.index = index;
        this.movieSearchInterface = movieSearchInterface;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.movie_info_dialog, null);

        TextView titleText       = (TextView) layout.findViewById(R.id.info_dialog_movie_title);
        TextView genresText      = (TextView) layout.findViewById(R.id.info_dialog_movie_genres);
        TextView actorsText      = (TextView) layout.findViewById(R.id.info_dialog_actors);
        TextView directorsText   = (TextView) layout.findViewById(R.id.info_dialog_directors);

        // get the correct movie from the interface
        Movie movie = null;
        try {
            movie = movieSearchInterface.geFilteredListOfMovies().get(index);
        } catch (NullPointerException e) {
            Log.e(null, "Null pointer exception at MovieInfoDialogFragment");
        }
        if (movie != null) {
            if (movie.getTitle() != null)
                titleText.setText(new StringBuilder().append("Title: ").append(movie.getTitle()).toString());

            if (movie.getGenres() != null)
                genresText.setText(new StringBuilder().append("Genres: ").append(movie.getGenres()).toString());

            if (movie.getActors() != null)
                actorsText.setText(new StringBuilder().append("Actors: ").append(movie.getActorNamesAsString()).toString());

            if (movie.getDirectors() != null)
                directorsText.setText(new StringBuilder().append("Directors: ").append(movie.getDirectorNamesAsString()).toString());
        }

        builder.setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                });

        return builder.create();
    }

}
