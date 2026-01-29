package com.example.blue_vault;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminResearchAdapter extends RecyclerView.Adapter<AdminResearchAdapter.ViewHolder> {

    private List<ResearchItem> list;
    private OnItemClickListener listener;

    // Define the interface for the click
    public interface OnItemClickListener {
        void onItemClick(ResearchItem item);
    }

    public AdminResearchAdapter(List<ResearchItem> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_admin_item_research, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResearchItem item = list.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvAuthor.setText(item.getAuthor());
        holder.tvCourse.setText(item.getCourse());
        holder.tvDate.setText(item.getDate());
        holder.tvTags.setText(item.getTags());

        // Handle Click
        holder.btnAdminView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), view_research_admin.class);
            intent.putExtra("RESEARCH_ITEM", item);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvAuthor, tvCourse, tvDate, tvStatus, tvTags;
        public Button btnAdminView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvCourse = itemView.findViewById(R.id.tvCourse);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTags = itemView.findViewById(R.id.tvTags);
            btnAdminView = itemView.findViewById(R.id.btnAdminViewResearch);
        }
    }
}