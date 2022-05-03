package com.example.mychat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mychat.R;

public class loginTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflator.inflate(R.layout.login_fragment, container, false);
        return root;
    }
}
