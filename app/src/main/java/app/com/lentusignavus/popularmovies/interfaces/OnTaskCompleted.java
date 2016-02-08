package app.com.lentusignavus.popularmovies.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kare2436 on 1/5/16.
 */
public interface OnTaskCompleted {
    void onTaskCompleted(JSONObject jsonObject) throws JSONException;
}
