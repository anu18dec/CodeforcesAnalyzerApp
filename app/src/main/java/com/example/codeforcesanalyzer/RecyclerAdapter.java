package com.example.codeforcesanalyzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<ContestModel> contestList;

    public RecyclerAdapter(Context context, ArrayList<ContestModel> contestList) {
        this.context = context;
        this.contestList = contestList;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.contest_row, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.titleText.setText(contestList.get(position).title);
        holder.statusText.setText(contestList.get(position).status);
        holder.dateText.setText(contestList.get(position).date);

    }

    @Override
    public int getItemCount() {
        return contestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView titleText;
        MaterialTextView statusText;
        MaterialTextView dateText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.contest_title);
            statusText = itemView.findViewById(R.id.contest_status);
            dateText = itemView.findViewById(R.id.contest_date);
        }
    }
}
