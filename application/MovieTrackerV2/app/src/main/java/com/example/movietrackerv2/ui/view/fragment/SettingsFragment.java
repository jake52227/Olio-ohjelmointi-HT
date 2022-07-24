package com.example.movietrackerv2.ui.view.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.movietrackerv2.R;

// a fragment which opens form the three dots in the upper corner of the applications view. Does not contain anything at the moment
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}