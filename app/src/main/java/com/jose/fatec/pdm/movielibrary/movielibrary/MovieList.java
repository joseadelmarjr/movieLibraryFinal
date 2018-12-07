package com.jose.fatec.pdm.movielibrary.movielibrary;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLES31Ext;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Date;
import java.util.List;

public class MovieList extends Activity {

    private List<String> testList;
    private ListView MovieListView;
    private ArrayAdapter moviesAdapter;
    private List<Movie> movieList;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_list);

        genre genre = (com.jose.fatec.pdm.movielibrary.movielibrary.genre) getIntent().getSerializableExtra("genre");
        testList = new ArrayList<>();

        String urlMovieList = getString(R.string.url_movie_list);
        String apiKey = getString(R.string.api_key);
        String language = getString(R.string.language);
        String urlMontada = urlMovieList + String.valueOf(genre.getId())+ "&language=" + language
                +"&api_key="+apiKey;

        new getGenres().execute(urlMontada);



        //Recuperei o gênero da outra activity
        MovieListView = findViewById(R.id.movielist);
        // Toast.makeText(MovieList.this, "Nulo", Toast.LENGTH_SHORT).show();
        //Montei o adapter mas só faço o set no postExecute
        movieList = new ArrayList<>();
        moviesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,  testList);
        MovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it;
                it = new Intent(MovieList.this, MovieActivity.class);
                Movie choiceMovie = new Movie();
                choiceMovie= movieList.get(position);
                it.putExtra("movie",choiceMovie);
                startActivity(it);

            }
        });





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
                    int     vote_count = filmes.getInt("vote_count");
                    int     id         = filmes.getInt("id");
                    String   vote_average = filmes.getString("vote_average");
                    String title = filmes.getString("title");
                    String popularity = filmes.getString("popularity");
                    String poster_path = filmes.getString("poster_path");
                    String original_language = filmes.getString("original_language");
                    String original_title = filmes.getString("original_title");
                    String overview = filmes.getString("overview");
                    String release_date = filmes.getString("release_date");

                    String descricao = filmes.getString("title");
                    //Toast.makeText(MainActivity.this, descricao, Toast.LENGTH_SHORT).show();
                    Movie novo = new Movie();
                    novo.setVote_count(vote_count);
                    novo.setId(id);
                    novo.setVote_average(Float.parseFloat(vote_average));
                    novo.setTitle(title);
                    novo.setPopularity(Float.parseFloat(popularity));
                    novo.setPoster_path(poster_path);
                    novo.setOriginal_language(original_language);
                    novo.setOriginal_title(original_title);
                    novo.setOverview(overview);
                   // novo.setRelease_date(java.sql.Date.valueOf(release_date));

                    movieList.add(novo);
                    testList.add(descricao);
                }
                MovieListView.setAdapter(moviesAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
