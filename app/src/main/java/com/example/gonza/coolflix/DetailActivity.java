package com.example.gonza.coolflix;

import android.media.Rating;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.gonza.coolflix.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyB0zaYRCbeLujlPo35Heqpbg67cYS0slO4";
    private static final String VIDEOS_API_KEY = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView movieTitle;
    TextView movieOverview;
    TextView releaseDate;
    TextView rating;
    RatingBar ratingBar;
    Movie movie;
    YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        movieTitle = findViewById(R.id.title_movie);
        movieOverview = findViewById(R.id.overview_movie);
        releaseDate = findViewById(R.id.date_release);
        ratingBar = findViewById(R.id.ratingBar);
        rating = findViewById(R.id.rating);
        youTubePlayerView = findViewById(R.id.player);


        movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        movieTitle.setText(movie.getTitle());
        movieOverview.setText(movie.getOverview());
        releaseDate.setText("Release Date: " + movie.getReleaseDate());
        rating.setText("Rating: " + movie.getVoteAverage());
        ratingBar.setRating((float) movie.getVoteAverage());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_API_KEY, movie.getMovieID()), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray results = response.getJSONArray("results");
                    if( results.length() != 0)
                    {
                        JSONObject trailer = results.getJSONObject(0);
                        String videoKey = trailer.getString("key");
                        initializeYoutube(videoKey);
                    }
                    else return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });




    }

    private void initializeYoutube(final String videoKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("i am a message", "on init success");
                if(movie.isPopular())
                    youTubePlayer.loadVideo(videoKey);
                else youTubePlayer.cueVideo(videoKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("i am message", "on init failure");

            }
        });
    }


}
