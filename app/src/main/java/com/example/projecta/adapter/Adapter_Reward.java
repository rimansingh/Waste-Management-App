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

import com.bumptech.glide.Glide;
import com.example.projecta.R;
import com.example.projecta.homeScreen.rewards.WasteQuestionsActivity;
import com.example.projecta.model.QuizCategory;

import java.util.ArrayList;

public class Adapter_Reward extends RecyclerView.Adapter<Adapter_Reward.ViewHolder> {

    private ArrayList<QuizCategory> quizCategories;
    private Context context;

    public Adapter_Reward(Context context, ArrayList<QuizCategory> quizCategories){
        this.context = context;
        this.quizCategories = quizCategories;
    }

    @NonNull
    @Override
    public Adapter_Reward.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reward_grid_layout, parent,false);
        return new Adapter_Reward.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Reward.ViewHolder holder, int position) {
        QuizCategory model = quizCategories.get(position);
        holder.title.setText(model.getQuizCategoryName());
        Glide.with(context).load(model.getQuizCategoryImage()).into(holder.icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WasteQuestionsActivity.class);
                intent.putExtra("quizCategoryID", model.getQuizCategoryID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView);
            icon = itemView.findViewById(R.id.imageView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    switch (position) {
//                        case 0:
//                            Intent intent = new Intent(context, WasteQuestionsActivity.class);
//                            intent.putExtra("quizCategoryID", position);
//                            context.startActivity(intent);
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            });
        }
    }
}
