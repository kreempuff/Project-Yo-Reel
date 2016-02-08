package app.com.lentusignavus.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.com.lentusignavus.popularmovies.Utils.JSONHandler;
import app.com.lentusignavus.popularmovies.interfaces.OnTaskCompleted;

/**
 * Created by kare2436 on 12/31/15.
 */
public class GetMovies extends AsyncTask<String, Void, JSONObject> {

    private final String TAG = getClass().getSimpleName();

    private Uri.Builder api;
    // These two need to be declared outside the try/catch
    // so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    Uri apiUrl;

    // Will contain the raw JSON response as a string.
    String movieData = null;
    JSONObject movieDataJson = null;
    JSONHandler jsonHandler;

    private OnTaskCompleted listener;

    public GetMovies(OnTaskCompleted delegate){
        this.listener = delegate;
    }

    @Override
    protected JSONObject doInBackground(String... sort) {


        try {

             apiUrl = Uri.parse(ApiInfo.getApiBaseUrl())
                    .buildUpon()
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", ApiInfo.getMoviedbKey())
                    .appendQueryParameter("sort_by", sort[0])
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
                return null;
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
                return null;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieDataJson;

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        try {
            listener.onTaskCompleted(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return;

    }


}
