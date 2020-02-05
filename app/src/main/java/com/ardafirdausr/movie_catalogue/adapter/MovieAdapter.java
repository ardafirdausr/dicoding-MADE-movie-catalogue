package com.ardafirdausr.movie_catalogue.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.api.movie.response.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.bind(movie);
        holder.vgListitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onClick(view, movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickCallback {
        void onClick(View view, Movie movie);
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

        void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvRating.setText(Double.toString(movie.getVote()));
            tvDescription.setText(movie.getDescription());
            tvReleaseDate.setText(movie.getReleaseDate());
            Picasso.get()
                    .load(movie.getImageUrl())
                    .resize(90, 120)
                    .transform(new RoundedCornersTransformation(10, 0))
                    .into(ivPoster);
        }

    }

}
