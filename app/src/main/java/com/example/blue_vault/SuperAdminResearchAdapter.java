package com.example.blue_vault;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SuperAdminResearchAdapter extends RecyclerView.Adapter<SuperAdminResearchAdapter.ViewHolder> {

    private List<ResearchItem> researchList;
    private List<ResearchItem> researchListFull;

    public SuperAdminResearchAdapter(List<ResearchItem> researchList) {
        this.researchList = researchList;
        this.researchListFull = new ArrayList<>(researchList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_super_admin_available_researches, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResearchItem item = researchList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvAuthor.setText(item.getAuthor());
        holder.tvCourse.setText(item.getCourse());
        holder.tvDate.setText(item.getDate());
        holder.tvTags.setText(item.getTags());
        
        // Handle int status: 0=Declined, 1=Approved, 3=Pending
        int status = item.getStatus();
        if (status == DataRepository.STATUS_APPROVED) {
            holder.tvStatus.setText("Approved");
            holder.tvStatus.setTextColor(Color.GREEN);
        } else if (status == DataRepository.STATUS_DECLINED) {
            holder.tvStatus.setText("Declined");
            holder.tvStatus.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setText("Pending");
            holder.tvStatus.setTextColor(Color.YELLOW);
        }

        holder.btnSuperView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), view_research_super_admin.class);
            intent.putExtra("research_data", item); 
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return researchList.size();
    }

    public void filter(String statusStr) {
        researchList.clear();
        if (statusStr.equals("ALL")) {
            researchList.addAll(researchListFull);
        } else {
            int statusToFilter = -1;
            if (statusStr.equalsIgnoreCase("Approved")) statusToFilter = DataRepository.STATUS_APPROVED;
            else if (statusStr.equalsIgnoreCase("Declined")) statusToFilter = DataRepository.STATUS_DECLINED;
            else if (statusStr.equalsIgnoreCase("Pending")) statusToFilter = DataRepository.STATUS_PENDING;

            for (ResearchItem item : researchListFull) {
                if (item.getStatus() == statusToFilter) {
                    researchList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvAuthor, tvCourse, tvDate, tvStatus, tvTags;
        public Button btnSuperView;

        public ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvAuthor = view.findViewById(R.id.tvAuthor);
            tvCourse = view.findViewById(R.id.tvCourse);
            tvDate = view.findViewById(R.id.tvDate);
            tvTags = view.findViewById(R.id.tvTags);
            tvStatus = view.findViewById(R.id.textView3);
            btnSuperView = view.findViewById(R.id.btnSuperViewResearch);
        }
    }
}