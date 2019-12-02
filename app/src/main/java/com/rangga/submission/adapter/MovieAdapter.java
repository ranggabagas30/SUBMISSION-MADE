package com.rangga.submission.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rangga.submission.R;
import com.rangga.submission.data.database.entity.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CategoryViewHolder> {

    private Context context;
    private boolean isOnFavorite = false;
    private OnItemClickListener onItemClickListener;
    private OnLongItemClickListener onLongItemClickListener;
    private OnFavoriteClickListener onFavoriteClickListener;
    private List<Movie> movies;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public MovieAdapter(Context context, boolean isOnFavorite) {
        this.context = context;
        this.isOnFavorite = isOnFavorite;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        this.onLongItemClickListener = onLongItemClickListener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener onFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener;
    }

    public void setData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, final int position) {
        Movie movie = movies.get(position);
        categoryViewHolder.txtMovieName.setText(movie.getMovieName());
        categoryViewHolder.txtMovieDescription.setText(movie.getMovieDescription());
        categoryViewHolder.txtMovieGenre.setText(movie.getMovieGenre());
        categoryViewHolder.txtMovieRating.setText(movie.getMovieRating());

        if (isOnFavorite) {
            categoryViewHolder.ibFavorite.setImageDrawable(context.getDrawable(R.drawable.ic_close_24dp));
        } else {
            if (movie.isFavorite()) categoryViewHolder.ibFavorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_24dp));
            else categoryViewHolder.ibFavorite.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_border_24dp));
        }

        Glide.with(context)
                .load(movie.getMovieImageUrl())
                .apply(new RequestOptions())
                .into(categoryViewHolder.imgMoviePoster);

        categoryViewHolder.itemView.setOnClickListener(v -> {
            Movie movieClick = movies.get(categoryViewHolder.getAdapterPosition());
            if (onItemClickListener != null) onItemClickListener.onItemClick(movieClick);
        });

        categoryViewHolder.itemView.setOnLongClickListener(v -> {
            Movie movieClick = movies.get(categoryViewHolder.getAdapterPosition());
            if (onLongItemClickListener != null) onLongItemClickListener.onLongItemClickListener(movieClick);
            return true;
        });

        categoryViewHolder.ibFavorite.setOnClickListener(v -> {
            Movie movieClick = movies.get(categoryViewHolder.getAdapterPosition());
            if (onFavoriteClickListener != null) onFavoriteClickListener.onFavoriteClickListener(movieClick);
        });
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        return movies.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        final TextView txtMovieName;
        final TextView txtMovieDescription;
        final TextView txtMovieGenre;
        final TextView txtMovieRating;
        final ImageButton ibFavorite;
        final ImageView imgMoviePoster;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMovieName = itemView.findViewById(R.id.tv_item_name);
            txtMovieDescription = itemView.findViewById(R.id.tv_item_desc);
            txtMovieGenre = itemView.findViewById(R.id.tv_item_genre);
            txtMovieRating = itemView.findViewById(R.id.tv_item_rating);
            ibFavorite = itemView.findViewById(R.id.ib_item_favorite);
            imgMoviePoster = itemView.findViewById(R.id.iv_item_photo);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public interface OnLongItemClickListener {
        void onLongItemClickListener(Movie movie);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClickListener(Movie movie);
    }
}