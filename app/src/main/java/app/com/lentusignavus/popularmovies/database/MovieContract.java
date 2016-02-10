package app.com.lentusignavus.popularmovies.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kare2436 on 1/23/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "app.com.lentusignavus.popularmovies.database";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";


    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movie";


        public static final String COLUMN_TRAIL = "trailers";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_IMAGE_URI = "poster_url";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_REAL_DATE = "release_date";

        public static final String COLUMN_DESC = "description";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }






    }





}
