package app.com.lentusignavus.popularmovies.fragments;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import java.util.ArrayList;

import app.com.lentusignavus.popularmovies.ApiInfo;
import app.com.lentusignavus.popularmovies.R;
import app.com.lentusignavus.popularmovies.Utils.UtilMethods;
import app.com.lentusignavus.popularmovies.adapters.ReviewAdapter;
import app.com.lentusignavus.popularmovies.adapters.TrailerAdapter;
import app.com.lentusignavus.popularmovies.database.MovieContract;
import app.com.lentusignavus.popularmovies.database.MovieHelper;
import app.com.lentusignavus.popularmovies.interfaces.OnTaskCompleted;
import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    private final String LOG_TAG = "DetailFragment - TAG";


    Bundle extras;
    String movieTitle;
    String movieImagePath;
    String movieDescription;
    Double voteAverage;
    String releaseDate;
    String movieId;
    TrailerAdapter adapter;

    @Bind(R.id.movie_title) TextView movieTitleView;
    @Bind(R.id.movie_description) TextView movieDescriptionView;
    @Bind(R.id.movie_vote_average) TextView voteAverageView;
    @Bind(R.id.movie_release_date) TextView releaseDateView;
    @Bind(R.id.big_image_poster) ImageView moviePosterView;
    @Bind(R.id.save_movie_button) ToggleButton saveMovieButton;
    @Bind(R.id.trailer_list_view) ListView trailerListView;
    @Bind(R.id.review_list_view) ListView reviewList;
    JSONArray youtubeVids;


    private OnFragmentInteractionListener mListener;

    public DetailFragment() {}

    public static DetailFragment newInstance(Bundle mainActivityBundle) {
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(mainActivityBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        ButterKnife.bind(this, rootView);

        Picasso.with(getContext()).load(ApiInfo.getImageBaseUrl() + "w780" + movieImagePath).into(moviePosterView);
        movieDescriptionView.setText(movieDescription);
        voteAverageView.setText(String.format("Vote Average: %s/10", voteAverage.toString()));
        releaseDateView.setText(String.format("Release Date: %s", releaseDate));
        movieTitleView.setText(movieTitle);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMovieTrailers(movieId);

        checkIfMovieIsFavoriteAndSetUpFavoriteButton();
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onDetailFragmentInteraction();
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

        } else if (tabletExtras != null){

            movieTitle = tabletExtras.getString("title");
            movieImagePath = tabletExtras.getString("imagePath");
            movieDescription = tabletExtras.getString("description");
            voteAverage = tabletExtras.getDouble("vote_avg");
            releaseDate = tabletExtras.getString("release_date");
            movieId = tabletExtras.getString("movie_id");

        }

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
        void onDetailFragmentInteraction();
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

        if(movieId == null) {
            return;
        }


        String[] columnsToReturn = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID
        };

        String[] selectionArgs = {
                movieId
        };

        Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                columnsToReturn,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                selectionArgs, null, null);

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

                if(isChecked) {
                    favoriteMovie();
                    mListener.onDetailFragmentInteraction();
                } else {
                    unFavoriteMovie();
                    mListener.onDetailFragmentInteraction();
                }
            }
        });

    }

    public boolean favoriteMovie() throws SQLException {

        String[] columnsToReturn = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID
        };

        String[] selectionArgs = {
                movieId
        };

        Cursor cursor = getContext()
                .getContentResolver()
                .query(MovieContract.MovieEntry.CONTENT_URI,
                        columnsToReturn,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        selectionArgs,
                        null);

        if(cursor.moveToFirst()){
            Toast.makeText(getContext(), "Movie Saved Already", Toast.LENGTH_SHORT).show();
            return false;
        }


        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movieTitle);
        cv.put(MovieContract.MovieEntry.COLUMN_DESC, movieDescription);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(MovieContract.MovieEntry.COLUMN_IMAGE_URI, movieImagePath);
        cv.put(MovieContract.MovieEntry.COLUMN_RATING, voteAverage);
        cv.put(MovieContract.MovieEntry.COLUMN_REAL_DATE, releaseDate);

        getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
        cursor.close();

        return true;
    }

    public void unFavoriteMovie() {

        int rows = getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movieId, null);

        if (rows > 0) {
            Toast.makeText(getContext(), "Movie UnFavorited", Toast.LENGTH_SHORT).show();
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

        if (!UtilMethods.connectedToNetwork(getContext())){
            Toast.makeText(getContext(), "Not connected to Internet!", Toast.LENGTH_SHORT).show();
            return;
        }

        client.get(trailerUrl.toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {
                    youtubeVids = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(youtubeVids != null){
                    adapter = new TrailerAdapter(youtubeVids, getContext());


                    trailerListView.setAdapter(adapter);


                    trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String key;
                            try {
                                key = youtubeVids.getJSONObject(position).getString("key");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }

                            Uri youtube = Uri.parse("http://www.youtube.com/")
                                    .buildUpon()
                                    .appendPath("watch")
                                    .appendQueryParameter("v", key)
                                    .build();
                            startActivity(new Intent(Intent.ACTION_VIEW, youtube));
                        }
                    });

                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {

                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, response);
            }
        });

        client.get(reviewUrl.toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray reviews = new JSONArray();
                try {
                    reviews = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                reviewList.setAdapter(new ReviewAdapter(reviews, getContext()));
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response ) {

                Log.d(getClass().getSimpleName(), response.toString());

            }
        });



    }

}
