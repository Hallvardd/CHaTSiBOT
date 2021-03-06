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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.taphan.core1.course.InfoActivity;
import com.example.taphan.core1.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.taphan.core1.login.LoginActivity.globalUser;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    private static final String student = "Student";
    private static final String professor = "Professor";
    private static final String ta = "TA";

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userType;
    private EmailValidator emailValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        userType = student;

        emailValidator = new EmailValidator();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for valid input
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(! emailValidator.validate(email)) {
                    Toast.makeText(getApplicationContext(), "Invalid email format!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "Created account complete: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                User localUser = new User();// creates a user object to store
                                // Add user to 'user' child in firebase to store information about courses
                                user = auth.getCurrentUser();
                                /*
                                String userID = user.getUid(); // Get userID from firebase, then put in database object
                                localUser.setUserType(userType);
                                localUser.setUserID(userID);
                                localUser.setEmail(email);
                                mDatabase.child("users").child(userID).setValue(localUser);
                                */

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {

                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(SignupActivity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String userID = user.getUid(); // Get userID from firebase, then put in database object
                                    localUser.setUserType(userType);
                                    localUser.setUserID(userID);
                                    localUser.setEmail(email);
                                    mDatabase.child("users").child(userID).setValue(localUser);
                                    globalUser = localUser;
                                    if(globalUser.getUserType().equals("TA")) {
                                        startActivity(new Intent(SignupActivity.this, TaActivity.class));
                                    } else {
                                        startActivity(new Intent(SignupActivity.this, InfoActivity.class));
                                    }
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_stud:
                if (checked)
                    userType = student;
                break;
            case R.id.radio_prof:
                if (checked)
                    userType = professor;
                    break;
            case R.id.radio_ta:
                if (checked)
                    userType = ta;
                break;
        }

    }
}