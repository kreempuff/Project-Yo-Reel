package app.com.lentusignavus.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import app.com.lentusignavus.popularmovies.database.MovieContract;
import app.com.lentusignavus.popularmovies.database.MovieHelper;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    private final String LOG_TAG = "DetailFragment - TAG";


    Bundle extras;
    String movieTitle;
    String movieImagePath;
    String movieDescription;
    Double voteAverage;
    String releaseDate;
    String movieId;
    TrailerAdapter adapter;

    TextView movieTitleView;
    @Bind(R.id.movie_description) TextView movieDescriptionView;
    @Bind(R.id.movie_vote_average) TextView voteAverageView;
    @Bind(R.id.movie_release_date) TextView releaseDateView;
    @Bind(R.id.big_image_poster) ImageView moviePosterView;
    //@Nullable @Bind(R.id.detail_view_toolbar) Toolbar detailToolbar;
    //@Bind(R.id.trailer_list_view) ListView listView;
    //@Bind(R.id.save_movie_button) ImageButton saveMovieButton;
     @Bind(R.id.save_movie_button) ToggleButton saveMovieButton;
    JSONArray youtubeVids;

    SQLiteOpenHelper movieHelper;
    SQLiteDatabase db;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mainActivityBundle Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(Bundle mainActivityBundle) {
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(mainActivityBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        ButterKnife.bind(this, rootView);


        //TODO make date format more user friendly
        Picasso.with(getContext()).load(ApiInfo.getImageBaseUrl() + "w780" + movieImagePath).into(moviePosterView);
//        movieTitleView.setText(movieTitle);
        movieDescriptionView.setText(movieDescription);
        voteAverageView.setText(String.format("Vote Average: %s", voteAverage.toString()));
        releaseDateView.setText(String.format("Release Date: %s", releaseDate));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkIfMovieIsFavoriteAndSetUpFavoriteButton();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        extras = getActivity().getIntent().getExtras();
        Bundle tabletExtras = getArguments();

        if (extras != null) {

            movieTitle = extras.getString("title");
            movieImagePath = extras.getString("imagePath");
            movieDescription = extras.getString("description");
            voteAverage = extras.getDouble("vote_avg");
            releaseDate = extras.getString("release_date");
            movieId = extras.getString("movie_id");

            Log.d(LOG_TAG, extras.toString());

        } else if (tabletExtras != null){

            movieTitle = tabletExtras.getString("title");
            movieImagePath = tabletExtras.getString("imagePath");
            movieDescription = tabletExtras.getString("description");
            voteAverage = tabletExtras.getDouble("vote_avg");
            releaseDate = tabletExtras.getString("release_date");
            movieId = tabletExtras.getString("movie_id");

            Log.d("Table Extra", tabletExtras.toString());
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }

    public void onClick(View v){

        mListener.onFragmentInteraction();

        return;

    }


    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    private void checkIfMovieIsFavoriteAndSetUpFavoriteButton() {

        movieHelper = new MovieHelper(getContext());

        db = movieHelper.getWritableDatabase();

        String[] columnsToReturn = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID
        };

        String[] selectionArgs = {
                movieId
        };

        //TODO remove early stop here
        if(movieId == null) return;

        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                columnsToReturn,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
            saveMovieButton.setChecked(true);
            cursor.close();
        } else {
            saveMovieButton.setChecked(false);
            cursor.close();
        }

        saveMovieButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d(LOG_TAG, "IS CHECKED:" + (String.valueOf(isChecked)));
                if(isChecked) {
                    favoriteMovie();
                } else {
                    unFavoriteMovie();
                }
            }
        });

    }

    public boolean favoriteMovie() throws SQLException {
        movieHelper = new MovieHelper(getContext());

        db = movieHelper.getWritableDatabase();

        String[] columnsToReturn = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID
        };

        String[] selectionArgs = {
                movieId
        };

        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                columnsToReturn, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
            Toast.makeText(getContext(), "Movie Saved Already", Toast.LENGTH_SHORT).show();
            cursor.close();
            return false;
        }


        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movieTitle);
        cv.put(MovieContract.MovieEntry.COLUMN_DESC, movieDescription);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(MovieContract.MovieEntry.COLUMN_IMAGE_URI, movieImagePath);
        cv.put(MovieContract.MovieEntry.COLUMN_RATING, voteAverage);
        cv.put(MovieContract.MovieEntry.COLUMN_REAL_DATE, releaseDate);

        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
        cursor.close();
        db.close();

        return true;
    }

    public void unFavoriteMovie() {
        movieHelper = new MovieHelper(getContext());

        db = movieHelper.getWritableDatabase();

        String deleteQuery = String.format("DELETE FROM %s WHERE %s = %s",
                MovieContract.MovieEntry.TABLE_NAME,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                movieId);
        Log.d(LOG_TAG, deleteQuery);
        db.close();



    }

}
