package com.cescristorey.appmovie.ui.favoritos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cescristorey.appmovie.FavoritosAdapterPelis;
import com.cescristorey.appmovie.FavoritosAdapterSeries;
import com.cescristorey.appmovie.FavoritosSQLiteHelper;
import com.cescristorey.appmovie.R;

public class FavoritosFragment extends Fragment {

    RecyclerView recyclerView,recyclerView2;
    FavoritosAdapterPelis favoritosAdapterPelis;
    FavoritosAdapterSeries favoritosAdapterPelis2;

    TextView textViewFavoritos;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_favoritos, container, false);


        FavoritosSQLiteHelper favoritosSQLiteHelper = new FavoritosSQLiteHelper(vista.getContext(), "DBFavoritos", null, 1);
        SQLiteDatabase db = favoritosSQLiteHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Favoritos WHERE es_pelicula='1'",null);
        if (c.moveToFirst()) {

            recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view_favoritos);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(vista.getContext(), LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            favoritosAdapterPelis = new FavoritosAdapterPelis(vista.getContext(), c);
            recyclerView.setAdapter(favoritosAdapterPelis);
            favoritosAdapterPelis.swap(c);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }
        Cursor a = db.rawQuery("SELECT * FROM Favoritos WHERE es_pelicula='0'",null);
        if (a.moveToFirst()) {

            recyclerView2 = (RecyclerView) vista.findViewById(R.id.recycler_view_favoritos2);
            recyclerView2.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(vista.getContext(), LinearLayoutManager.HORIZONTAL,false);
            recyclerView2.setLayoutManager(layoutManager);
            recyclerView2.setHasFixedSize(true);
            favoritosAdapterPelis2 = new FavoritosAdapterSeries(vista.getContext(), a);
            recyclerView2.setAdapter(favoritosAdapterPelis2);
            favoritosAdapterPelis2.swap(c);
            recyclerView2.setItemAnimator(new DefaultItemAnimator());

        }

        db.close();


        return vista;
    }
}