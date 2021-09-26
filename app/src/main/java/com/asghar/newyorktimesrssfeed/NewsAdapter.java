package com.asghar.newyorktimesrssfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    List<News> newsList;
    private NewsClickListener newsClickListener;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final String TAG = "NewsViewHolder";
        public TextView titleTextView;
        public TextView descTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title_news);
            descTextView = itemView.findViewById(R.id.desc_news);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(newsClickListener != null){
                newsClickListener.onNewsClick(view, getAdapterPosition());
            }        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View newsView = inflater.inflate(R.layout.single_news, parent, false);
        return new NewsViewHolder(newsView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.descTextView.setText(news.getDesc());
        holder.titleTextView.setText(news.getTitle());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateDataset(List<News> news){
        this.newsList = news;
        notifyDataSetChanged();
    }

    void setNewsClickListener(NewsClickListener newsClickListener){
        this.newsClickListener = newsClickListener;
    }

    News getItem(int position){
        return newsList.get(position);
    }

    public interface NewsClickListener{
        void onNewsClick(View view, int position);
    }
}
