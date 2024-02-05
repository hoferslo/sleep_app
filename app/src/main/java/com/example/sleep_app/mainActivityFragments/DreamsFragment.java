package com.example.sleep_app.mainActivityFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sleep_app.Dream;
import com.example.sleep_app.DreamDetailsFragment;
import com.example.sleep_app.R;
import com.example.sleep_app.databinding.FragmentDreamsBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;

import java.util.ArrayList;
import java.util.List;


public class DreamsFragment extends Fragment implements DreamDetailsFragment.OnDialogDismissListener {

    FragmentDreamsBinding binding;
    public int scrollProgress;


    public DreamsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDreamsBinding.inflate(inflater, container, false);

        scrollProgress = 0;

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDreams();
    }


    private void getDreams(){
        DreamsAccess dreamsAccess = new DreamsAccess(requireContext());
        dreamsAccess.open();
        List<Dream> dreamList = dreamsAccess.getDreams();
        dreamsAccess.close();
        ArrayList<Dream> dreamArrayList = new ArrayList<>(dreamList);
        ArrayAdapter<Dream> adapter = new CustomAdapter(requireContext(), R.layout.item_dream_layout, dreamArrayList);
        binding.scrollLv.setAdapter(adapter);
        binding.scrollLv.setDivider(null);
        binding.scrollLv.setSelection(scrollProgress);
        scrollProgress = 0;
        Log.d("DreamsFragment onResume", "getDreams() method was called");
    }

    @Override
    public void onDialogDismissed() {
        onResume();
    }

    private class CustomAdapter extends ArrayAdapter<Dream> {
        private final LayoutInflater inflater;
        private final int resource;
        private final ArrayList<Dream> objects;

        public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Dream> objects) {
            super(context, resource, objects);
            this.inflater = LayoutInflater.from(context);
            this.resource = resource;
            this.objects = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Inflate your custom layout for each item
            if (convertView == null) {
                convertView = inflater.inflate(resource, parent, false);
            }

            // Customize the view based on the data (if needed)
            TextView titleTextView = convertView.findViewById(R.id.dreamTitleTv);
            titleTextView.setText(objects.get(position).getTitle());

            TextView lucidityTextView = convertView.findViewById(R.id.dreamLucidityTv);
            lucidityTextView.setText(String.valueOf(objects.get(position).getLucidity()));

            TextView clarityTextView = convertView.findViewById(R.id.dreamClarityTv);
            clarityTextView.setText(String.valueOf(objects.get(position).getClarity()));

            TextView descriptionTextView = convertView.findViewById(R.id.dreamDescriptionTv);
            descriptionTextView.setText(objects.get(position).getDescription());

            convertView.setOnClickListener(v -> {
                scrollProgress = binding.scrollLv.getFirstVisiblePosition();
                Dream clickedDream = objects.get(position);
                showMyDialog(clickedDream);

            });
            return convertView;
        }
    }
    private void showMyDialog(Dream clickedDream) {
        DreamDetailsFragment dialogFragment = DreamDetailsFragment.newInstance(clickedDream);
        dialogFragment.setOnDialogDismissListener(this);
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "dream_details_dialog");
    }

}