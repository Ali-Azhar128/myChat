package com.example.mychat.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.databinding.ItemContainerUserBinding;
import com.example.mychat.listeners.UserListener;
import com.example.mychat.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> user;
    private final UserListener userListener;

    public UserAdapter(List<User> user, UserListener userListener) {
        this.user = user;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(LayoutInflater.from(parent
        .getContext()), parent, false);
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(user.get(position));



    }

    @Override
    public int getItemCount() {
        return user.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder {
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        ItemContainerUserBinding binding;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding)//Because we have enabled view binding
        {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;

        }
        void setUserData(User user)
        {
            binding.textName.setText(user.getName());
            binding.textEmail.setText(user.getEmail());
            binding.layoutImage.setImageBitmap(getUserImage(user.getProfileImage()));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap getUserImage(String encodedImage)
    {

        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
