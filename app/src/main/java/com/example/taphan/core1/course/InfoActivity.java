package com.example.taphan.core1.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.taphan.core1.R;
import com.example.taphan.core1.login.LoginActivity;
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
    private DatabaseReference mUserDatabase;
    private static final String usersBranch =  "users";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Log.d(TAG,"The usertype is " + globalUser.getUserType() + " and the isTA is " + String.valueOf(globalUser.getIsTa()));

        addCourseButton = (Button) findViewById(R.id.add_course);
        signOutButton = (Button) findViewById(R.id.sign_out);
        infoAppButton = (Button) findViewById(R.id.app_info);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child(usersBranch);

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
