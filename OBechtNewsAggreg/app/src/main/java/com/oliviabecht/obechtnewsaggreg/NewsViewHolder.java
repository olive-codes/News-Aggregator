package com.oliviabecht.obechtnewsaggreg;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    TextView articleTitle;

    TextView articleDate;

    ImageView articleImage;
    TextView articleBody;
    TextView counter;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        articleTitle = itemView.findViewById(R.id.articleTitle);

        articleDate = itemView.findViewById(R.id.articleDate);

        articleImage = itemView.findViewById(R.id.articleImage);
        articleBody = itemView.findViewById(R.id.articleBody);
        counter = itemView.findViewById(R.id.countText);
    }

}
