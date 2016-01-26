package booksforsure.galaxyworks.com.booksforsure;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by shrey on 26-01-2016.
 */
public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Y8JdtwlfkTJqVvGiKkAi2SWFxNlgQyfLiSG6h1EV", "SBwObRS30bOlMSUDADuaciFM5SJLtg6bnJLmMtCw");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        TypefaceProvider.registerDefaultIconSets();
        //ParseInstallation pi = ParseInstallation.getCurrentInstallation();
    }
}
