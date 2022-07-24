package com.example.movietrackerv2.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movietrackerv2.data.component.MovieEvent;
import com.example.movietrackerv2.databinding.FragmentMovieEventBinding;

import java.util.List;

// an adapter class for handling the recyclerview of the "Movie Events" tab
public class MovieEventRecyclerViewAdapter extends RecyclerView.Adapter<MovieEventRecyclerViewAdapter.ViewHolder> {

    private final List<MovieEvent> mValues;
    private final RecyclerViewOnClickInterface onClickInterface;

    public MovieEventRecyclerViewAdapter(List<MovieEvent> items, RecyclerViewOnClickInterface onClickInterface1) {
        mValues = items;
        this.onClickInterface = onClickInterface1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentMovieEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), onClickInterface);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getMovieTitle());
        holder.mContentView.setText(mValues.get(position).getStartTime());
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public MovieEvent mItem;

        public ViewHolder(FragmentMovieEventBinding binding, RecyclerViewOnClickInterface onClickInterface) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
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