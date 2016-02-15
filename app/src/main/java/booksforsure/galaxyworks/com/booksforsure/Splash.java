package booksforsure.galaxyworks.com.booksforsure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TypefaceProvider.registerDefaultIconSets();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences user_details = getSharedPreferences("user_details_sharedpref",MODE_PRIVATE);
                Intent mainIntent;

                if(user_details.contains("userPhoneNumber")) {
                    if(user_details.contains("new")) {
                        mainIntent = new Intent(getApplicationContext(), Homepage.class);
                    }else{
                        mainIntent = new Intent(getApplicationContext(), Welcome.class);
                    }
                }else {
                        mainIntent = new Intent(getApplicationContext(), Login.class);
                }

                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
