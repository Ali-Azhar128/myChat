package com.example.mychat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.example.mychat.R;
import com.example.mychat.adapters.RecentConversationAdapter;
import com.example.mychat.constants.Constants;
import com.example.mychat.databinding.ActivityAllusersBinding;
import com.example.mychat.models.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class allusers extends AppCompatActivity {
    ActivityAllusersBinding binding;
    FirebaseFirestore firebaseFirestore;
    private List<ChatMessage> conversations;
    private RecentConversationAdapter conversationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllusersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);

        setListeners();


    }


    private void init(){
        conversations = new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(conversations);
        binding.conversationRecyclerView.setAdapter(conversationAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    public void setListeners()
    {

        binding.imageSignOut.setOnClickListener(view -> signOut());
        binding.fabNewChat.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USER).get()
                .addOnCompleteListener(task -> {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if(task.isSuccessful() && task.getResult() != null)
                    {
                        for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult())
                        {
                            if(userId.equals(queryDocumentSnapshot.getString("UID")))
                            {
                                byte bytes[] = Base64.decode(queryDocumentSnapshot.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                binding.layoutImage.setImageBitmap(bitmap);
                                binding.textname.setText(queryDocumentSnapshot.getString(Constants.KEY_NAME));

                            }
                        }

                    }
                });

    }
    public void signOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }
}