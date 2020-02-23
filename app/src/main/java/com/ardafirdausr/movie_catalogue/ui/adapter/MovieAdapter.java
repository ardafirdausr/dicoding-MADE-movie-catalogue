package com.ardafirdausr.movie_catalogue.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements Filterable {

    private List<Movie> immutableMovies;
    private List<Movie> movies;
    private OnItemClickCallback onItemClickCallback;

    public MovieAdapter() {
        this.movies = new ArrayList<>();
        this.immutableMovies = new ArrayList<>();
    }

    public void setMovie(List<Movie> movies) {
        if(this.movies != null) this.movies.clear();
        this.movies.addAll(movies);
        if(this.immutableMovies != null) this.immutableMovies.clear();
        this.immutableMovies.addAll(movies);
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

    @Override
    public Filter getFilter() {
        return filteredMovies;
    }

    private Filter filteredMovies = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Movie> filtered = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filtered.addAll(immutableMovies);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Movie movie: immutableMovies){
                     if(movie.getTitle().toLowerCase().contains(filterPattern)){
                         filtered.add(movie);
                     }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movies.clear();
            movies.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };

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
