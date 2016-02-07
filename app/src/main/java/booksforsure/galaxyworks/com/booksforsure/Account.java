package booksforsure.galaxyworks.com.booksforsure;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Account extends AppCompatActivity {

    TextView name,phone_no,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (TextView) findViewById(R.id.account_name);
        phone_no = (TextView) findViewById(R.id.account_phone);
        address = (TextView) findViewById(R.id.account_address);

        get_details();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void get_details(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User_details");
        final String phone = Digits.getSessionManager().getActiveSession().getPhoneNumber();
        query.whereEqualTo("phoneNumber", phone);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    Log.d("score", "retrieved");

                    name.setText(object.getString("user_name"));
                    phone_no.setText(object.getString("phoneNumber"));
                    address.setText(object.getString("userAddress"));

                } else {
                    Log.d("score", "The getFirst request failed.");
                    Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }

}
