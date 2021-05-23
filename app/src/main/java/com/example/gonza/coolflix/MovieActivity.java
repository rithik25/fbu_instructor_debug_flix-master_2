package com.example.gonza.coolflix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;

import com.example.gonza.coolflix.adapters.MoviesAdapter;
import com.example.gonza.coolflix.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    List<Movie> movies;
    AsyncHttpClient client;
    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    MoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        getSupportActionBar().hide();
        movies = new ArrayList<>();

        RecyclerView recViewMovies = findViewById(R.id.recview_movies);

        adapter = new MoviesAdapter(this, movies);
        recViewMovies.setAdapter(adapter);
        recViewMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        fetchMovies();
    }

    private void fetchMovies() {
        client = new AsyncHttpClient();
        Log.i("Fetch", "Success1");
        //accesses the movie api that contains all the movies in the form of JSON objects
        client.get(MOVIE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("Success", "onSuccess");
                    //Each JSONArray is a movie + its info
                    JSONArray movieJsonArray = response.getJSONArray("results");
                    //List of all the movies
                    movies.addAll(Movie.fromJsonArray(movieJsonArray));
                    adapter.changeMovies(movies);
                    adapter.notifyDataSetChanged();
                    Log.d("log-tag", movieJsonArray.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("FailCatch", e.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               // super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("Fail", responseString);
            }
        });
    }
}
