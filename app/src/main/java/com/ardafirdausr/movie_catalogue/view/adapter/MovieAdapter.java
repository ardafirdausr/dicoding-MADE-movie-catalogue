package com.ardafirdausr.movie_catalogue.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.api.movie.response.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends BaseAdapter {

    private Context context;
    private List<Movie> movies = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).getId();
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_movie_list, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(itemView);
        final Movie movie = getItem(position);
        viewHolder.bind(movie);
        viewHolder.vgListitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onClick(movie);
            }
        });
        return itemView;
    }

    public interface OnItemClickCallback {
        void onClick(Movie movie);
    }

    private class ViewHolder {
        public ViewGroup vgListitem;
        public TextView tvTitle, tvRating, tvDescription;
        public ImageView ivPoster;

        ViewHolder(View view) {
            vgListitem = view.findViewById(R.id.vg_list_item);
            tvTitle = view.findViewById(R.id.tv_title);
            tvRating = view.findViewById(R.id.tv_rating);
            tvDescription= view.findViewById(R.id.tv_description);
            ivPoster = view.findViewById(R.id.iv_poster);
        }

        void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvRating.setText(movie.getVote() + " / " + 10);
            tvDescription.setText(movie.getDescription());
            Picasso.get().load(movie.getImageUrl()).into(ivPoster);
        }
    }

}

