package app.com.lentusignavus.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import app.com.lentusignavus.popularmovies.R;
import app.com.lentusignavus.popularmovies.fragments.DetailFragment;
import app.com.lentusignavus.popularmovies.fragments.MainFragment;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    Boolean tabletMode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


    }




    @Override
    public void onDetailFragmentInteraction() {
        if (tabletMode) {
            MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
            if (fragment != null){
                fragment.reloadMovies();
            }
        }
    }

    @Override
    public void onMainFragmentInteraction(Intent detailIntent) {

        tabletMode = (findViewById(R.id.detail_container) != null);

        if (tabletMode) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment detailFrag = DetailFragment.newInstance(detailIntent.getExtras());
            fragmentTransaction.replace(R.id.detail_container, detailFrag).commit();
        } else {
            startActivity(detailIntent);
        }
    }
}
