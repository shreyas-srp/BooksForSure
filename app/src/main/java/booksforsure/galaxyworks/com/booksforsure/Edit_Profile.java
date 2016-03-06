package booksforsure.galaxyworks.com.booksforsure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class Edit_Profile extends AppCompatActivity {

    Button save_profile_btn;
    EditText name,address,landmark;
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
        landmark = (EditText) findViewById(R.id.landmark_edittext);

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
        final String userLandmark = landmark.getText().toString();

        if(userName.length() == 0) {
            name.setError("Enter Name");
            progressDialog.dismiss();
            return;
        }

        if(userAddress.length() == 0) {
            address.setError("Enter Address");
            progressDialog.dismiss();
            return;
        }

        if(userLandmark.length() == 0) {
            landmark.setError("Enter Landmark");
            progressDialog.dismiss();
            return;
        }



        final ParseQuery<ParseObject> query = ParseQuery.getQuery("User_details");
        final String phone = Digits.getSessionManager().getActiveSession().getPhoneNumber();
       ;

        query.whereEqualTo("phoneNumber", phone);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                //progressDialog.dismiss();
                if (e == null) {
                    //progressDialog.show();

                    user.put("user_name",userName);
                    user.put("userAddress",userAddress);
                    user.put("landmark",userLandmark);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                progressDialog.dismiss();
                                SharedPreferences user_details = getSharedPreferences("user_details_sharedpref",MODE_PRIVATE);
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

                                if(user_details.contains("new")) {
                                    Intent get_details = new Intent(getApplicationContext(), Homepage.class);
                                    startActivity(get_details);
                                    Toast.makeText(getApplicationContext(),"Profile updated",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Intent get_details = new Intent(getApplicationContext(), Welcome.class);
                                    startActivity(get_details);
                                    finish();
                                }
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
