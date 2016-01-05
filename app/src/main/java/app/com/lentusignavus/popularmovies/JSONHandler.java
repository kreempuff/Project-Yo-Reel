package app.com.lentusignavus.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kare2436 on 1/4/16.
 */
public class JSONHandler {

    public JSONObject transformIntoJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject;

    }

    public JSONArray getJsonArray(String jsonObject) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonObject);
        return jsonArray;
    };




}
