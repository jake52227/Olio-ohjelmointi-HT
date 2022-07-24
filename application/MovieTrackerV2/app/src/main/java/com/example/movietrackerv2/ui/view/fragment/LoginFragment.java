package com.example.movietrackerv2.ui.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.ui.view.dialog.InformationDialog;
import com.example.movietrackerv2.ui.viewmodel.LoginInterface;
import com.example.movietrackerv2.ui.viewmodel.UserViewModel;

// Source for setting up login related things in login-, user- and ratings fragment: https://developer.android.com/guide/navigation/navigation-conditional#java
// a fragment displaying the user a login window
public class LoginFragment extends Fragment {

    public LoginFragment() {}
    private LoginInterface loginInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginInterface = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // this check is added because the user can log in at two places. If the user backs out of another login view and logs in at the other fragment, an "extra" log in fragment will be shown.
        // This removes that "extra" from the back stack, so the user does not have to login twice
        if (loginInterface.getLoggedInUser().getValue() != null) {
            NavHostFragment.findNavController(this).popBackStack();
        }

        Button loginButton      = (Button) view.findViewById(R.id.loginButton);
        Button registerButton   = (Button) view.findViewById(R.id.registerButton);
        EditText usernameText   = (EditText) view.findViewById(R.id.editTextTextUserName);
        EditText passwordText   = (EditText) view.findViewById(R.id.editTextTextPassword);

        loginButton.setOnClickListener(v -> {
            String password = passwordText.getText().toString();
            String username = usernameText.getText().toString();
            onLoginClick(password, username);
        });

        registerButton.setOnClickListener(v -> {
            String password = passwordText.getText().toString();
            String username = usernameText.getText().toString();
            onRegisterClick(password, username);
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // calls the login interface to check validity of the credentials. If login is a success, display success message and pop login fragment from stack returning to user fragment
    private void onLoginClick(String password, String username) {
        loginInterface.attemptLogIn(username, password).observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                showLoginSuccessMessage();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                showLoginFailMessage();
            }
        });
    }

    // calls the login interface to check validity of the credentials. If register is a success, adds a new user, displays a success message and pops login fragment from stack returning to user fragment
    private void onRegisterClick(String password, String username) {
        if (!loginInterface.checkPasswordValidity(password)) {
            showPasswordGuideDialog();
            return;
        }
        loginInterface.addUser(username, password).observe(getViewLifecycleOwner(), success -> {
            if (success) {
                showRegisterSuccessMessage();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                showRegisterFailMessage();
            }
        });

    }

    // displays a toast with a message
    private void showToast(String message) {
        Toast toast = new Toast(getContext());
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    // creates a dialog displaying a text guide on how to create an acceptable password
    private void showPasswordGuideDialog() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        InformationDialog frag = new InformationDialog( getResources().getString(R.string.password_help_dialog_title_text),
                getResources().getString(R.string.password_help_dialog_help_text));
        frag.show(fragmentManager, null);
    }

    private void showLoginFailMessage() {
        showToast(getResources().getString(R.string.login_fragment_login_fail));
    }

    private void showLoginSuccessMessage() {
        showToast(getResources().getString(R.string.login_fragment_login_success));
    }

    private void showRegisterFailMessage() {
        showToast(getResources().getString(R.string.login_fragment_register_fail));
    }

    private void showRegisterSuccessMessage() {
        showToast(getResources().getString(R.string.login_fragment_register_success));
    }
}