package app.com.lentusignavus.popularmovies;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    String imageBaseUrl = "http://image.tmbd.org/t/p/";
    HashMap sizes = ImageSize.getSizes();
    String imageUrl;
    ArrayAdapter<String> imageAdapter;
    GridView mgridView;
    Intent detailView;
    Intent intent;
    String sort;
    GetMovies getMovies;
    final String vote_sort = "vote_average.desc";
    final String pop_sort = "popularity.desc";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetMoviesService moviesService = new GetMoviesService(this);

        intent = new Intent(this, GetMoviesService.class);

        startService(intent);
         mgridView = (GridView) findViewById(R.id.gridview);
         sort = pop_sort;


        //mgridView.setAdapter(new ImageAdapter(this));

        getMovies = new GetMovies(this);

        getMovies.execute(sort);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.popular_option_id):
                if(sort == pop_sort) {
                    break;
                } else {
                    sort = pop_sort;
                    getMovies = new GetMovies(this);
                    getMovies.execute(sort);
                    break;
                }
            case (R.id.vote_option_id):
                if(sort == vote_sort) {
                    break;
                } else {
                    sort = vote_sort;
                    getMovies = new GetMovies(this);
                    getMovies.execute(sort);
                    break;
                }
        }
        return true;
    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject) throws JSONException {
        final JSONArray jsonArrayOfMovies = jsonObject.getJSONArray("results");

        mgridView.setAdapter(new ImageAdapter(this, jsonArrayOfMovies));
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject movie = null;
                try {
                    movie = jsonArrayOfMovies.getJSONObject(position);
                } catch (JSONException e) {
                    Log.e(getLocalClassName(), "MainActivity.OnItemClickListener", e);
                }

                String movieTitle = "";
                String movieImagePath = "";
                String movieDescription = "";
                Double movieVoteAverage = null;
                String movieReleaseDate = "";

                try {
                   movieTitle = movie.getString("original_title");
                   movieImagePath = movie.getString("poster_path");
                   movieDescription = movie.getString("overview");
                   movieVoteAverage = movie.getDouble("vote_average");
                   movieReleaseDate = movie.getString("release_date");

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
                startActivity(detailView);
            }
        });

    }
}
