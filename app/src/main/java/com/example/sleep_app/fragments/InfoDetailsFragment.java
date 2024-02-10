package com.example.sleep_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.sleep_app.databinding.FragmentInfoDetailsBinding;

public class InfoDetailsFragment extends DialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";

    FragmentInfoDetailsBinding binding;

    public static InfoDetailsFragment newInstance(String title, String description) {
        InfoDetailsFragment fragment = new InfoDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    public InfoDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int theme = android.R.style.ThemeOverlay_Material_Dialog_Alert;
        setStyle(DialogFragment.STYLE_NORMAL, theme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve title and description from arguments
        String title = getArguments().getString(ARG_TITLE, "");
        String description = getArguments().getString(ARG_DESCRIPTION, "");

        // Set title and description in the fragment's UI
        binding.titleTv.setText(title);
        binding.descriptionTv.setText(description);

    }
}
