package com.oliviabecht.obechtnewsaggreg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends
        RecyclerView.Adapter<NewsViewHolder> {

    private final MainActivity mainActivity;
    private final ArrayList<News> newsList;


    private Context context;
    private Picasso picasso;



    public NewsAdapter(MainActivity mainActivity, ArrayList<News> newsList) {
        this.mainActivity = mainActivity;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.view_pager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);

       // final int resourceId = mainActivity.getResources().
              //  getIdentifier(news.getTitle(), "drawable", mainActivity.getPackageName());

        holder.articleTitle.setText(news.getTitle());
        holder.articleDate.setText(news.getPublishedAt());

        //use URL TO LOAD
        String imageURL = news.getUrlToImage();

        System.out.println(imageURL);
        //holder.articleImage.setImageURI(Uri.parse(imageURL));
        //holder.articleImage.setImageResource(resourceId);

        picasso = Picasso.get();
        picasso.load(Uri.parse(imageURL)).fit().centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.drawable.brokenimage)
                .into(holder.articleImage);


        holder.articleBody.setText(news.getBody());
        holder.counter.setText(
                MessageFormat.format("{0} of {1}", position+1, getItemCount()));


        holder.articleTitle.setOnClickListener(view -> {
            context = view.getContext();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(news.getUrl()));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(browserIntent);
        });

        holder.articleBody.setOnClickListener(view -> {
            context = view.getContext();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(news.getUrl()));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(browserIntent);
        });

        holder.articleImage.setOnClickListener(view -> {
            context = view.getContext();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(news.getUrl()));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(browserIntent);
        });



    }





    @Override
    public int getItemCount() {
        return newsList.size();
    }

}
