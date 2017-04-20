package com.example.taphan.core1.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.taphan.core1.R;
import com.example.taphan.core1.course.InfoActivity;

import static com.example.taphan.core1.login.LoginActivity.globalUser;

/**
 * Created by taphan on 04.04.2017.
 */

public class TaActivity extends AppCompatActivity {

    public static final String TAG = "TaActivity";

    private Button ta_login;
    private Button student_login;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta);

        ta_login = (Button) findViewById(R.id.ta_choose_prof);
        student_login = (Button) findViewById(R.id.ta_choose_student);


        ta_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                globalUser.setIsTa(true);
                Intent intent = new Intent(TaActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        student_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                globalUser.setIsTa(false);
                Intent intent = new Intent(TaActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

    }
}
