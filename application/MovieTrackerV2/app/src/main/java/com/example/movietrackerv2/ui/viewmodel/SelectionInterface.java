package com.example.movietrackerv2.ui.viewmodel;

import androidx.lifecycle.LiveData;

import java.util.List;

// an interface used mainly for the purpose of setting up a multi choice list
public interface SelectionInterface {
    List<String> getListOfOptions();
    LiveData<boolean[]> getCheckedItems();
    void addSelectedItem(int which);
    void removeSelectedItem(Integer which);
    List<Integer> getSelectionIndices();
}
