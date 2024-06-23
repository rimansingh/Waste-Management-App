package com.example.projecta.fragments.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecta.R;
import com.example.projecta.model.FirebaseDatabase;

import java.util.List;

public class leaderboardAdapter extends RecyclerView.Adapter<leaderboardAdapter.ViewHolderAdapter> {

    List<FirebaseDatabase> list;
    Context context;

    public leaderboardAdapter(List<FirebaseDatabase> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_layout, parent, false);
        return new ViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapter holder, int position) {
        FirebaseDatabase currentItem = list.get(position);
        holder.firstName.setText(currentItem.getFirstName());
        holder.lastName.setText(currentItem.getLastName());
        holder.points.setText(String.valueOf(currentItem.getEarnedPoints()));
        holder.rank.setText(String.valueOf(list.size()-position));
        Glide.with(context).load(currentItem.getImgProfile()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderAdapter extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView rank, firstName, lastName, points;

        public ViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.leaderBoardUserImage);
            rank = itemView.findViewById(R.id.leaderBoardRanking);
            firstName = itemView.findViewById(R.id.leaderBoardFirstName);
            lastName = itemView.findViewById(R.id.leaderBoardLastName);
            points = itemView.findViewById(R.id.leaderBoardPoints);
        }
    }
}
