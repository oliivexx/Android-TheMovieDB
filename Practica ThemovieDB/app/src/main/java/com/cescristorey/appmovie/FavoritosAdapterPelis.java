package com.cescristorey.appmovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cescristorey.appmovie.ModeloPelicula.MovieDetail;
import com.cescristorey.appmovie.ModeloPelicula.MovieListed;
import com.cescristorey.appmovie.ModeloPelicula.VideoDetail;
import com.squareup.picasso.Picasso;


public class FavoritosAdapterPelis extends RecyclerView.Adapter<FavoritosAdapterPelis.moviesViewHolder> {

    public final Context context;
    private Cursor list;
    private OnItemClickListener listener;
    public MovieDetail movierecycle = new MovieDetail();



    public interface OnItemClickListener {
        void onItemClick(MovieDetail movie);
    }


    public FavoritosAdapterPelis(Context context, Cursor cursor) {
        this.list = cursor;
        this.context = context;
        setListener();
    }


    private void setListener () {
        this.listener = new OnItemClickListener() {
            @Override
            public void onItemClick(MovieDetail movie) {
                Intent intent = new Intent(context, Pelicula_Actividad.class);
                int id = (int) movie.getId();
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        };
    }


    @Override
    public moviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favoritos_item, parent, false);
        moviesViewHolder tvh = new moviesViewHolder(itemView);
        return tvh;
    }


    @Override
    public void onBindViewHolder(moviesViewHolder holder, int position) {
        String name;
        String photo;
        float id = Float.parseFloat(list.getString(0));
        name = list.getString(1);
        photo= list.getString(2);
        movierecycle.setId(id);
        holder.bindMovie(name, photo,movierecycle, listener);
        list.moveToNext();

    }


    @Override
    public int getItemCount() {
        return list.getCount();
    }


    public class moviesViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView imageView;


        public moviesViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            imageView=(ImageView) itemView.findViewById(R.id.imageView2);

        }


        public void bindMovie(String nombre, String foto, final MovieDetail id, final OnItemClickListener listener) {
            tvName.setText(nombre);
            Picasso.get().load("https://image.tmdb.org/t/p/w500"+foto).into(imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(id);
                }
            });

        }
    }


    public void swap(Cursor cursor) {
        list.equals(cursor);
        notifyDataSetChanged();
    }
}