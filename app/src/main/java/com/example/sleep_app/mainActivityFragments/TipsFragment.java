package com.example.sleep_app.mainActivityFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.sleep_app.databinding.FragmentTipsBinding;

import java.time.LocalDateTime;

public class TipsFragment extends Fragment {

    FragmentTipsBinding binding;
    LocalDateTime now;

    public TipsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTipsBinding.inflate(inflater, container, false);

        now = LocalDateTime.now();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebSettings webSettings = binding.webView.getSettings();
        binding.webView.setWebViewClient(new WebViewClient());

        String url = "https://www.reddit.com/r/LucidDreaming/wiki/index/";  // Replace with your actual URL
        binding.webView.loadUrl(url); // Replace with the URL of the website


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TipsFragment onResume", "no method was called");
    }

}