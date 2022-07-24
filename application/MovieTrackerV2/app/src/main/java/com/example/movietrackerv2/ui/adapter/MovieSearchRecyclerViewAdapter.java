package com.example.movietrackerv2.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movietrackerv2.data.component.Movie;
import com.example.movietrackerv2.databinding.FragmentMovieSearchBinding;


import java.util.List;

// an adapter class for handling the recycler view of the "Search Movies" tab
public class MovieSearchRecyclerViewAdapter extends RecyclerView.Adapter<MovieSearchRecyclerViewAdapter.ViewHolder> {

    private final List<Movie> mValues;
    private final RecyclerViewOnClickInterface onClickInterface;

    public MovieSearchRecyclerViewAdapter(List<Movie> items, RecyclerViewOnClickInterface onClickInterface) {
        mValues = items;
        this.onClickInterface = onClickInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentMovieSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), onClickInterface);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public Movie mItem;

        public ViewHolder(FragmentMovieSearchBinding binding, RecyclerViewOnClickInterface onClickInterface) {
            super(binding.getRoot());
            mContentView = binding.content;

            // set up onClickInterface for recyclerview
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickInterface != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onClickInterface.onItemClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}