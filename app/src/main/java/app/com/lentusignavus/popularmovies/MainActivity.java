package app.com.lentusignavus.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {

    @Bind(R.id.gridview) GridView mgridView;
    @Bind(R.id.toolbar) Toolbar toolbar;
    Intent detailView;
    Boolean tabletMode;
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
    public void onFragmentInteraction() {
        return;
    }

    @Override
    public void onFragmentInteraction(Intent detailIntent) {

        tabletMode = (findViewById(R.id.detail_container) != null);

        if (tabletMode) {
            FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
            Fragment detailFrag = DetailFragment.newInstance(detailIntent.getExtras());
            frag.replace(R.id.detail_container, detailFrag);
            frag.commit();
        } else {
            startActivity(detailIntent);
        }
    }
}
