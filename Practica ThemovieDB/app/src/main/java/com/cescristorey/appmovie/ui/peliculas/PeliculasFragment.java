package com.cescristorey.appmovie.ui.peliculas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cescristorey.appmovie.MainActivity;
import com.cescristorey.appmovie.ModeloPelicula.MovieFeed;
import com.cescristorey.appmovie.MovieAdapter;
import com.cescristorey.appmovie.R;
import com.cescristorey.appmovie.retrofit.MovieService;
import com.cescristorey.appmovie.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeliculasFragment extends Fragment {

    RecyclerView recyclerView,recyclerView2;
    MovieAdapter movieAdapter,movieAdapter2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View vista = inflater.inflate(R.layout.fragment_peliculas, container, false);

        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(vista.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        movieAdapter = new MovieAdapter(vista.getContext());
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadSearch();

        recyclerView2 = (RecyclerView) vista.findViewById(R.id.recycler_view2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(vista.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setHasFixedSize(true);
        recyclerView2.addItemDecoration(new DividerItemDecoration(recyclerView2.getContext(), DividerItemDecoration.VERTICAL));
        movieAdapter2 = new MovieAdapter(vista.getContext());
        recyclerView2.setAdapter(movieAdapter2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        loadSearch2();


        return vista;
    }
    public void loadSearch () {
        MovieService getMovie = RetrofitInstance.getRetrofitInstance().create(MovieService.class);
        Call<MovieFeed> call = getMovie.getTopRated(RetrofitInstance.getApiKey(), "es");
        call.enqueue(new Callback<MovieFeed>() {
            @Override
            public void onResponse(Call<MovieFeed> call, Response<MovieFeed> response) {
                switch (response.code()) {
                    case 200:
                        MovieFeed data = response.body();
                        movieAdapter.swap(data.getResults());
                        movieAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<MovieFeed> call, Throwable t) {
            }
        });
    }
    public void loadSearch2 () {
        MovieService getMovie = RetrofitInstance.getRetrofitInstance().create(MovieService.class);
        Call<MovieFeed> call = getMovie.getMovieLatest(RetrofitInstance.getApiKey(), "es");
        call.enqueue(new Callback<MovieFeed>() {
            @Override
            public void onResponse(Call<MovieFeed> call, Response<MovieFeed> response) {
                switch (response.code()) {
                    case 200:
                        Log.i("Entra","hello");
                        MovieFeed data = response.body();
                        movieAdapter2.swap(data.getResults());
                        movieAdapter2.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<MovieFeed> call, Throwable t) {
            }
        });
    }
}