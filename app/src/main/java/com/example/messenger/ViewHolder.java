package com.example.messenger;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView massage;
    public ViewHolder(View itemView){
        super(itemView);
        massage = itemView.findViewById(R.id.message_item);
    }

}

