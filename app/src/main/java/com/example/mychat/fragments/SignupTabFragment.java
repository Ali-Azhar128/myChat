package com.example.mychat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mychat.activities.ChatActivity;
import com.example.mychat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SignupTabFragment extends Fragment {
    EditText email, pass, cpass;
    Button b;
    FirebaseAuth auth;
    ViewGroup root;
    ProgressBar bar;


    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        root = (ViewGroup) inflator.inflate(R.layout.signup_fragment, container, false);
        email = (EditText) root.findViewById(R.id.signupemail);
        pass = (EditText) root.findViewById(R.id.pass);
        cpass = (EditText) root.findViewById(R.id.confirmpass);
        auth = FirebaseAuth.getInstance();
        b = (Button) root.findViewById(R.id.b1);
        bar = (ProgressBar) root.findViewById(R.id.progbar2);
        bar.setVisibility(View.INVISIBLE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                String emailText = email.getText().toString();
                String passText = pass.getText().toString();

                if (TextUtils.isEmpty(emailText)) {
                    bar.setVisibility(View.INVISIBLE);
                    email.setError("Email can't be empty");
                }
                if (TextUtils.isEmpty(passText)) {
                    bar.setVisibility(View.INVISIBLE);
                    pass.setError("Pass can't be empty");
                }

                if (pass.length() == 0) {
                    bar.setVisibility(View.INVISIBLE);
                    pass.setError("Enter a valid password");
                }
                if (pass.length() < 8) {
                    bar.setVisibility(View.INVISIBLE);
                    pass.setError("Enter a secure password");
                }
                if(!(pass.getText().toString().equals(cpass.getText().toString())))
                {
                    bar.setVisibility(View.INVISIBLE);
                    pass.setError("");
                    cpass.setError("Password does not match");
                }
                else
                {
                    auth.createUserWithEmailAndPassword(emailText, passText)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        bar.setVisibility(View.INVISIBLE);
                                        try {
                                            throw task.getException();
                                        } catch(FirebaseAuthWeakPasswordException e) {
                                            pass.setError("Weak Password");
                                            pass.requestFocus();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            email.setError("Invalid Email");
                                            email.requestFocus();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            email.setError("User Already Exist");
                                            email.requestFocus();
                                        } catch(Exception e) {
                                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        bar.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(getActivity(), ChatActivity.class));
                                        getActivity().finish();
                                    }
                                }
                            });







                }

            }


        });


        return root;
    }


}