package com.sarthak.trackit.trackit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    boolean unique = false;

    String displayName, username, email, password;

    EditText mDisplayNameEt, mUsernameEt, mEmailEt, mPasswordEt;
    Button mSignUpBtn;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mDisplayNameEt = findViewById(R.id.sign_up_display_name_et);
        mUsernameEt = findViewById(R.id.sign_up_username_et);
        mEmailEt = findViewById(R.id.sign_up_email_et);
        mPasswordEt = findViewById(R.id.sign_up_pass_et);
        mSignUpBtn = findViewById(R.id.sign_up_btn);

        mUsernameEt.setOnFocusChangeListener(this);
        mSignUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sign_up_btn:

                displayName = mDisplayNameEt.getText().toString();
                username = mUsernameEt.getText().toString();
                email = mEmailEt.getText().toString();
                password = mPasswordEt.getText().toString();

                if (displayName != null && username != null && email != null && password != null) {

                    if (unique) {

                        signUp();
                    } else {

                        Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        if (!hasFocus) {

            username = mUsernameEt.getText().toString();

            mFirestore.collection(Constants.USERS_REFERENCE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {

                        for (DocumentSnapshot documentSnapshot : task.getResult()) {

                            if (documentSnapshot.exists()) {

                                if (username.equals(documentSnapshot.toObject(User.class).getUsername())) {

                                    unique = false;
                                    Log.e("TAG", "lolsame");
                                    break;
                                } else {

                                    unique = true;
                                }
                            } else {

                                unique = true;
                            }
                        }
                    } else {

                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void signUp() {

        final String deviceToken = FirebaseInstanceId.getInstance().getToken();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    User user = new User(username, displayName, email, deviceToken, "profile", "thumb");

                    mFirestore.collection(Constants.USERS_REFERENCE).document(mAuth.getCurrentUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Intent mapIntent = new Intent(SignUpActivity.this, HomeActivity.class);
                                mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mapIntent);
                                finish();

                            } else {

                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {

                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
