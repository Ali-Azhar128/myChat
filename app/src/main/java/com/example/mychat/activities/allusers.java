package com.example.mychat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class allusers extends AppCompatActivity {
    ActivityAllusersBinding binding;
    FirebaseFirestore firebaseFirestore;
    private List<ChatMessage> conversations;
    private RecentConversationAdapter conversationAdapter;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllusersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);


        setListeners();
        listenConversations();


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

    private void listenConversations() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, userId)
                .addSnapshotListener(eventListener);
        firebaseFirestore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userId)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null) {
            return;
        }
        if(value != null) {
            for(DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if(userId.equals(senderId)) {
                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.conversationName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversationId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    }else {
                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.conversationName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversationId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);


                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for(int i = 0 ; i < conversations.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationAdapter.notifyDataSetChanged();
            binding.conversationRecyclerView.smoothScrollToPosition(0);
            binding.conversationRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);


        }
    };
    public void signOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }
}