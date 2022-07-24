package com.example.movietrackerv2.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movietrackerv2.data.component.Rating;
import com.example.movietrackerv2.databinding.FragmentRatingArchiveBinding;

import java.util.List;

// an adapter class for handling the recyclerview of the "My Ratings" tab
public class RatingArchiveRecyclerViewAdapter extends RecyclerView.Adapter<RatingArchiveRecyclerViewAdapter.ViewHolder> {

    private final List<Rating> mValues;
    private final RecyclerViewOnClickInterface onClickInterface;

    public RatingArchiveRecyclerViewAdapter(List<Rating> items, RecyclerViewOnClickInterface onClickInterface) {
        mValues = items;
        this.onClickInterface = onClickInterface;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentRatingArchiveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), onClickInterface);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getMovie().getTitle());        // recyclerview first text: title of movie
        holder.mContentView.setText(mValues.get(position).getDate());                 // second text: date of rating
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mContentView;
        public Rating mItem;

        public ViewHolder(FragmentRatingArchiveBinding binding, RecyclerViewOnClickInterface onClickInterface) {
            super(binding.getRoot());
            mNameView = binding.itemNumber;
            mContentView = binding.content;


            // set up the on click action with the OnClickInterface interface
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