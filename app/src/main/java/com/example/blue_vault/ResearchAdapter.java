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
        // Decide which layout to use based on where the adapter is called
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

        // Handle Status for Profile View using MySQL Status numbers
        // 1 = Approved, 2 = Published, 0 = Declined, 3 = Pending
        if (isProfileView && holder.rsStatus != null) {
            int status = item.getStatus();
            if (status == 1) { // Approved
                holder.rsStatus.setText("Approved");
                holder.rsStatus.setTextColor(Color.GREEN);
            } else if (status == 0) { // Declined
                holder.rsStatus.setText("Declined");
                holder.rsStatus.setTextColor(Color.RED);
            } else if (status == 2) { // Published
                holder.rsStatus.setText("Published");
                holder.rsStatus.setTextColor(Color.YELLOW);
            }
            else { // 3 or other = Pending
                holder.rsStatus.setText("Pending");
                holder.rsStatus.setTextColor(Color.GRAY);
            }
        }

        // Dynamic Star Rating Logic
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

        // Set the click listener to open the detailed view
        holder.btnItem.setOnClickListener(v -> {
            // Check if the user is an admin or student to decide where to go
            // If isProfileView is true, we go to user view.
            // If you are in admin_pending_reqs, you should use the click listener from that activity.
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
            rsStatus = view.findViewById(R.id.tvStatus);
            btnItem = view.findViewById(R.id.research_item_btn);

            star1 = view.findViewById(R.id.star1);
            star2 = view.findViewById(R.id.star2);
            star3 = view.findViewById(R.id.star3);
            star4 = view.findViewById(R.id.star4);
            star5 = view.findViewById(R.id.star5);
        }
    }
}