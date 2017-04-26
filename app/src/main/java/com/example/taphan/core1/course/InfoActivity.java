package com.example.taphan.core1.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.taphan.core1.R;
import com.example.taphan.core1.login.LoginActivity;
import com.example.taphan.core1.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.taphan.core1.login.LoginActivity.globalUser;

/**
 * Created by Charles on 23.03.2017.
 */

public class InfoActivity extends AppCompatActivity{
    public static final String TAG = "InfoActivity";

    private Button signOutButton;
    private Button addCourseButton;
    private Button infoAppButton;
    private TextView user_name;
    private DatabaseReference mUserDatabase;
    private static final String usersBranch =  "users";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        // Prevent crash when doing unit test
        if(globalUser == null)
            globalUser = new User();

        addCourseButton = (Button) findViewById(R.id.add_course);
        signOutButton = (Button) findViewById(R.id.sign_out);
        infoAppButton = (Button) findViewById(R.id.app_info);
        user_name = (TextView) findViewById(R.id.email_name);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child(usersBranch);

        String email_name = globalUser.getEmail();
        user_name.setText("Logged in as: " + email_name);

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(InfoActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(!globalUser.getUserID().isEmpty()){
                    String uID = globalUser.getUserID();
                    mUserDatabase.child(uID).setValue(globalUser);
                }
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        infoAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(InfoActivity.this, AppInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
