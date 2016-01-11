package app.com.lentusignavus.popularmovies;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kare2436 on 1/4/16.
 */
public class GetMoviesService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GetMoviesService(String name) {
        super(name);
    }

    public GetMoviesService(){
        super("GetMoviesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Service", "Service Started");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(getClass().getSimpleName(), "Service Stopped");
    }
}
