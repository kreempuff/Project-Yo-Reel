package app.com.lentusignavus.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.com.lentusignavus.popularmovies.database.MovieHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements OnTaskCompleted, MainFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {

    @Bind(R.id.gridview) GridView mgridView;
    @Bind(R.id.toolbar) Toolbar toolbar;
    Intent detailView;
    String sort;
    GetMovies getMovies;
    final String vote_sort = "vote_average.desc";
    final String pop_sort = "popularity.desc";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject) throws JSONException {
        final JSONArray jsonArrayOfMovies = jsonObject.getJSONArray("results");

        mgridView.setAdapter(new ImageAdapter(this, jsonArrayOfMovies));

        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject movie;

                String movieTitle = null;
                String movieImagePath = null;
                String movieDescription = null;
                Double movieVoteAverage = null;
                String movieReleaseDate = null;
                String movieID = null;

                try {
                    movie = jsonArrayOfMovies.getJSONObject(position);
                } catch (JSONException e) {
                    Log.e(getLocalClassName(), "MainActivity.OnItemClickListener", e);
                    return;
                }



                try {
                   movieTitle = movie.getString("original_title");
                   movieImagePath = movie.getString("poster_path");
                   movieDescription = movie.getString("overview");
                   movieVoteAverage = movie.getDouble("vote_average");
                   movieReleaseDate = movie.getString("release_date");
                   movieID = movie.getString("id");


                } catch (JSONException e) {
                    Log.e(getLocalClassName(), "MainActivity.OnItemClickListener", e);
                }
                    Toast
                       .makeText(getApplicationContext(), movieTitle, Toast.LENGTH_SHORT)
                       .show();

                detailView = new Intent(getApplication(), DetailViewActivity.class);
                detailView.putExtra("title", movieTitle);
                detailView.putExtra("imagePath", movieImagePath);
                detailView.putExtra("description", movieDescription);
                detailView.putExtra("vote_avg", movieVoteAverage);
                detailView.putExtra("release_date", movieReleaseDate);
                detailView.putExtra("movie_id", movieID);
                startActivity(detailView);
            }
        });

    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }
}
