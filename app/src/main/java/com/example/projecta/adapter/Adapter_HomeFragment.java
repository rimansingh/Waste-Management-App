package com.example.projecta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecta.R;
import com.example.projecta.homeScreen.Complaints;
import com.example.projecta.homeScreen.Feedback;
import com.example.projecta.homeScreen.Reward;
import com.example.projecta.homeScreen.WasteBinLocator;
import com.example.projecta.homeScreen.WasteReductionTips;

import java.util.List;

public class Adapter_HomeFragment extends RecyclerView.Adapter<Adapter_HomeFragment.ViewHolder> {

    List<String> titles;
    List<Integer> images;
    private Context context;
    private String TAG;

    public Adapter_HomeFragment(Context context, List<Integer> images, List<String> titles){
        this.context = context;
        this.images = images;
        this.titles = titles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_grid_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.gridIcon.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView gridIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.grid_text);
            gridIcon = itemView.findViewById(R.id.grid_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    switch (position){
                        case 0:
                            Intent intent = new Intent(context, WasteBinLocator.class);
                            context.startActivity(intent);
                            break;
                        case 1:
                            Intent intent1 = new Intent(context, WasteReductionTips.class);
                            context.startActivity(intent1);
                            break;
                        case 2:
                            Intent intent3 = new Intent(context, Complaints.class);
                            context.startActivity(intent3);
                            break;
                        case 3:
                            Intent intent4 = new Intent(context, Feedback.class);
                            context.startActivity(intent4);
                            break;
                        case 4:
                            Intent intent2 = new Intent(context, Reward.class);
                            context.startActivity(intent2);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}
