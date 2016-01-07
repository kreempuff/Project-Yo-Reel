package app.com.lentusignavus.popularmovies;

import java.util.HashMap;

/**
 * Created by kare2436 on 12/30/15.
 */
public class ImageSize {
    private static HashMap<String, String> sizes = new HashMap<String, String>();



    public static HashMap<String, String> getSizes() {
        sizes.put("w92", "w92/");
        sizes.put("w154", "w154/");
        sizes.put("w185", "w185/");
        sizes.put("w342", "w342/");
        sizes.put("w500", "w500/");
        sizes.put("w780", "w780/");
        sizes.put("original", "original/");

        return sizes;
    }


}
