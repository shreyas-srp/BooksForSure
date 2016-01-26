package booksforsure.galaxyworks.com.booksforsure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

public class Login extends AppCompatActivity {

    private AuthCallback authCallback;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login_btn = (Button) findViewById(R.id.login_btn);

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
            public void success(DigitsSession session, String phoneNumber) {
                Intent get_details = new Intent(getApplicationContext(),Edit_Profile.class);
                startActivity(get_details);
                finish();
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                Toast.makeText(getApplicationContext(),"Sorry! Try again!",Toast.LENGTH_SHORT).show();
            }
        };

        Digits.authenticate(authCallback,R.style.AppTheme,"+91",false);
    }

}
