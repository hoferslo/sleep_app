package com.example.sleep_app.mainActivityFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sleep_app.R;
import com.example.sleep_app.databinding.FragmentOverviewBinding;
import com.scwang.wave.MultiWaveHeader;

import java.util.ArrayList;
import java.util.Random;

public class OverviewFragment extends Fragment {

    FragmentOverviewBinding binding;
    private ArrayAdapter<String> adapter;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        adapter = new CustomAdapter(requireContext(), R.layout.item_dream_stats_layout, new ArrayList<>());
        binding.scrollLv.setAdapter(adapter);
        binding.scrollLv.setDivider(null);

        new AddItemsAsyncTask().execute();

        return binding.getRoot();
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private final LayoutInflater inflater;
        private final int resource;

        public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
            super(context, resource, objects);
            this.inflater = LayoutInflater.from(context);
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            // Inflate your custom layout for each item
            if (convertView == null) {
                convertView = inflater.inflate(resource, parent, false);
            }

            // Customize the view based on the data (if needed)
            MultiWaveHeader waveView = convertView.findViewById(R.id.waveHeader);
            //waveView.setWaveHeight(5);
            waveView.setProgress(0.5F);

            return convertView;
        }
    }

    private class AddItemsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(10); // Simulating some background task
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(); // Update UI on the main thread
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // Update UI on the main thread
            adapter.add("overview");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // This is called when the task is complete (optional)
        }
    }
}
