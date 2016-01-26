package booksforsure.galaxyworks.com.booksforsure;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.digits.sdk.android.Digits;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

/**
 * Created by shrey on 26-01-2016.
 */
public class MyApp extends Application{

    private static final String TWITTER_KEY = "o3BDU8NGDuNtzI4meeG7VSPv6";
    private static final String TWITTER_SECRET = "tzstIqggBekQHhLacOT8hNq36d6upK9zhCIcQhNGjdD8dMutCG";

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "YOLPl8WD6T36lrvNPDKVszMuwpodsYUhxXBKpmoq", "0TVywLlePatIcptbzIVtDWNj5L0tDhE7ZrbWLkjg");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        TypefaceProvider.registerDefaultIconSets();
        //ParseInstallation pi = ParseInstallation.getCurrentInstallation();
    }
}
