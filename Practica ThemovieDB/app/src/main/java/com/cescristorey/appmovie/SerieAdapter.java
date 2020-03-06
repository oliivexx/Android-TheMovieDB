package com.cescristorey.appmovie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cescristorey.appmovie.ModeloPelicula.TVShowListed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.moviesViewHolder> {

    public final Context context;
    private ArrayList<TVShowListed> list;
    private SerieAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TVShowListed movie);
    }


    public SerieAdapter(Context context) {
        this.list = new ArrayList<TVShowListed>();
        this.context = context;
        setListener();
    }


    private void setListener () {
        this.listener = new SerieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TVShowListed movie) {
                Intent intent = new Intent(context, Serie_Actividad.class);
                int id = (int) movie.getId();
                intent.putExtra("id", id);
                context.startActivity(intent);
            }

        };
    }


    @Override
    public moviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tvshow_item, parent, false);
        moviesViewHolder tvh = new moviesViewHolder(itemView);
        return tvh;
    }


    @Override
    public void onBindViewHolder(moviesViewHolder holder, int position) {

        final TVShowListed movie = list.get(position);
        holder.bindMovie(movie, listener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class moviesViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        RatingBar ratingBar;
        ImageView image;


        public moviesViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            image = (ImageView) itemView.findViewById(R.id.imageView2);

        }


        public void bindMovie(final TVShowListed movie, final SerieAdapter.OnItemClickListener listener) {
            tvName.setText(movie.getName());
            float puntuacion = (movie.getVote_average()) /2;
            ratingBar.setNumStars(5);
            ratingBar.setRating(puntuacion);
            Picasso.get().load("http://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }


    public void swap(List<TVShowListed> newListMovies) {
        list.clear();
        list.addAll(newListMovies);
        notifyDataSetChanged();
    }
}