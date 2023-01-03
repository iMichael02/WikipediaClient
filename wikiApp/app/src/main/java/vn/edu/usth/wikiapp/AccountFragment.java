package vn.edu.usth.wikiapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private Button signup_button;
    private EditText username, password;
    private FirebaseAuth mAuth;
// ...


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

        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.i("Status","Logged in successfully");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();

        Button signin_button = (Button) view.findViewById(R.id.signin_button);

        // username: admin, password: admin
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
//                    Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
//
//                } else
//                    Toast.makeText(getContext(), "Login Failed! Account does not exist!", Toast.LENGTH_SHORT).show();
//                Log.d("username+pw", String.valueOf(username.getText())+"-----"+String.valueOf(password.getText()));
                mAuth.signInWithEmailAndPassword(String.valueOf(username.getText()), String.valueOf(password.getText()))
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
        Intent intent = new Intent(getContext(), activity_signup.class);
        startActivity(intent);
    }
}