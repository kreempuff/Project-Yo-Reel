package app.com.lentusignavus.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DetailViewActivity extends AppCompatActivity {

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
        ButterKnife.bind(this);

        //May be used in P2 to display title instead of toolbar
        //movieTitleView = (TextView) findViewById(R.id.movie_title);


        setSupportActionBar(detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

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

            //TODO make date format more user friendly
            Picasso.with(this).load(ApiInfo.getImageBaseUrl() + "w780" + movieImagePath).into(moviePosterView);
            //movieTitleView.setText(movieTitle);
            movieDescriptionView.setText(movieDescription);
            voteAverageView.setText("Vote Average: " + voteAverage.toString());
            releaseDateView.setText("Release Date: " + releaseDate);
        }

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

                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Uri youtube = Uri.parse("http://www.youtube.com/")
                                    .buildUpon()
                                    .appendPath("watch")
                                    .appendQueryParameter("v", ((TextView) view.findViewById(R.id.trailer_list_text_view)).getText().toString())
                                    .build();
                            Toast.makeText(getApplicationContext(), youtube.toString(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Intent.ACTION_VIEW, youtube));
                        }
                    });

                }

                Log.d(getLocalClassName(), response.toString());
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                throwable.printStackTrace();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        client.get(reviewUrl.toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                Log.d(getClass().getSimpleName().toString(), response.toString());
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

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
