package com.example.mychat.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mychat.R;
import com.example.mychat.activities.allusers;
import com.example.mychat.constants.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class loginTabFragment extends Fragment {
    EditText _email, _pass;
    FirebaseAuth auth;
    Button b;
    ProgressBar bar;
    SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflator.inflate(R.layout.login_fragment, container, false);
        _email = (EditText) root.findViewById(R.id.email);
        _pass = (EditText) root.findViewById(R.id.pass);
        preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        auth = FirebaseAuth.getInstance();
        b = root.findViewById(R.id.button);
        bar = new ProgressBar(getActivity());
        bar = (ProgressBar) root.findViewById(R.id.progbar);
        bar.setVisibility(View.INVISIBLE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = preferences.edit();
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection(Constants.KEY_COLLECTION_USER)
                        .whereEqualTo(Constants.KEY_EMAIL, _email.getText().toString())
                        .whereEqualTo(Constants.KEY_PASSWORD, _pass.getText().toString())
                        .get()
                        .addOnCompleteListener(task ->
                        {
                            if(task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0)
                            {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                                editor.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                                editor.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                                editor.apply();


                            }
                        });

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
                                    startActivity(new Intent(getActivity(), allusers.class));
                                    getActivity().finish();
                                }
                            }
                        });

            }
        });


        return root;
    }
    public void addDataToFirebase()
    {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();





    }

}
