package vn.edu.usth.wikiapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private Button signup_button;
    private EditText username, password;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);

        Button signin_button = (Button) view.findViewById(R.id.signin_button);

        // username: admin, password: admin
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                    Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "Login Failed! Account does not exist!", Toast.LENGTH_SHORT).show();
            }
        });

        signup_button = (Button) view.findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opensignup();
            }
        });
        return view;
    }

    public void opensignup() {
        Intent intent = new Intent(getActivity(), activity_signup.class);
        startActivity(intent);
    }
}