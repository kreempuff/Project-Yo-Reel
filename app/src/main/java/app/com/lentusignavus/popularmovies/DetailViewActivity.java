package app.com.lentusignavus.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailViewActivity extends AppCompatActivity {

    Bundle extras;
    String movieTitle = "";
    String movieImagePath = "";
    String movieDescription = "";

    TextView movieTitleView;
    TextView movieDescriptionView;
    ImageView moviePosterView;
    Toolbar detailToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        moviePosterView = (ImageView) findViewById(R.id.big_image_poster);
        movieTitleView = (TextView) findViewById(R.id.movie_title);
        movieDescriptionView = (TextView) findViewById(R.id.movie_description);
        detailToolbar = (Toolbar) findViewById(R.id.detail_view_toolbar);

        setSupportActionBar(detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            movieTitle = extras.getString("title");
            movieImagePath = extras.getString("imagePath");
            movieDescription = extras.getString("description");
            getSupportActionBar().setTitle(movieTitle);


            Picasso.with(this).load(ApiInfo.getImageBaseUrl() + "w780" + movieImagePath).into(moviePosterView);
            movieTitleView.setText(movieTitle);
            movieDescriptionView.setText(movieDescription);
        }

    }
}
