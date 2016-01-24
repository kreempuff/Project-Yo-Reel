package app.com.lentusignavus.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kare2436 on 1/23/16.
 */
public class MovieHelper extends SQLiteOpenHelper {


    private static final int VERSION = 1;

    static final String DATABASE_NAME = "movies.db";


    public MovieHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +


                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +

                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT, " +

                MovieContract.MovieEntry.COLUMN_DESC + " TEXT, " +

                MovieContract.MovieEntry.COLUMN_TRAIL + " TEXT, " +

                MovieContract.MovieEntry.COLUMN_DESC + " TEXT, " +

                MovieContract.MovieEntry.COLUMN_IMAGE_URI + " TEXT, " +

                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT, " +

                MovieContract.MovieEntry.COLUMN_REAL_DATE + " TEXT, " +

                MovieContract.MovieEntry.COLUMN_RATING + " REAL);";


        db.execSQL(SQL_CREATE_WEATHER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
