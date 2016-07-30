package booksforsure1.galaxyworks.com.galaxyworks;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Offer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        final TextView t1 = (TextView) findViewById(R.id.offer_text);
      //  Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_SHORT).show();
        final ProgressDialog progressDialog = new ProgressDialog(Offer.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Offer");
        query.whereEqualTo("objectId", "vK9K2GkRIG");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                String offer = object.getString("offerNew");
                t1.setText(offer);
                progressDialog.dismiss();
              //  Toast.makeText(getApplicationContext(), offer, Toast.LENGTH_SHORT).show();
            }
        });
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
    }
}
