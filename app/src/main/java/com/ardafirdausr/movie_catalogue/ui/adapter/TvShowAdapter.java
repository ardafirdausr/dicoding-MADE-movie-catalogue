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
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> implements Filterable {

    private List<TvShow> immutableTvShows;
    private List<TvShow> tvShows;
    private TvShowAdapter.OnItemClickCallback onItemClickCallback;

    public TvShowAdapter(){
        immutableTvShows = new ArrayList<>();
        tvShows = new ArrayList<>();
    }

    public void setTvShows(List<TvShow> tvShows) {
        if(this.tvShows != null) {
            this.tvShows.clear();
            this.tvShows.addAll(tvShows);
        }
        if(this.immutableTvShows != null){
            this.immutableTvShows.clear();
            this.immutableTvShows.addAll(tvShows);
        }

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
        final TvShow tvShow = tvShows.get(position);
        holder.bind(tvShow);
        holder.vgListitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onClick(view, tvShow);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    @Override
    public Filter getFilter() {
        return filteredTvShows;
    }

    private Filter filteredTvShows = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TvShow> filtered = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filtered.addAll(immutableTvShows);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(TvShow tvShow: immutableTvShows){
                    if(tvShow.getTitle().toLowerCase().contains(filterPattern)){
                        filtered.add(tvShow);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tvShows.clear();
            tvShows.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickCallback {
        void onClick(View view, TvShow tvShow);
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
                    .fit()
                    .centerCrop()
                    .transform(new RoundedCornersTransformation(10, 0))
                    .into(ivPoster);
        }

    }

}
