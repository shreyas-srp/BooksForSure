package booksforsure.galaxyworks.com.booksforsure;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.digits.sdk.android.Digits;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import booksforsure.galaxyworks.com.booksforsure.AnimationAdapters.ScaleInAnimationAdapter;
import booksforsure.galaxyworks.com.booksforsure.Holders.History_holder;

public class History extends AppCompatActivity {

    private RecyclerView mRcyclerView;
    private History_Cards_Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRcyclerView = (RecyclerView) findViewById(R.id.history_list);
        mRcyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRcyclerView.setItemAnimator(new DefaultItemAnimator());

        gethistory();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void gethistory() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("OrderHistory");
        String phone = Digits.getSessionManager().getActiveSession().getPhoneNumber();
        query.whereEqualTo("phoneNumber", phone);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> List, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + List.size() + " scores");

                    final List<History_holder> history = new ArrayList<History_holder>();

                    for (int i = 0; i < List.size(); i++) {
                        ParseObject ob = List.get(i);
                        final History_holder hist = new History_holder();
                        hist.type = ob.getInt("type");
                        hist.time = ob.getCreatedAt().toString();

                        if (hist.type == 1) {
                            hist.image = (ParseFile) ob.get("photoOrder");
                            history.add(hist);

                        } else if (hist.type == 2) {
                            hist.order = ob.getString("textOrder");
                            history.add(hist);
                        }

                    }
                    progressDialog.dismiss();
                    mAdapter = new History_Cards_Adapter(history, getApplicationContext());
                    mRcyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }

            }
        });

    }

}
