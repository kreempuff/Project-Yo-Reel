package app.com.lentusignavus.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.com.lentusignavus.popularmovies.Utils.UtilMethods;
import app.com.lentusignavus.popularmovies.interfaces.OnTaskCompleted;
import cz.msebera.android.httpclient.Header;

public class GetMovies2 extends AsyncHttpClient {

    JSONObject trailers = null;
    JSONObject movies = null;
    Context serviceContext = null;



    private OnTaskCompleted delegate;

    public GetMovies2(OnTaskCompleted callback, Context context) {
        this.delegate = callback;
        serviceContext = context;
    }

    public JSONArray getReviews(String movieId){

        Uri reviewUrl = Uri.parse(ApiInfo.getApiBaseUrl())
                .buildUpon()
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", ApiInfo.getMoviedbKey())
                .build();
        get(reviewUrl.toString(), defaultHandler);

        return null;
    }

    public void getTrailers(String movieId) {


        Uri trailerUrl = Uri.parse(ApiInfo.getApiBaseUrl())
                .buildUpon()
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", ApiInfo.getMoviedbKey())
                .build();

        get(trailerUrl.toString(), defaultHandler);
    }

    public void getMovies(String sort){

        if(UtilMethods.connectedToNetwork(serviceContext)){
            Uri apiUrl = Uri.parse(ApiInfo.getApiBaseUrl())
                    .buildUpon()
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", ApiInfo.getMoviedbKey())
                    .appendQueryParameter("sort_by", sort)
                    .build();

            get(apiUrl.toString(), defaultHandler);
        } else {
            Toast.makeText(serviceContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private JsonHttpResponseHandler defaultHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject){

            try {
                delegate.onTaskCompleted(jsonObject);
            } catch (JSONException e){
                Log.e(getClass().getSimpleName(), "Error:", e);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject){
            try {
                delegate.onTaskCompleted(jsonObject);
            } catch (JSONException e){
                //TODO get rid of this
                Log.e(getClass().getSimpleName(), "Error:", e);
            }

        }
    };
}
