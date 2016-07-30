package booksforsure1.galaxyworks.com.galaxyworks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

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
                        String phone=user_details.getString("userPhoneNumber","0");

                        ParseQuery<ParseObject> query=ParseQuery.getQuery("OrderHistory");
                        query.whereEqualTo("phoneNumber", phone);
                        query.orderByDescending("createdAt");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null){
                                    int i=0;
                                    for (ParseObject order : objects) {
                                        int status = order.getInt("status");
                                        if (status == 0) {
                                            Intent intent = new Intent(getApplicationContext(),History.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                }

                            }
                        });
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
