package com.example.sleep_app.mainActivityFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sleep_app.Dream;
import com.example.sleep_app.R;
import com.example.sleep_app.databinding.FragmentDreamsBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;

import java.util.ArrayList;
import java.util.List;


public class DreamsFragment extends Fragment {

    FragmentDreamsBinding binding;


    public DreamsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDreamsBinding.inflate(inflater, container, false);

        getDreams();

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
    }

    private static class CustomAdapter extends ArrayAdapter<Dream> {
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
            TextView textView = convertView.findViewById(R.id.dreamTitleTv);
            textView.setText(objects.get(position).getTitle());
            return convertView;
        }
    }
}