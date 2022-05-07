package com.example.mychat.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychat.activities.allusers;
import com.example.mychat.R;
import com.example.mychat.constants.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignupTabFragment extends Fragment {
    EditText email, pass, cpass, username;
    TextView tv2;
    Button b;
    FirebaseAuth auth;
    ViewGroup root;
    ProgressBar bar;
    ImageView imageView;
    String encodedImage;


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
        imageView = (ImageView) root.findViewById(R.id.layoutImage);
        tv2 = (TextView) root.findViewById(R.id.textAddImage);
        username = (EditText) root.findViewById(R.id.inputName);

        setListeners();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                String emailText = email.getText().toString();
                String passText = pass.getText().toString();

                if(imageView == null)
                {
                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Select an Image", Toast.LENGTH_SHORT).show();
                }

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
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                            {
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
                                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                        HashMap<String, Object> user = new HashMap<>();
                                        user.put(Constants.KEY_NAME,username.getText().toString());
                                        user.put(Constants.KEY_EMAIL, email.getText().toString());
                                        user.put(Constants.KEY_PASSWORD, pass.getText().toString());
                                        user.put(Constants.KEY_IMAGE, encodedImage);
                                        user.put("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        firebaseFirestore.collection(Constants.KEY_COLLECTION_USER).
                                                add(user).addOnSuccessListener(
                                                        documentReference -> {

                                                        }).addOnFailureListener(exception ->

                                        {


                                        });


                                        bar.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(getActivity(), allusers.class));
                                        getActivity().finish();
                                    }
                                }
                            });
                }

            }


        });


        return root;
    }
    private void setListeners()
    {

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);

        });


    }






    private String encodedImage(Bitmap bitmap)
    {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);


    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK)
                {
                    if(result.getData() != null)
                    {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageView.setImageBitmap(bitmap);
                            tv2.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);


                        }catch(FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


}