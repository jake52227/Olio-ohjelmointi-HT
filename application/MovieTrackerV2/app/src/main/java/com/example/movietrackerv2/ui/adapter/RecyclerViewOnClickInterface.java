package com.example.movietrackerv2.ui.adapter;

// source for everything related to this interface is Practical Coding's video on the subject: https://www.youtube.com/watch?v=7GPUpvcU1FE

// the purpose of this is to set up an interface for a recycler view adapter and a fragment to communicate with when the user clicks an item in the recyclerview
// the adapter notifies the fragment and the fragment can react to this.
public interface RecyclerViewOnClickInterface {
    void onItemClick(int index);
}
