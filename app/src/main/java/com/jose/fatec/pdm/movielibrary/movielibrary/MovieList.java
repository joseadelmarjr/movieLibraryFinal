package com.jose.fatec.pdm.movielibrary.movielibrary;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLES31Ext;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieList extends Activity {

    private List<String> testList;
    private ListView MovieListView;
    private ArrayAdapter moviesAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list);
        //Pega a intent de outra activity
        Intent it = getIntent();

        //Recuperei a string da outra activity
        genre genre = (com.jose.fatec.pdm.movielibrary.movielibrary.genre) getIntent().getSerializableExtra("genre");
        testList = new ArrayList<>();
        MovieListView = findViewById(R.id.movielist);
        moviesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,  testList);
        MovieListView.setAdapter(moviesAdapter);
        String sql = "https://api.themoviedb.org/3/discover/movie?api_key=e4f84def55041ae2e243efe2a0bc56c9&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=false&page=1&with_genres=";
        sql = sql + String.valueOf(genre.getId());
        new getGenres().execute(sql);

    }
    private class getGenres extends AsyncTask<String,Void,String> {


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                StringBuilder stringBuilder = new StringBuilder("");
                try (BufferedReader reader  = new BufferedReader(inputStreamReader)) {
                    String linha = null;
                    String json = "";
                    while ((linha = reader.readLine()) != null)
                        stringBuilder.append(linha);
                }
                connection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute (String jsonS){
            //Toast.makeText(MainActivity.this, dados, Toast.LENGTH_SHORT).show();
            try {
                JSONObject json = new JSONObject(jsonS);
                JSONArray list = json.getJSONArray("results");

                for (int i = 0; i < list.length(); i++){
                    JSONObject filmes = list.getJSONObject(i);
                    int     id       = filmes.getInt("id");
                    String descricao = filmes.getString("title");
                    //Toast.makeText(MainActivity.this, descricao, Toast.LENGTH_SHORT).show();
                    genre novo = new genre();
                    novo.setId(id);
                    novo.setDescription(descricao);
                 //   movieList.add(novo);
                    testList.add(descricao);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
