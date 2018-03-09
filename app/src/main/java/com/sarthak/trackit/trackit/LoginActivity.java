package com.sarthak.trackit.trackit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String email, password;

    EditText mEmailEt, mPasswordEt;
    Button mLoginBtn, mSignUpBtn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmailEt = findViewById(R.id.login_email_et);
        mPasswordEt = findViewById(R.id.login_pass_et);
        mLoginBtn = findViewById(R.id.login_login_btn);
        mSignUpBtn = findViewById(R.id.login_sign_up_btn);

        mLoginBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.login_sign_up_btn:

                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
                break;

            case R.id.login_login_btn:

                email = mEmailEt.getText().toString();
                password = mPasswordEt.getText().toString();

                if (email != null && password != null) {

                    login(email, password);
                }

                break;
        }
    }

    private void login(String email, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {

                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
