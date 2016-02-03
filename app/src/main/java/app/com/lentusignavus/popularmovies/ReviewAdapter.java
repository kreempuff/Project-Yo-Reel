package app.com.lentusignavus.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import static app.com.lentusignavus.popularmovies.R.color.text_color;

/**
 * Created by kare2436 on 1/19/16.
 */
public class ReviewAdapter extends BaseAdapter {
    private JSONArray reviewArray;
    private Context mContext;



    public ReviewAdapter(JSONArray jsonArray, Context context){
        reviewArray = jsonArray;
        mContext = context;

    }

    @Override
    public int getCount() {
        return reviewArray.length();
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
        View v;
        TextView textView;

        if (convertView == null){
//
            textView = new TextView(mContext);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(R.color.text_color);

            Toast.makeText(mContext, "Trailer Adapter", Toast.LENGTH_SHORT).show();

            try {
                textView.setText(reviewArray.getJSONObject(position).getString("content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            textView = (TextView) convertView;
        }


        return textView;
    }
}
