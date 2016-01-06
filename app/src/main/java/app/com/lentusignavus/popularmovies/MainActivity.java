package app.com.lentusignavus.popularmovies;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    String imageBaseUrl = "http://image.tmbd.org/t/p/";
    HashMap sizes = ImageSize.getSizes();
    String imageUrl;
    ArrayAdapter<String> imageAdapter;
    GridView mgridView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         mgridView = (GridView) findViewById(R.id.gridview);


        //mgridView.setAdapter(new ImageAdapter(this));

        GetMovies getMovies = new GetMovies(this);

        getMovies.execute();

    }

    public void showToast(View v) {
        String imageDescription = (String) v.getContentDescription();
        imageUrl = imageBaseUrl + sizes.get("w185").toString() + "/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        Toast.makeText(getApplication(), imageUrl, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onTaskCompleted(JSONObject jsonObject) throws JSONException {
        final JSONArray jsonArrayOfMovies = jsonObject.getJSONArray("results");
        mgridView.setAdapter(new ImageAdapter(this, jsonArrayOfMovies));
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Toast.makeText(getApplicationContext(), jsonArrayOfMovies.getJSONObject(position).getString("original_title"), Toast.LENGTH_SHORT)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
