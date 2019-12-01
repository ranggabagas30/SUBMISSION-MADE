package com.rangga.submission.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rangga.submission.R;
import com.rangga.submission.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CategoryViewHolder> {

    private final Context context;
    private OnItemClickListener onItemClickListener;
    private ArrayList<Movie> movies;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(ArrayList<Movie> movies) {
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

        Glide.with(context)
                .load(movie.getMovieImageUrl())
                .apply(new RequestOptions())
                .into(categoryViewHolder.imgMoviePoster);

        categoryViewHolder.itemView.setOnClickListener(v -> {
            Movie movieClick = movies.get(categoryViewHolder.getAdapterPosition());
            onItemClickListener.onItemClick(movieClick);
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
        final ImageView imgMoviePoster;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMovieName = itemView.findViewById(R.id.tv_item_name);
            txtMovieDescription = itemView.findViewById(R.id.tv_item_desc);
            txtMovieGenre = itemView.findViewById(R.id.tv_item_genre);
            txtMovieRating = itemView.findViewById(R.id.tv_item_rating);
            imgMoviePoster = itemView.findViewById(R.id.iv_item_photo);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }
}