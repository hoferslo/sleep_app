package com.example.sleep_app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sleep_app.R;

public class DeleteDreamDialogFragment extends DialogFragment {

    // Interface for the callback
    public interface DeleteDreamDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
    }

    private DeleteDreamDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // Inflate the custom layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_delete_dream_dialog, null);

        LinearLayout positiveButton = view.findViewById(R.id.btnPositive);
        positiveButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogPositiveClick();
                dismiss();
            }
        });

        LinearLayout negativeButton = view.findViewById(R.id.btnNegative);
        negativeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogNegativeClick();
                dismiss();
            }
        });

        builder.setView(view);

        return builder.create();
    }

    public void setDeleteDreamDialogListener(DeleteDreamDialogListener listener) {
        this.listener = listener;
    }
}
