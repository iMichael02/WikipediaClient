package vn.edu.usth.wikiapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//^.\S{6,}$
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    Button changePwButton;
    EditText oldPw, newPw, cfNewPw;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldPw = getView().findViewById(R.id.oldPwEditText);
        newPw = getView().findViewById(R.id.newPwEditText);
        cfNewPw = getView().findViewById(R.id.cfNewPwEditText);


        Pattern pattern = Pattern.compile("^.\\S{6,}$");


        Matcher matcherOld = pattern.matcher(oldPw.getText().toString());
        Matcher matcherNew = pattern.matcher(newPw.getText().toString());
        Matcher matcherCfNew = pattern.matcher(cfNewPw.getText().toString());

        boolean matchFoundOld = matcherOld.find();
        boolean matchFoundNew = matcherNew.find();
        boolean matchFoundCfNew = matcherCfNew.find();



        String newPassword = String.valueOf(newPw.getText());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        changePwButton = getView().findViewById(R.id.changePwButton);
        changePwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((pattern.matcher(oldPw.getText().toString()).find()) ) {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), String.valueOf(oldPw.getText()));
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if((pattern.matcher(newPw.getText().toString()).find()) && (pattern.matcher(cfNewPw.getText().toString()).find()) &&(newPw.getText().toString().equals(cfNewPw.getText().toString())) ) {
                                        if (task.isSuccessful()) {
                                        user.updatePassword(String.valueOf(cfNewPw.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("statusUpdated", "Password updated");
                                                    getActivity().finish();
                                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Log.d("statusUpdated", "Error password not updated");
                                                }
                                            }
                                        });
                                    }
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Invalid user input. Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {

                    Toast.makeText(getContext(), "Invalid user input. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}