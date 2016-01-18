package app.com.lentusignavus.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailViewActivity extends AppCompatActivity {

    Bundle extras;
    String movieTitle = null;
    String movieImagePath = null;
    String movieDescription = null;
    Double voteAverage = null;
    String releaseDate = null;

    TextView movieTitleView;
    @Bind(R.id.movie_description)TextView movieDescriptionView;
    @Bind(R.id.movie_vote_average) TextView voteAverageView;
    @Bind(R.id.movie_release_date) TextView releaseDateView;
    @Bind(R.id.big_image_poster) ImageView moviePosterView;
    @Bind(R.id.detail_view_toolbar) Toolbar detailToolbar;


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


            getSupportActionBar().setTitle(movieTitle);

            //TODO make date format more user friendly
            Picasso.with(this).load(ApiInfo.getImageBaseUrl() + "w780" + movieImagePath).into(moviePosterView);
            //movieTitleView.setText(movieTitle);
            movieDescriptionView.setText(movieDescription);
            voteAverageView.setText("Vote Average: " + voteAverage.toString());
            releaseDateView.setText("Release Date: " + releaseDate);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
