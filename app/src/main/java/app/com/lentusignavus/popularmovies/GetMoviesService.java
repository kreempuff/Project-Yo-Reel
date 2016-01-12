package app.com.lentusignavus.popularmovies;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kare2436 on 1/4/16.
 */
public class GetMoviesService extends IntentService {


     final String TAG = getClass().getSimpleName();
    private Uri.Builder api;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    Uri apiUrl;
    Bundle extras;

    String sortParam;

    String movieData = null;
    JSONObject movieDataJson = null;
    JSONHandler jsonHandler;
    private OnTaskCompleted listener;


    public GetMoviesService(OnTaskCompleted delegate){
        super("GetMoviesService");
        this.listener = delegate;
    }

    public GetMoviesService(){
        super("GetMoviesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Service", "Service Started");

        extras = intent.getExtras();

        if(extras != null){
             sortParam = extras.get("sort").toString();
        }

        try {

            apiUrl = Uri.parse(ApiInfo.getApiBaseUrl())
                    .buildUpon()
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", ApiInfo.getMoviedbKey())
                    .appendQueryParameter("sort_by", sortParam)
                    .build();

            URL builtUrl = new URL(apiUrl.toString());

            urlConnection = (HttpURLConnection) builtUrl.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }

            movieData = buffer.toString();




        } catch (Exception e) {
            Log.e(TAG, "Error", e);

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        jsonHandler = new JSONHandler();


        try {
            movieDataJson = jsonHandler.transformIntoJson(movieData);

            //listener.onTaskCompleted(movieDataJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }






        return;

    }


}
