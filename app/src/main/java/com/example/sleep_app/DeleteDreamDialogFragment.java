package com.example.sleep_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

        // Customize the dialog appearance using the inflated view
        TextView titleTextView = view.findViewById(R.id.dialogTitle);
        titleTextView.setText("Delete Dream");

        TextView messageTextView = view.findViewById(R.id.dialogMessage);
        messageTextView.setText("Are you sure you want to delete this dream?");

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
