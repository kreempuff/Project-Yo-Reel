package app.com.lentusignavus.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;

/**
 * Created by kare2436 on 1/19/16.
 */
public class TrailerAdapter extends BaseAdapter {
    private JSONArray trailerArray;
    private Context mContext;



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
        View v;
        TextView textView;

        if (convertView == null){
//

            v = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, null);
            textView = (TextView) v.findViewById(R.id.trailer_list_text_view);

            Toast.makeText(mContext, "Trailer Adapter", Toast.LENGTH_SHORT).show();

            try {
                textView.setText(trailerArray.getJSONObject(position).getString("key"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            v = convertView;
        }


        return v;
    }
}
