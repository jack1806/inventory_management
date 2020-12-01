package com.example.sewinventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.sewinventory.helper.sharePrefContract;
import com.example.sewinventory.helper.sharePrefHelper;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button submit;
    SharedPreferences preferences;
    sharePrefHelper prefHelper = new sharePrefHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(sharePrefContract.SHARED_PREF_NAME, sharePrefContract.SHARED_PREF_MODE);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.dologin);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = username.getText().toString();
                String p = password.getText().toString();
                if(u.equalsIgnoreCase("surya") && p.equalsIgnoreCase("surya")){
                    prefHelper.setLogin(preferences, true);
                    if(prefHelper.isLogin(preferences)){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
                password.setError("Incorrect details");
            }
        });
        if(prefHelper.isLogin(preferences)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}
