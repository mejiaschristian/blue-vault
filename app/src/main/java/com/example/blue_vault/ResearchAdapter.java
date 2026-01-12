package com.example.blue_vault;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResearchAdapter extends RecyclerView.Adapter<ResearchAdapter.ViewHolder> {

    private List<ResearchItem> researchList;

    public ResearchAdapter(List<ResearchItem> researchList) {
        this.researchList = researchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_research, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResearchItem item = researchList.get(position);
        holder.rsTitle.setText(item.getTitle());
        holder.rsAuthor.setText(item.getAuthor());
        holder.rsSchool.setText(item.getSchool());
        holder.rsCourse.setText(item.getCourse());
        holder.rsDate.setText(item.getDate());

        // Handle the click event for the invisible button overlaying the entire item
        holder.btnItem.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), view_research_user.class);
            // In a real app, you would pass the item details or ID here:
            // intent.putExtra("title", item.getTitle());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return researchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rsTitle, rsAuthor, rsSchool, rsCourse, rsDate;
        public Button btnItem;

        public ViewHolder(View view) {
            super(view);
            rsTitle = view.findViewById(R.id.tvTitle);
            rsAuthor = view.findViewById(R.id.tvAuthor);
            rsSchool = view.findViewById(R.id.tvSchool);
            rsCourse = view.findViewById(R.id.tvCourse);
            rsDate = view.findViewById(R.id.tvDate);
            btnItem = view.findViewById(R.id.research_item_btn);
        }
    }
}