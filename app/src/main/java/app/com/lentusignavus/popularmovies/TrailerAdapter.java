package app.com.lentusignavus.popularmovies;

import android.content.Context;
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
        Button textView;
        if (convertView == null){
            textView = new Button(mContext);
            try {
                textView.setText(trailerArray.getJSONObject(position).getString("key"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));


        } else {

            textView = (Button) convertView;

        }





        return textView;
    }
}
