package booksforsure.galaxyworks.com.booksforsure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import booksforsure.galaxyworks.com.booksforsure.R;

public class Discount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_welcome_slide_5);
        Button b = (Button) findViewById(R.id.home_btn);
        b.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
