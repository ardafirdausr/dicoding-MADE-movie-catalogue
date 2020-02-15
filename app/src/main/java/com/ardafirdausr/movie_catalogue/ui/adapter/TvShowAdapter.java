package com.ardafirdausr.movie_catalogue.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {

    private List<TvShow> tvShows = new ArrayList<>();
    private TvShowAdapter.OnItemClickCallback onItemClickCallback;

    public void setTvShows(List<TvShow> tvShows) {
        this.tvShows = tvShows;
    }

    public void setOnItemClickCallback(TvShowAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public TvShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_show_list, parent, false);
        return new TvShowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowAdapter.ViewHolder holder, int position) {
        final TvShow movie = tvShows.get(position);
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
        return tvShows.size();
    }

    public interface OnItemClickCallback {
        void onClick(View view, TvShow movie);
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

        void bind(TvShow tvShowResponse) {
            tvTitle.setText(tvShowResponse.getTitle());
            tvRating.setText(Double.toString(tvShowResponse.getVote()));
            tvDescription.setText(tvShowResponse.getDescription());
            tvReleaseDate.setText(tvShowResponse.getFirstAirDate());
            Picasso.get()
                    .load(tvShowResponse.getImageUrl())
                    .resize(90, 120)
                    .transform(new RoundedCornersTransformation(10, 0))
                    .into(ivPoster);
        }

    }

}
