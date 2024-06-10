package com.example.messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList<String> masseges;
    LayoutInflater inflater;

    public DataAdapter(Context context, ArrayList<String> masseges) {
        this.masseges = masseges;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_massage, parent, false);
        return new ViewHolder(view);
    }

    @Nullable
    private static ViewHolder getViewHolder() {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String msg = masseges.get(position);
        holder.massage.setText(msg);
    }

    @Override
    public int getItemCount() {
        return masseges.size();
    }
}
