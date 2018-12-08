package com.jose.fatec.pdm.movielibrary.movielibrary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView genreListView;
    private List<genre> genresList;
    private List<String> testList;
    private ArrayAdapter genresAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genreListView = findViewById(R.id.lista);
        genresList = new ArrayList<genre>();
        testList = new ArrayList<>();
        genresAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,  testList);

        //Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();

        String urlGenres = getString(R.string.url_genres);
        String apiKey = getString(R.string.api_key);
        String language = getString(R.string.language);
        String urlMontada = urlGenres + apiKey + "&language=" + language;
        new getGenres().execute(urlMontada);
        genreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                Intent it;
                it = new Intent(MainActivity.this, MovieList.class);
                genre choiceGenre = new genre();
                choiceGenre = genresList.get(position);
                it.putExtra("genre",choiceGenre);
                startActivity(it);
            }
        });

    }
    private class getGenres extends AsyncTask <String,Void,String>{


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
                JSONArray list = json.getJSONArray("genres");

                for (int i = 0; i < list.length(); i++){
                    JSONObject genero = list.getJSONObject(i);
                    int     id       = genero.getInt("id");
                    String descricao = genero.getString("name");
                    //Toast.makeText(MainActivity.this, descricao, Toast.LENGTH_SHORT).show();
                    genre novo = new genre();
                    novo.setId(id);
                    novo.setDescription(descricao);
                    testList.add(descricao);
                    genresList.add(novo);
                }
                genreListView.setAdapter(genresAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



}
