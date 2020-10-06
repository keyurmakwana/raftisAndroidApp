package com.keyur.raftis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout email, password;
    Button btn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 5;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (amIConnected()) {
        } else {
            Intent intent1 = new Intent(getApplicationContext(), NoNetwork.class);
            intent1.putExtra("activity", "login");
            startActivity(intent1);
            finish();
        }
        findViewById(R.id.login_to_signup).setOnClickListener(this);
        findViewById(R.id.login_to_forgot_password).setOnClickListener(this);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        btn = findViewById(R.id.login_btn);
        mFirebaseAuth = FirebaseAuth.getInstance();
        final LoadingDialog loadingDialog = new LoadingDialog(Login.this);
        signInButton = findViewById(R.id.google_signin);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Intent i = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(i);
                    finish();
                } else {
                }
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amIConnected()) {
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), NoNetwork.class);
                    intent1.putExtra("activity", "login");
                    startActivity(intent1);
                    finish();
                }
                String emailID = email.getEditText().getText().toString();
                String pwd = password.getEditText().getText().toString();
                email.setError(null);
                password.setError(null);
                if (emailID.isEmpty()) {
                    email.setError("Email field is empty");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailID).matches()) {
                    email.setError("Please enter a valid email address");
                } else if (pwd.isEmpty()) {
                    password.setError("Password field is empty");
                    password.requestFocus();
                } else if (!(emailID.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailID).matches() && pwd.isEmpty())) {
                    loadingDialog.startLoadingDialog();
                    mFirebaseAuth.signInWithEmailAndPassword(emailID, pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Exception e=task.getException();
                                Toast.makeText(Login.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            } else {
                                Intent intToHome = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(Login.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            Toast.makeText(Login.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //check if the account is null
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mFirebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        } else {

        }
    }

    private void updateUI(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            //UserHelperClass userHelperClass = new UserHelperClass(personEmail, personName, number1);
            //reference.child(number1).setValue(userHelperClass);


            Toast.makeText(Login.this, personName + personEmail, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_to_signup:
                startActivity(new Intent(getApplicationContext(), SignUp.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.login_to_forgot_password:
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
        }
    }

    public boolean amIConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}