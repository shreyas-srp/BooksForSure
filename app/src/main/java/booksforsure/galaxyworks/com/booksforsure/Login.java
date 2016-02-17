package booksforsure.galaxyworks.com.booksforsure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class Login extends AppCompatActivity {

    private AuthCallback authCallback;
    Button login_btn;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login_btn = (Button) findViewById(R.id.login_btn);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login(){



        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, final String phoneNumber) {

                final String phone = Digits.getSessionManager().getActiveSession().getPhoneNumber();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("User_details");
                String phoneNo = Digits.getSessionManager().getActiveSession().getPhoneNumber();
                query.whereEqualTo("phoneNumber", phoneNo);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject user, ParseException e) {
                        //progressDialog.dismiss();
                        if (e == null) {
                            //progressDialog.show();
                            SharedPreferences user_details = getSharedPreferences("user_details_sharedpref", MODE_PRIVATE);
                            SharedPreferences.Editor user_details_editor = user_details.edit();
                            user_details_editor.putString("userPhoneNumber", phone);
                            user_details_editor.commit();

                            Intent home;
                            if(user_details.contains("new")) {
                                home = new Intent(getApplicationContext(), Homepage.class);
                            }else {
                                home = new Intent(getApplicationContext(), Welcome.class);
                            }
                            startActivity(home);
                            finish();


                        } else if(e.getCode()==101){

                            ParseObject user_details = new ParseObject("User_details");
                            user_details.put("phoneNumber", phone);

                            user_details.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null) {

                                        SharedPreferences user_details = getSharedPreferences("user_details_sharedpref", MODE_PRIVATE);
                                        SharedPreferences.Editor user_details_editor = user_details.edit();
                                        user_details_editor.putString("userPhoneNumber", phone);
                                        user_details_editor.commit();

                                        Intent get_details = new Intent(getApplicationContext(), Edit_Profile.class);
                                        startActivity(get_details);
                                        finish();
                                    } else {
                                        Snackbar.make(coordinatorLayout, "Failed!", Snackbar.LENGTH_INDEFINITE).setAction("Try Again!", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                login();
                                            }
                                        }).show();
                                    }
                                }
                            });


                        }
                        else {
                            Log.d("score", "The getFirst request failed.");
                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                Snackbar.make(coordinatorLayout,"Failed!",Snackbar.LENGTH_INDEFINITE).setAction("Try Again!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                }).show();
            }
        };

        Digits.authenticate(authCallback,R.style.AppTheme,"+91",false);

    }

}
