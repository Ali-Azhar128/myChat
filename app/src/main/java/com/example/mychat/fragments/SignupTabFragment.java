package com.example.mychat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mychat.ChatActivity;
import com.example.mychat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
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


    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        root = (ViewGroup) inflator.inflate(R.layout.signup_fragment, container, false);
        email = (EditText) root.findViewById(R.id.signupemail);
        pass = (EditText) root.findViewById(R.id.pass);
        cpass = (EditText) root.findViewById(R.id.confirmpass);
        auth = FirebaseAuth.getInstance();
        b = (Button) root.findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passText = pass.getText().toString();

                if (TextUtils.isEmpty(emailText)) {
                    email.setError("Email can't be empty");
                }
                if (TextUtils.isEmpty(passText)) {
                    pass.setError("Pass can't be empty");
                }

                if (pass.length() == 0) {
                    pass.setError("Enter a valid password");
                }
                if (pass.length() < 8) {
                    pass.setError("Enter a secure password");
                }
                else
                {
                    auth.createUserWithEmailAndPassword(emailText, passText)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
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