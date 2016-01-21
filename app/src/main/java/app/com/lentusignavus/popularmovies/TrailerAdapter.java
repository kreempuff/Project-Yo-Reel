package app.com.lentusignavus.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by kare2436 on 1/19/16.
 */
public class TrailerAdapter extends BaseAdapter {
    private JSONArray trailerArray;
    private Context mContext;
    private LayoutInflater inflater;



    public TrailerAdapter(JSONArray jsonArray, Context context){
        trailerArray = jsonArray;
        mContext = context;

    }

    @Override
    public int getCount() {
        return trailerArray.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            textView = new TextView(mContext, );
            textView = (TextView) inflater.inflate(R.layout.custom_text, null);

            try {
                textView.setText(trailerArray.getJSONObject(position).getString("key"));
            } catch (JSONException e) {
                e.printStackTrace();
            }



        } else {

            textView = (TextView) convertView;

        }


        Log.d(getClass().getSimpleName(), textView.toString());
        return textView;
    }
}
