package com.example.movietrackerv2.ui.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.Rating;
import com.example.movietrackerv2.ui.viewmodel.LoginInterface;
import com.example.movietrackerv2.ui.viewmodel.RatingInterface;

// A dialog which displays information about a rating the user has created
public class CheckRatingDialogFragment extends DialogFragment {

    private int index; // an index which determines which rating we want information from
    private RatingInterface ratingInterface;
    private LoginInterface loginInterface;

    public CheckRatingDialogFragment(int index, RatingInterface ratingInterface, LoginInterface loginInterface) {
        this.index = index;
        this.ratingInterface = ratingInterface;
        this.loginInterface = loginInterface;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.check_rating_dialog, null);

        // get the correct rating from the interface
        Rating rating = ratingInterface.getRatingByIndex(loginInterface.getLoggedInUser().getValue(), index);

        RatingBar ratingBar      = (RatingBar) layout.findViewById(R.id.check_rating_ratingbar);
        TextView titleTextView   = (TextView) layout.findViewById(R.id.title_TextView);
        TextView genresTextView  = (TextView) layout.findViewById(R.id.genres_TextView);
        TextView commentText     = (TextView) layout.findViewById(R.id.check_rating_comment_text);

        ratingBar.setRating(rating.getStars());
        titleTextView.setText( new StringBuilder().append(titleTextView.getText().toString()).append(" ").append(rating.getMovie().getTitle()).toString());
        genresTextView.setText(new StringBuilder().append(genresTextView.getText().toString()).append(" ").append(rating.getMovie().getGenres()).toString());
        commentText.setText(new StringBuilder().append(commentText.getText().toString()).append(" ").append(rating.getComment()).toString());

        Button deleteButton = (Button) layout.findViewById(R.id.check_rating_delete_button);
        deleteButton.setOnClickListener(v -> {
            ratingInterface.deleteRating(rating.getId(), loginInterface.getLoggedInUser().getValue());
        });

        builder.setView(layout)
                .setPositiveButton("done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                });

        return builder.create();
    }
}
