package com.example.palakarora_shivangishandilya_comp304_lab5;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LandmarkAdapter extends RecyclerView.Adapter<com.example.palakarora_shivangishandilya_comp304_lab5.LandmarkAdapter.LandmarkHolder> {

    private final Landmarks[] landmarks;

    public LandmarkAdapter(@NonNull Landmarks[] data) {
        landmarks = data;
    }

    @NonNull
    @Override
    public LandmarkHolder onCreateViewHolder(@NonNull ViewGroup p, int type) {
        LayoutInflater in = LayoutInflater.from(p.getContext());
        View view = in.inflate(R.layout.landmark_view, p, false);
        return new com.example.palakarora_shivangishandilya_comp304_lab5.LandmarkAdapter.LandmarkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandmarkHolder holder, int i) {
        Log.d("onBindViewHolder", landmarks[i].getName());
        holder.setLandmarks(landmarks[i]);
    }

    @Override
    public int getItemCount() {
        return landmarks.length;
    }

    public static class LandmarkHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Landmarks landmarks;

        private final TextView nameTextView, typeTextView, addressTextView;

        public LandmarkHolder(@NonNull View view) {
            super(view);
            nameTextView = view.findViewById(R.id.landmarkNameTextView);
            typeTextView = view.findViewById(R.id.landmarkTypeTextView);
            addressTextView = view.findViewById(R.id.landmarkAddressTextView);

            view.setOnClickListener(this);
        }

        public void setLandmarks(Landmarks landmarks) {
            this.landmarks = landmarks;
            nameTextView.setText(landmarks.getName());
            addressTextView.setText(landmarks.getAddress());

            LandmarkType type = landmarks.getType();
            typeTextView.setText(String.format("%s", type.format(false)));
            typeTextView.setTextColor(type.getColorFrom(itemView));
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(itemView.getContext(),
                "Clicked " + landmarks.getName(),
                Toast.LENGTH_SHORT).show();

            Context ctx = v.getContext();
            Intent in = new Intent(ctx, MapsActivity.class);
            in.putExtra(Landmarks.ID_EXTRA, landmarks.getLandmarkId());
            ctx.startActivity(in);
        }
    }
}
