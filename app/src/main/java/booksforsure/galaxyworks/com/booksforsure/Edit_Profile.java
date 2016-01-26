package booksforsure.galaxyworks.com.booksforsure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Edit_Profile extends AppCompatActivity {

    Button save_profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save_profile_btn = (Button) findViewById(R.id.save_profile_btn);

        save_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_details();
            }
        });
    }

    public void save_details(){
        Intent get_details = new Intent(getApplicationContext(),Homepage.class);
        startActivity(get_details);
        finish();
    }

}
