package com.example.sleep_app.mainActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sleep_app.LiquidView;
import com.example.sleep_app.databinding.FragmentOverviewBinding;

public class OverviewFragment extends Fragment {

    FragmentOverviewBinding binding;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        // Create an instance of LiquidView
        LiquidView liquidView = new LiquidView(requireContext(), null);

        // Set layout parameters for the LiquidView
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        liquidView.setLayoutParams(layoutParams);

        // Add the LiquidView to the container in your layout (assuming you have a container with ID "liquidContainer")
        LinearLayout liquidContainer = binding.liquidContainer;
        liquidContainer.addView(liquidView);

        return binding.getRoot();
    }
}
