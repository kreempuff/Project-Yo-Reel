package app.com.lentusignavus.popularmovies;

import com.orm.SugarRecord;

/**
 * Created by kare2436 on 1/24/16.
 */
public class FavoriteMovie extends SugarRecord {

    String title;
    Double voteAverage;

    String description;
    String movieId;
    String imagePath;
    String releaseDate;

    public FavoriteMovie (){}


}
