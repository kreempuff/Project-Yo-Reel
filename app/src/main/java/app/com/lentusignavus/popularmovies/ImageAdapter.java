package app.com.lentusignavus.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by kare2436 on 12/30/15.
 */
    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private JSONArray movieJsonArray;
        private HashMap imageSizes = ImageSize.getSizes();

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
            ImageView imageView;
            Uri imageUrl = Uri.parse(ApiInfo.getImageBaseUrl());
            String finalImageUrl = "Nothing";
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(650, 650));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(1, 1, 1, 1);
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
            if (finalImageUrl != "Nothing"){

                //A forward slash is replaced during uri building for some reason
                //to the encoded value
                finalImageUrl = finalImageUrl.replaceAll("%2F", "/");
                Picasso.with(mContext).load(finalImageUrl).into(imageView);
            }
            return imageView;
        }

        

        // references to our images
        private String[] mImageUrls = {
                "http://www.iconsdb.com/icons/preview/yellow/square-xxl.png",
                "http://vignette2.wikia.nocookie.net/deathbattlefanon/images/6/6c/The_Flash.jpg/revision/latest?cb=20140816134052"
        };
    }
