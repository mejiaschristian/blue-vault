package com.example.blue_vault;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResearchAdapter extends RecyclerView.Adapter<ResearchAdapter.ViewHolder> {

    private List<ResearchItem> researchList;
    private boolean isProfileView;

    public ResearchAdapter(List<ResearchItem> researchList) {
        this(researchList, false);
    }

    public ResearchAdapter(List<ResearchItem> researchList, boolean isProfileView) {
        this.researchList = researchList;
        this.isProfileView = isProfileView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isProfileView ? R.layout.rc_profile_item_research : R.layout.rc_item_research;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
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
        if (holder.rsTags != null) {
            holder.rsTags.setText(item.getTags());
        }

        // Handle Status for Profile View
        if (isProfileView && holder.rsStatus != null) {
            int status = item.getStatus();
            if (status == DataRepository.STATUS_APPROVED) {
                holder.rsStatus.setText("Approved");
                holder.rsStatus.setTextColor(Color.GREEN);
            } else if (status == DataRepository.STATUS_DECLINED) {
                holder.rsStatus.setText("Declined");
                holder.rsStatus.setTextColor(Color.RED);
            } else {
                holder.rsStatus.setText("Pending");
                holder.rsStatus.setTextColor(Color.YELLOW);
            }
        }

        // Dynamic Star Rating Logic with Decimal Support
        float rating = item.getRating();
        ImageView[] stars = {holder.star1, holder.star2, holder.star3, holder.star4, holder.star5};
        
        for (int i = 0; i < stars.length; i++) {
            if (i < (int) rating) {
                stars[i].setImageResource(R.drawable.ic_star);
                stars[i].setAlpha(1.0f);
            } else if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_half);
                stars[i].setAlpha(1.0f);
            } else {
                stars[i].setImageResource(R.drawable.ic_star);
                stars[i].setAlpha(0.2f);
            }
        }

        holder.btnItem.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), view_research_user.class);
            intent.putExtra("research_data", item); 
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return researchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rsTitle, rsAuthor, rsSchool, rsCourse, rsDate, rsTags, rsStatus;
        public Button btnItem;
        public ImageView star1, star2, star3, star4, star5;

        public ViewHolder(View view) {
            super(view);
            rsTitle = view.findViewById(R.id.tvTitle);
            rsAuthor = view.findViewById(R.id.tvAuthor);
            rsSchool = view.findViewById(R.id.tvSchool);
            rsCourse = view.findViewById(R.id.tvCourse);
            rsDate = view.findViewById(R.id.tvDate);
            rsTags = view.findViewById(R.id.tvTags);
            rsStatus = view.findViewById(R.id.tvStatus); // For profile view status display
            btnItem = view.findViewById(R.id.research_item_btn);
            
            star1 = view.findViewById(R.id.star1);
            star2 = view.findViewById(R.id.star2);
            star3 = view.findViewById(R.id.star3);
            star4 = view.findViewById(R.id.star4);
            star5 = view.findViewById(R.id.star5);
        }
    }
}