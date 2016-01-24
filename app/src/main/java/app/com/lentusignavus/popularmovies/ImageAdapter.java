package app.com.lentusignavus.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;


public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private JSONArray movieJsonArray;

        public ImageAdapter(Context c, JSONArray jsonArray) {
            mContext = c;
            movieJsonArray = jsonArray;
        }

        public int getCount() {
            return movieJsonArray.length();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int index, View convertView, ViewGroup parent) {

//             For custom grid item
//            View v = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_item, null);
//            ImageView imageView = (ImageView) v.findViewById(R.id.movie_grid_item_image);
//            TextView textView = (TextView) v.findViewById(R.id.movie_grid_item_text);

            int size = ((GridView) parent).getColumnWidth();
            ImageView imageView;

            Uri imageUrl = Uri.parse(ApiInfo.getImageBaseUrl());
            String finalImageUrl = null;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes


                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams( size, (size*2) ));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                //textView.setLayoutParams(new GridView.LayoutParams(size, ViewGroup.LayoutParams.WRAP_CONTENT));

                //imageView.setPadding(1, 1, 1, 1);
            } else {
                imageView = (ImageView) convertView;
            }

            try {
                finalImageUrl = imageUrl.buildUpon()
                        .appendPath("w185")
                        .appendPath(movieJsonArray.getJSONObject(index).getString("poster_path"))
                        .toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!finalImageUrl.equals(null)) {

                //A forward slash is replaced during uri building for some reason
                //to the encoded value and the api doesn't accept encoded values
                finalImageUrl = finalImageUrl.replaceAll("%2F", "/");
                Picasso.with(mContext).load(finalImageUrl).into(imageView);
            }


            return imageView;
        }
    }
