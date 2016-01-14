package app.com.lentusignavus.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;

public class DetailViewActivity extends AppCompatActivity {

    Bundle extras;
    String movieTitle = "";
    String movieImagePath = "";
    String movieDescription = "";
    Double voteAverage = null;
    String releaseDate = null;

    TextView movieTitleView;
    TextView movieDescriptionView;
    TextView voteAverageView;
    TextView releaseDateView;
    ImageView moviePosterView;
    Toolbar detailToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        moviePosterView = (ImageView) findViewById(R.id.big_image_poster);
        //May be used in P2 to display title instead of toolbar
        //movieTitleView = (TextView) findViewById(R.id.movie_title);
        movieDescriptionView = (TextView) findViewById(R.id.movie_description);
        voteAverageView = (TextView) findViewById(R.id.movie_vote_average);
        releaseDateView = (TextView) findViewById(R.id.movie_release_date);

        detailToolbar = (Toolbar) findViewById(R.id.detail_view_toolbar);

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



    private int getIntegerFromText(String textNumber){
        return Integer.parseInt(textNumber);
    }
}
