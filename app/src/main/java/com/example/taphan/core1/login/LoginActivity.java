package com.example.taphan.core1.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taphan.core1.R;
import com.example.taphan.core1.course.InfoActivity;
import com.example.taphan.core1.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignup, btnLogin, btnReset;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser firebaseUser;
    public static User globalUser;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        btnLogin.setBackgroundColor(getResources().getColor(R.color.primary_button));
        btnLogin.setEnabled(true);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        // A listener for when a user sign in and sign out
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                    // if the user is logged in user data is gathered from the database.
                    mUserDatabase.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                globalUser = dataSnapshot.getValue(User.class);
                                // Start the right page in accordance to user type
                                Intent intent;
                                Log.d(TAG, globalUser.getUserType());
                                if (globalUser.getUserType().equals("TA")) {
                                    intent = new Intent(LoginActivity.this, TaActivity.class);
                                } else {
                                    intent = new Intent(LoginActivity.this, InfoActivity.class);
                                }
                                startActivity(intent);
                                finish();
                            }
                            else{
                                // no userdata found in database, user is signed out. And new login is made posible
                                Toast.makeText(getApplicationContext(), "Userdata compormised. Contact system admin", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                btnLogin.setBackgroundColor(getResources().getColor(R.color.primary_button));
                                btnLogin.setEnabled(true);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        globalUser = new User();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton();
            }
        });

    }

    // When the login button is clicked
    private void clickButton() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        // Validate user input
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }


        //authenticate globalUser
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the globalUser. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in globalUser can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            {
                                // If the login is successful disable the login button.
                                btnLogin.setEnabled(false);
                                btnLogin.setBackgroundColor(Color.GRAY);
                                mUserDatabase.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // Checks if there is any user data in database.
                                        if(dataSnapshot.exists()){
                                            globalUser = dataSnapshot.getValue(User.class);
                                            // Start the right page in accordance to user type
                                            Intent intent;
                                            Log.d(TAG, globalUser.getUserType());
                                            if (globalUser.getUserType().equals("TA")) {
                                                intent = new Intent(LoginActivity.this, TaActivity.class);
                                            } else {
                                                intent = new Intent(LoginActivity.this, InfoActivity.class);
                                            }
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            // no userdata found in database, user is loged out. And new login is made posible
                                            Toast.makeText(getApplicationContext(), "Userdata compormised. Contact system admin", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                            btnLogin.setBackgroundColor(getResources().getColor(R.color.primary_button));
                                            btnLogin.setEnabled(true);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }
}