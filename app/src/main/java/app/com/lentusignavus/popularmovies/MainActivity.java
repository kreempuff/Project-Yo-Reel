package app.com.lentusignavus.popularmovies;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String imageBaseUrl = "http://image.tmbd.org/t/p/";
    HashMap sizes = ImageSize.getSizes();
    String imageUrl;
    ArrayAdapter<String> imageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView mgridView = (GridView) findViewById(R.id.gridview);


        mgridView.setAdapter(new ImageAdapter(this));

        GetMovies getMovies = new GetMovies();
        getMovies.execute();


        //imageUrl = imageBaseUrl + sizes.get("w185").toString() + "/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";

        //Picasso.with(getApplication()).load("http://www.iconsdb.com/icons/preview/yellow/square-xxl.png").into(imageView);
    }

    public void showToast(View v) {
        String imageDescription = (String) v.getContentDescription();
        imageUrl = imageBaseUrl + sizes.get("w185").toString() + "/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        Toast.makeText(getApplication(), imageUrl, Toast.LENGTH_SHORT).show();
    }
}
