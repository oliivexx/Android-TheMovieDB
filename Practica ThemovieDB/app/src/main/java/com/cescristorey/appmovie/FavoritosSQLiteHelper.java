package com.cescristorey.appmovie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritosSQLiteHelper extends SQLiteOpenHelper {

        String sqlCreate = "CREATE TABLE Favoritos (codigo TEXT, nombre TEXT, foto TEXT, es_pelicula TEXT)";

        public FavoritosSQLiteHelper(Context contexto, String nombre,
                                     CursorFactory factory, int version) {
            super(contexto, nombre, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sqlCreate);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        }
}
