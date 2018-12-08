package com.jose.fatec.pdm.movielibrary.movielibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import org.w3c.dom.Text;


public class MovieActivity extends Activity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        //Recuperei o filme da outra activity
        setContentView(R.layout.movie);
        Toast.makeText(MovieActivity.this, movie.getTitle(), Toast.LENGTH_SHORT).show();


        TextView titleTxt = findViewById(R.id.movieTitle);
        TextView descriptionTxt = findViewById(R.id.movieDescription);
        String urlImage = getString(R.string.url_image) + movie.getPoster_path();

        titleTxt.setText(movie.getTitle());
        titleTxt.setHint(movie.getTitle());
        descriptionTxt.setText(movie.getOverview());
        descriptionTxt.setHint(movie.getOverview());
        //private SmartImageView teste;
        SmartImageView myImage = findViewById(R.id.my_image);
        myImage.setImageUrl(urlImage);



    }
}
