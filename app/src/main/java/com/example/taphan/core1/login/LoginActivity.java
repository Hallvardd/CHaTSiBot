package com.example.taphan.core1.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.taphan.core1.R;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.taphan.core1.chat.ChatActivity;
import com.example.taphan.core1.course.InfoActivity;
import com.example.taphan.core1.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLoginProf, btnLoginStud, btnReset;
    private FirebaseUser firebaseUser;
    public static User globalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
/*
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, InfoActivity.class));
            finish();
        }
*/
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLoginProf = (Button) findViewById(R.id.btn_login_prof);
        btnLoginStud = (Button) findViewById(R.id.btn_login_stud);
        btnReset = (Button) findViewById(R.id.btn_reset_password);


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
        globalUser.setUserID(firebaseUser.getUid());

        btnLoginProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user = new User("Professor");
                globalUser.setUserType("Professor");
                clickButton();
            }
        });

        btnLoginStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user = new User("Student");
                globalUser.setUserType("Student");
                clickButton();
            }
        });
    }

    private void clickButton() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate globalUser
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the globalUser. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in globalUser can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            {
                                // Log in successfully will lead to Info page
                                Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });

    }
}

