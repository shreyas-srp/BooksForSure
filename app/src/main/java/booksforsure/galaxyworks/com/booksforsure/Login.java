package booksforsure.galaxyworks.com.booksforsure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.parse.ParseException;
import com.parse.ParseObject;
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

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {

                ParseObject user_details = new ParseObject("User_details");
                String phone = Digits.getSessionManager().getActiveSession().getPhoneNumber();
                user_details.put("phoneNumber",phone);

                user_details.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if(e == null){
                            Intent get_details = new Intent(getApplicationContext(),Edit_Profile.class);
                            startActivity(get_details);
                            finish();
                        }
                        else {
                            Snackbar.make(coordinatorLayout,"Failed!",Snackbar.LENGTH_INDEFINITE).setAction("Try Again!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    login();
                                }
                            }).show();
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
