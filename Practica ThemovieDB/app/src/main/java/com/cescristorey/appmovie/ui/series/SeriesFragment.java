package com.cescristorey.appmovie.ui.series;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cescristorey.appmovie.ModeloPelicula.MovieFeed;
import com.cescristorey.appmovie.ModeloPelicula.TVShowFeed;
import com.cescristorey.appmovie.R;
import com.cescristorey.appmovie.SerieAdapter;
import com.cescristorey.appmovie.retrofit.MovieService;
import com.cescristorey.appmovie.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeriesFragment extends Fragment {


        RecyclerView recyclerView,recyclerView2;
        SerieAdapter serieAdapter,serieAdapter2;

        public View onCreateView(@NonNull LayoutInflater inflater,
                ViewGroup container, Bundle savedInstanceState) {


            View vista = inflater.inflate(R.layout.fragment_peliculas, container, false);


            recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(vista.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            serieAdapter = new SerieAdapter(vista.getContext());
            recyclerView.setAdapter(serieAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            loadSearch();


            recyclerView2 = (RecyclerView) vista.findViewById(R.id.recycler_view2);
            recyclerView2.setLayoutManager(new LinearLayoutManager(vista.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView2.setHasFixedSize(true);
            recyclerView2.addItemDecoration(new DividerItemDecoration(recyclerView2.getContext(), DividerItemDecoration.VERTICAL));
            serieAdapter2 = new SerieAdapter(vista.getContext());
            recyclerView2.setAdapter(serieAdapter2);
            recyclerView2.setItemAnimator(new DefaultItemAnimator());
            loadSearch2();



            return vista;
        }
        public void loadSearch () {
            MovieService getSerie = RetrofitInstance.getRetrofitInstance().create(MovieService.class);
            Call<TVShowFeed> call = getSerie.getTopRatedTvShow(RetrofitInstance.getApiKey(), "es");
            call.enqueue(new Callback<TVShowFeed>() {
                @Override
                public void onResponse(Call<TVShowFeed> call, Response<TVShowFeed> response) {
                    switch (response.code()) {
                        case 200:
                            TVShowFeed data = response.body();
                            serieAdapter.swap(data.getResults());
                            serieAdapter.notifyDataSetChanged();
                            break;
                        case 401:
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFailure(Call<TVShowFeed> call, Throwable t) {
                }
            });
        }
    public void loadSearch2 () {
        MovieService getSerie = RetrofitInstance.getRetrofitInstance().create(MovieService.class);
        Call<TVShowFeed> call = getSerie.getTvShowLatest(RetrofitInstance.getApiKey(), "es");
        call.enqueue(new Callback<TVShowFeed>() {
            @Override
            public void onResponse(Call<TVShowFeed> call, Response<TVShowFeed> response) {
                switch (response.code()) {
                    case 200:
                        TVShowFeed data = response.body();
                        serieAdapter2.swap(data.getResults());
                        serieAdapter2.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<TVShowFeed> call, Throwable t) {
            }
        });
    }

}
