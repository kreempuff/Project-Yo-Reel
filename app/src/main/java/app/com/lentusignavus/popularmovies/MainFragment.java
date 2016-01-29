package app.com.lentusignavus.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.com.lentusignavus.popularmovies.database.MovieContract;
import app.com.lentusignavus.popularmovies.database.MovieHelper;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnTaskCompleted {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.gridview) GridView mgridView;
    //@Bind(R.id.toolbar) Toolbar toolbar;
    Intent detailView;
    String sort;
    GetMovies getMovies;
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;


    final String vote_sort = "vote_average.desc";
    final String pop_sort = "popularity.desc";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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


        sort = pop_sort;

        getMovies = new GetMovies(this);

        getMovies.execute(sort);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

        @Override
        public void onTaskCompleted(JSONObject jsonObject) throws JSONException {
            final JSONArray jsonArrayOfMovies = jsonObject.getJSONArray("results");

            mgridView.setAdapter(new ImageAdapter(getContext(), jsonArrayOfMovies));

            mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject movie;

                    String movieTitle = null;
                    String movieImagePath = null;
                    String movieDescription = null;
                    Double movieVoteAverage = null;
                    String movieReleaseDate = null;
                    String movieID = null;

                    try {
                        movie = jsonArrayOfMovies.getJSONObject(position);
                    } catch (JSONException e) {
                        Log.e(getClass().getSimpleName(), "MainActivity.OnItemClickListener", e);
                        return;
                    }



                    try {
                        movieTitle = movie.getString("original_title");
                        movieImagePath = movie.getString("poster_path");
                        movieDescription = movie.getString("overview");
                        movieVoteAverage = movie.getDouble("vote_average");
                        movieReleaseDate = movie.getString("release_date");
                        movieID = movie.getString("id");


                    } catch (JSONException e) {
                        Log.e(getClass().getSimpleName(), "MainActivity.OnItemClickListener", e);
                    }
                    Toast.makeText(getContext(), movieTitle, Toast.LENGTH_SHORT)
                            .show();

                    detailView = new Intent(getContext(), DetailViewActivity.class);
                    detailView.putExtra("title", movieTitle);
                    detailView.putExtra("imagePath", movieImagePath);
                    detailView.putExtra("description", movieDescription);
                    detailView.putExtra("vote_avg", movieVoteAverage);
                    detailView.putExtra("release_date", movieReleaseDate);
                    detailView.putExtra("movie_id", movieID);
                    startActivity(detailView);
                }
            });
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
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main_activity_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.popular_option_id):
                if(sort.equals(pop_sort)) {
                    break;
                } else {
                    sort = pop_sort;
                    getMovies = new GetMovies(this);
                    getMovies.execute(sort);
                    break;
                }
            case (R.id.vote_option_id):
                if(sort.equals(vote_sort)) {
                    break;
                } else {
                    sort = vote_sort;
                    getMovies = new GetMovies(this);
                    getMovies.execute(sort);
                    break;
                }
            case (R.id.settings_option):
                startActivity(new Intent(getContext(), Settings.class));
                break;
            case (R.id.favorite_movie_option):
                AsyncTask<Void, Void, Boolean> favorMov = new getFavoriteMoviesTask();
                favorMov.execute();
                break;
            case (R.id.delete_all_from_db):
                AsyncTask<Void, Void, Void> delete = new deleteDB();
                delete.execute();
                break;
        }
        return true;
    }



    private class getFavoriteMoviesTask extends AsyncTask<Void, Void, Boolean>{


        Boolean endEarly;
        JSONArray movies = new JSONArray();

        @Override
        protected Boolean doInBackground(Void... params) throws SQLException {


            JSONObject movie = new JSONObject();


            dbHelper = new MovieHelper(getContext());

            db = dbHelper.getReadableDatabase();


            String[] columns = {
                    MovieContract.MovieEntry.COLUMN_TITLE,
                    MovieContract.MovieEntry.COLUMN_RATING,
                    MovieContract.MovieEntry.COLUMN_REAL_DATE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                    MovieContract.MovieEntry.COLUMN_DESC,
                    MovieContract.MovieEntry.COLUMN_IMAGE_URI
            };

            Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, columns, null, null, null, null, null);
            Boolean results = cursor.moveToFirst();
            if(!results) {
                endEarly = true;
                return endEarly;
            } else {
                while (results) {
                    int columnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URI);
                    Log.d(getClass().getSimpleName(), Integer.toString(columnIndex));
                    try {
                        movie.put("imagePath", cursor.getString(columnIndex));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    movies.put(movie);
                    //movie.remove("imagePath");
                    results = cursor.moveToNext();
                    Log.d(getClass().getSimpleName(), movies.toString());
                }

                endEarly = false;
            }
            return endEarly;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                Toast.makeText(getContext(), "No saved movies!", Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(getContext(), movies.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
    }


    private class deleteDB extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... params) throws SQLException {
            SQLiteOpenHelper sqLiteOpenHelper = new MovieHelper(getContext());

            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();

            db.execSQL(MovieHelper.deleteQuery);

            db.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aBoolean) {
            Toast.makeText(getContext(), "Click Fav Movies", Toast.LENGTH_SHORT).show();
            return;
        }
    }

}
