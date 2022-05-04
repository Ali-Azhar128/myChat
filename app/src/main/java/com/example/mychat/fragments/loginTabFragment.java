package com.example.mychat.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mychat.R;
import com.example.mychat.activities.ChatActivity;
import com.example.mychat.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class loginTabFragment extends Fragment {
    EditText _email, _pass;
    FirebaseAuth auth;
    Button b;
    ProgressBar bar;


    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflator.inflate(R.layout.login_fragment, container, false);
        _email = (EditText) root.findViewById(R.id.email);
        _pass = (EditText) root.findViewById(R.id.pass);
        auth = FirebaseAuth.getInstance();
        b = root.findViewById(R.id.button);
        bar = new ProgressBar(getActivity());
        bar = (ProgressBar) root.findViewById(R.id.progbar);
        bar.setVisibility(View.INVISIBLE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(_email.getText().toString(), _pass.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {

                                if (!task.isSuccessful()) {
                                    bar.setVisibility(View.INVISIBLE);
                                    try {
                                        throw task.getException();
                                    } catch (Exception e)
                                    {

                                        _pass.setError(e.toString());
                                        _email.setError(e.toString());
                                    }
                                } else {
                                    bar.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(getActivity(), ChatActivity.class));
                                    getActivity().finish();
                                }
                            }
                        });

            }
        });


        return root;
    }
    public void login(View v)
    {




    }

}
