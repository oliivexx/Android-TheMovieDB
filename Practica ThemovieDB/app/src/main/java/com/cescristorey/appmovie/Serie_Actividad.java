package com.cescristorey.appmovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cescristorey.appmovie.ModeloPelicula.CreditsFeed;
import com.cescristorey.appmovie.ModeloPelicula.MovieDetail;
import com.cescristorey.appmovie.ModeloPelicula.Result;
import com.cescristorey.appmovie.ModeloPelicula.TVShowDetail;
import com.cescristorey.appmovie.ModeloPelicula.VideoDetail;
import com.cescristorey.appmovie.ModeloPelicula.VideoDetailTV;
import com.cescristorey.appmovie.retrofit.MovieService;
import com.cescristorey.appmovie.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.hanks.library.bang.SmallBangView;

public class Serie_Actividad extends AppCompatActivity {

    ImageView image;
    TextView estudio, genero, releasedate, numero_episiodios, numero_temporadas;
    TextView titulo,descripcion, texto_estudio,texto_genero, texto_releasedate,texto_numero_temporadas, texto_numero_episodios;
    RatingBar ratingBar;
    RecyclerView recyclerView;
    CastAdapter castAdapter;
    Button botontrailer;
    Result resultadofinal = new Result();
    List<Result> resultados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        int id =  intent.getIntExtra("id", 0);
        loadSearch(id);




    }
    public void loadTrailer(int movieid){

        final MovieService[] getTrailer = {RetrofitInstance.getRetrofitInstance().create(MovieService.class)};
        Call<VideoDetailTV> call = getTrailer[0].getTrailerShow(movieid, RetrofitInstance.getApiKey(), "en-US");
        call.enqueue(new Callback<VideoDetailTV>() {
            @Override

            public void onResponse(Call<VideoDetailTV> call, Response<VideoDetailTV> response) {
                Log.i("LLAMADA TRAILER", String.valueOf(response));
                switch (response.code()) {
                    case 200:
                        VideoDetailTV data = response.body();
                        for(Result a : data.getResults()){

                            resultados.add(a);

                        }
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<VideoDetailTV> call, Throwable t) {
            }
        });
    }
    public void loadPelicula(TVShowDetail movie){

        final String id_db = String.valueOf((int)movie.getId());
        final String titulo_db = movie.getName();
        final String imagen_db = movie.getPoster_path();
        image = findViewById(R.id.image_pelicula);
        titulo = findViewById(R.id.titulo_pelicula);
        ratingBar = findViewById(R.id.ratingBar);
        descripcion = findViewById(R.id.descripcion);
        botontrailer = findViewById(R.id.boton_trailer);
        Picasso.get().load("http://image.tmdb.org/t/p/w500" + movie.getPoster_path()).resize(412, 320).into(image);
        titulo.setText(movie.getName());
        float puntuacion = (movie.getVote_average()) /2;
        ratingBar.setNumStars(5);
        ratingBar.setRating(puntuacion);
        descripcion.setText(movie.getOverview());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_actores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        castAdapter = new CastAdapter(this);
        recyclerView.setAdapter(castAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int id_cast = (int) movie.getId();
        loadCast(id_cast);


        estudio = findViewById(R.id.estudio);
        genero = findViewById(R.id.genero);
        releasedate = findViewById(R.id.releasedate);
        texto_estudio = findViewById(R.id.texto_estudio);
        texto_genero = findViewById(R.id.texto_genero);
        texto_releasedate = findViewById(R.id.texto_releasedate);
        numero_temporadas = findViewById(R.id.numero_temporadas);
        numero_episiodios = findViewById(R.id.numero_capitulos);
        texto_numero_temporadas = findViewById(R.id.texto_temporadas);
        texto_numero_episodios = findViewById(R.id.texto_capitulos);
        if(resultados!= null){
            for(int i = 0 ; i < resultados.size(); i++){
                if(resultados.get(i).getSite().equals("YouTube")){
                    resultadofinal = resultados.get(i);
                }
            }
        }
        botontrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeVideoId =  "https://www.youtube.com/watch?v=" + String.valueOf(resultadofinal.getKey()); //Id video.
                Uri uri = Uri.parse(youtubeVideoId);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        ArrayList<Object> generos;
        ArrayList<Object> productores;
        productores = movie.getProduction_companies();
        generos = movie.getGenres();
        int start = 0;
        String cadena1="";
        String cadena2="";
        Object aux;

        for(int i=0; i < productores.size() ;i++){
            aux = productores.get(i);
            if(i!=productores.size()-1) {
                cadena2 = aux.toString();
                start = cadena2.indexOf("name=")+5;
                cadena1 += cadena2.substring(start, cadena2.indexOf(",",start)) + " , ";

            }
            else{
                cadena2 = aux.toString();
                start = cadena2.indexOf("name=") + 5;
                cadena1 += cadena2.substring(start, cadena2.indexOf(",",start));
            }
        }
        texto_estudio.setText(cadena1);
        cadena2="";
        cadena1 = "";


        for(int i=0; i < generos.size() ;i++){
            aux = generos.get(i);
            if(i!=generos.size()-1) {
                cadena2 = aux.toString();
                start = cadena2.indexOf("n")+5;
                cadena1 += cadena2.substring(start, cadena2.length()-1) + " , ";
            }
            else{
                cadena2 = aux.toString();
                start = cadena2.indexOf("n") + 5;
                cadena1 += cadena2.substring(start, cadena2.length()-1);
            }
        }
        texto_genero.setText(cadena1);
        texto_releasedate.setText(movie.getLast_air_date());
        texto_numero_temporadas.setText(String.valueOf((int)movie.getNumber_of_seasons()));
        texto_numero_episodios.setText(String.valueOf((int)movie.getNumber_of_episodes()));

        final SmallBangView like_heart = findViewById(R.id.like_heart);

        FavoritosSQLiteHelper favoritosSQLiteHelper = new FavoritosSQLiteHelper(getApplicationContext(), "DBFavoritos", null, 1);
        SQLiteDatabase db = favoritosSQLiteHelper.getReadableDatabase();


        Cursor c = db.rawQuery("SELECT * FROM Favoritos WHERE codigo='"+id_db+"' AND es_pelicula='0'",null);
        if (c.moveToFirst()) {
            like_heart.setSelected(true);
        }
        db.close();
        like_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like_heart.isSelected()) {
                    like_heart.likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
                    like_heart.setSelected(false);

                    FavoritosSQLiteHelper usdbh =
                            new FavoritosSQLiteHelper(getApplicationContext(), "DBFavoritos", null, 1);
                    SQLiteDatabase db = usdbh.getWritableDatabase();
                    db.execSQL("DELETE FROM Favoritos WHERE codigo='"+id_db+"' AND es_pelicula= '0'");
                    db.close();

                } else {
                    like_heart.setSelected(true);
                    like_heart.likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
                    FavoritosSQLiteHelper usdbh =
                            new FavoritosSQLiteHelper(getApplicationContext(), "DBFavoritos", null, 1);
                    SQLiteDatabase db = usdbh.getWritableDatabase();
                    if(db != null) {
                        db.execSQL("INSERT INTO Favoritos (codigo, nombre, foto, es_pelicula) VALUES ('" + id_db + "', '" + titulo_db + "', '" + imagen_db + "', '0')");
                        db.close();
                    }
                }
            }
        });




    }

    public void loadSearch(int id) {
        MovieService getMovie = RetrofitInstance.getRetrofitInstance().create(MovieService.class);
        Call<TVShowDetail> call = getMovie.getTvShow(id, RetrofitInstance.getApiKey(), "es");
        call.enqueue(new Callback<TVShowDetail>() {
            @Override
            public void onResponse(Call<TVShowDetail> call, Response<TVShowDetail> response) {
                switch (response.code()) {
                    case 200:
                        Log.i("Entra", "hello");
                        TVShowDetail data = response.body();
                        loadPelicula(data);
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<TVShowDetail> call, Throwable t) {
            }
        });
    }
    public void loadCast (int id) {
        MovieService getCast = RetrofitInstance.getRetrofitInstance().create(MovieService.class);
        Call<CreditsFeed> call = getCast.getCastTvShow(id,RetrofitInstance.getApiKey(), "es");
        call.enqueue(new Callback<CreditsFeed>() {
            @Override
            public void onResponse(Call<CreditsFeed> call, Response<CreditsFeed> response) {
                switch (response.code()) {
                    case 200:
                        CreditsFeed data = response.body();
                        castAdapter.swap(data.getCast());
                        castAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<CreditsFeed> call, Throwable t) {
            }
        });
    }
}
