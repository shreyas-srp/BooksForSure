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
import android.widget.EditText;

import com.digits.sdk.android.Digits;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class Edit_Profile extends AppCompatActivity {

    Button save_profile_btn;
    EditText name,address;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        save_profile_btn = (Button) findViewById(R.id.save_profile_btn);
        name = (EditText) findViewById(R.id.name_edittext);
        address = (EditText) findViewById(R.id.address_edittext);

        save_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_details();
            }
        });
    }

    public void save_details(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String userName = name.getText().toString();
        final String userAddress = address.getText().toString();

        if(userAddress.length() == 0 || userName.length() == 0) {
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User_details");
        String phone = Digits.getSessionManager().getActiveSession().getPhoneNumber();
        query.whereEqualTo("phoneNumber", phone);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                //progressDialog.dismiss();
                if (e == null) {
                    //progressDialog.show();

                    user.put("user_name",userName);
                    user.put("userAddress",userAddress);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                progressDialog.dismiss();
                                Intent get_details = new Intent(getApplicationContext(),Homepage.class);
                                startActivity(get_details);
                                finish();
                            }
                            else {
                                Snackbar.make(coordinatorLayout,"Failed!",Snackbar.LENGTH_INDEFINITE).setAction("Try Again!", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        save_details();
                                    }
                                }).show();
                            }
                        }
                    });
                } else {
                    Snackbar.make(coordinatorLayout,"Failed!",Snackbar.LENGTH_INDEFINITE).setAction("Try Again!", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            save_details();
                        }
                    }).show();
                }
            }
        });

    }

}
