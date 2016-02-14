package app.com.lentusignavus.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private MovieHelper movieHelper;

    private static final int ONE_MOVIE = 1;

    private static final int ONE_MOVIE_ID = 2;




    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", ONE_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIES, ONE_MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieHelper = new MovieHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case ONE_MOVIE:
                cursor = movieHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Uri not supported: " + uri);
        }


        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri = null;
        switch (uriMatcher.match(uri)){
            case ONE_MOVIE:
                long ID = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (ID > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(ID);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;
        }


        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case ONE_MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ONE_MOVIE_ID:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
