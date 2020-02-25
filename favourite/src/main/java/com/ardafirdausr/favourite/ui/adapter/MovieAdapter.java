package com.ardafirdausr.favourite.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ardafirdausr.favourite.R;
import com.ardafirdausr.favourite.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private List<Movie> movies;
    private OnItemClickCallback onItemClickCallback;

    public MovieAdapter() {
        this.movies = new ArrayList<>();
    }

    public void setMovies(List<Movie> movies) {
        if(this.movies != null) {
            this.movies.clear();
            this.movies.addAll(movies);
        }
    }


    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movieResponse = movies.get(position);
        holder.bind(movieResponse);
        holder.vgListitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onClick(view, movieResponse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickCallback {
        void onClick(View view, Movie movieResponse);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup vgListitem;
        private TextView tvTitle, tvRating, tvDescription, tvReleaseDate;
        private ImageView ivPoster;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            vgListitem = itemView.findViewById(R.id.vg_list_item);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
            tvDescription= itemView.findViewById(R.id.tv_description);
            ivPoster = itemView.findViewById(R.id.iv_poster);
        }

        void bind(Movie movieResponse) {
            tvTitle.setText(movieResponse.getTitle());
            tvRating.setText(Double.toString(movieResponse.getVote()));
            tvDescription.setText(movieResponse.getDescription());
            tvReleaseDate.setText(movieResponse.getReleaseDate());
            Picasso.get()
                    .load(movieResponse.getImageUrl())
                    .fit()
                    .centerCrop()
                    .transform(new RoundedCornersTransformation(10, 0))
                    .into(ivPoster);
        }

    }
}
