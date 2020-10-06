package com.keyur.raftis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout email, name, number, password, passwordr;
    Button btn;
    FirebaseAuth mFirebaseAuth;

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&*+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        if (amIConnected()) {
        } else {
            Intent intent1 = new Intent(getApplicationContext(), NoNetwork.class);
            intent1.putExtra("activity", "signup");
            startActivity(intent1);
            finish();
        }

        findViewById(R.id.signup_to_login).setOnClickListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signup_email);
        name = findViewById(R.id.signup_name);
        number = findViewById(R.id.signup_number);
        password = findViewById(R.id.signup_password);
        btn = findViewById(R.id.signup_btn);
        passwordr = findViewById(R.id.signup_password_retype);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");


        final LoadingDialog loadingDialog = new LoadingDialog(SignUp.this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amIConnected()) {
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), NoNetwork.class);
                    intent1.putExtra("activity", "signup");
                    startActivity(intent1);
                    finish();
                }
                passwordr.setError(null);
                email.setError(null);
                name.setError(null);
                number.setError(null);
                password.setError(null);
                if (amIConnected()) {
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), NoNetwork.class);
                    intent1.putExtra("activity", "signup");
                    startActivity(intent1);
                    finish();
                }
                final String emailID = email.getEditText().getText().toString().trim();
                String pwd = password.getEditText().getText().toString().trim();
                final String name1 = name.getEditText().getText().toString().trim();
                final String number1 = number.getEditText().getText().toString().trim();
                final String pwdr = passwordr.getEditText().getText().toString().trim();

                if (emailID.isEmpty()) {
                    email.setError("Please enter your emailID");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailID).matches()) {
                    email.setError("Please enter a valid email address");
                    email.requestFocus();
                } else if (name1.isEmpty()) {
                    name.setError("Please enter your name");
                    name.requestFocus();
                } else if (number1.isEmpty()) {
                    number.setError("Please enter your mobile number");
                    number.requestFocus();
                } else if (!(number1.length() >= 10)) {
                    number.setError("Mobile number should be 10 digit");
                    number.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if (!PASSWORD_PATTERN.matcher(pwd).matches()) {
                    password.setError("Password too weak");
                    password.requestFocus();
                } else if (!pwdr.equals(pwd)) {
                    passwordr.setError("Not same as previous");
                    passwordr.requestFocus();
                } else if (!(emailID.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailID).matches() && name1.isEmpty() && number1.isEmpty() && number1.length() > 10 && pwd.isEmpty() && PASSWORD_PATTERN.matcher(pwd).matches())) {
                    loadingDialog.startLoadingDialog();
                    mFirebaseAuth.createUserWithEmailAndPassword(emailID, pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Exception e=task.getException();
                                Toast.makeText(SignUp.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            } else {
                                UserHelperClass userHelperClass = new UserHelperClass(emailID, name1, number1);
                                reference.child(number1).setValue(userHelperClass);
                                startActivity(new Intent(SignUp.this, HomePage.class));
                                loadingDialog.dismissDialog();
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean amIConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_to_login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
        }
    }

}