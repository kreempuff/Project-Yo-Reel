package app.com.lentusignavus.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import app.com.lentusignavus.popularmovies.database.MovieContract;
import app.com.lentusignavus.popularmovies.database.MovieHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DetailViewActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener {

    Bundle extras;
    String movieTitle;
    String movieImagePath;
    String movieDescription;
    Double voteAverage;
    String releaseDate;
    String movieId;
    TrailerAdapter adapter;

    TextView movieTitleView;
    @Bind(R.id.movie_description)TextView movieDescriptionView;
    @Bind(R.id.movie_vote_average) TextView voteAverageView;
    @Bind(R.id.movie_release_date) TextView releaseDateView;
    @Bind(R.id.big_image_poster) ImageView moviePosterView;
    @Bind(R.id.detail_view_toolbar) Toolbar detailToolbar;
    @Bind(R.id.trailer_list_view) ListView listView;
    JSONArray youtubeVids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Fragment detailFragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment);


        ButterKnife.bind(this);

        //May be used in P2 to display title instead of toolbar
        //movieTitleView = (TextView) findViewById(R.id.movie_title);


        setSupportActionBar(detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        extras = getIntent().getExtras();
        if (extras != null) {
            movieTitle = extras.getString("title");
            movieImagePath = extras.getString("imagePath");
            movieDescription = extras.getString("description");
            voteAverage = extras.getDouble("vote_avg");
            releaseDate = extras.getString("release_date");
            movieId = extras.getString("movie_id");

            getMovieTrailers(movieId);


            getSupportActionBar().setTitle(movieTitle);
        }

//        saveMovie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    Toast.makeText(getApplicationContext(), "Uncheck", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private void getMovieTrailers(final String movieId) {
        Uri trailerUrl = Uri.parse(ApiInfo.getApiBaseUrl())
                .buildUpon()
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", ApiInfo.getMoviedbKey())
                .build();

        Uri reviewUrl = Uri.parse(ApiInfo.getApiBaseUrl())
                .buildUpon()
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", ApiInfo.getMoviedbKey())
                .build();

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(trailerUrl.toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {
                    youtubeVids = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(youtubeVids != null){
                    adapter = new TrailerAdapter(youtubeVids, getApplicationContext());

                    //Toast.makeText(getApplicationContext(), youtubeVids.toString(), Toast.LENGTH_LONG).show();

                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Uri youtube = Uri.parse("http://www.youtube.com/")
                                    .buildUpon()
                                    .appendPath("watch")
                                    .appendQueryParameter("v", ((TextView) view.findViewById(R.id.trailer_list_text_view)).getText().toString())
                                    .build();
                            startActivity(new Intent(Intent.ACTION_VIEW, youtube));
                        }
                    });

                }

                Log.d(getLocalClassName(), response.toString());
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        client.get(reviewUrl.toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }






    //saves movie to database
    @Override
    public void onFragmentInteraction() {
        return;
    }
}
