package com.example.sleep_app.mainActivityFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sleep_app.R;
import com.example.sleep_app.databinding.FragmentTipsBinding;
import com.example.sleep_app.fragments.InfoDetailsFragment;

import java.time.LocalDateTime;

public class TipsFragment extends Fragment {

    FragmentTipsBinding binding;
    LocalDateTime now;
    String url = "";

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

        //binding.techniquesLl.setOnClickListener(view1 -> {
        //    showDetails(getString(R.string.technique_title), getString(R.string.technique_description));
        //});

        binding.WBTBbtn.setOnClickListener(view1 -> {
            showDetails(getString(R.string.technique_WBTB_title), getString(R.string.technique_WBTB_description));
            url = getString(R.string.technique_WBTB_url);
        });
        binding.MILDbtn.setOnClickListener(view1 -> {
            showDetails(getString(R.string.technique_MILD_title), getString(R.string.technique_MILD_description));
            url = getString(R.string.technique_MILD_url);
        });
        binding.WILDbtn.setOnClickListener(view1 -> {
            showDetails(getString(R.string.technique_WILD_title), getString(R.string.technique_WILD_description));
            url = getString(R.string.technique_WILD_url);
        });
        binding.FILDbtn.setOnClickListener(view1 -> {
            showDetails(getString(R.string.technique_FILD_title), getString(R.string.technique_FILD_description));
            url = getString(R.string.technique_FILD_url);
        });
        binding.SSILDbtn.setOnClickListener(view1 -> {
            showDetails(getString(R.string.technique_SSILD_title), getString(R.string.technique_SSILD_description));
            url = getString(R.string.technique_SSILD_url);

        });
        binding.VILDbtn.setOnClickListener(view1 -> {
            showDetails(getString(R.string.technique_VILD_title), getString(R.string.technique_VILD_description));
            url = getString(R.string.technique_VILD_url);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        infoHome();
    }

    private void replaceFragment(ViewFlipper viewFlipper, int index, Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the existing fragment at index with the new one
        transaction.replace(viewFlipper.getChildAt(index).getId(), fragment);

        transaction.commit();
    }

    public boolean onBackPressed() {
        url = "";
        if (binding.viewFlipper.getDisplayedChild() == 1) {
            binding.viewFlipper.setDisplayedChild(0);
            return false;
        }
        return true;
    }

    private void showDetails(String title, String description) {
        InfoDetailsFragment dialogFragment = InfoDetailsFragment.newInstance(title, description);
        replaceFragment(binding.viewFlipper, 1, dialogFragment);
        binding.viewFlipper.setDisplayedChild(1);
    }

    public void openLink() {
        if (!url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else {
            Toast.makeText(requireContext(), "Choose a category to open external information in the browser.", Toast.LENGTH_SHORT).show();
        }
    }

    public void infoHome() {
        if (binding.viewFlipper.getDisplayedChild() == 1) {
            binding.viewFlipper.setDisplayedChild(0);
        }
    }
}