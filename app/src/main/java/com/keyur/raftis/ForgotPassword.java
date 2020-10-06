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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout email;
    Button btn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (amIConnected()) {
        } else {
            Intent intent1 = new Intent(getApplicationContext(), NoNetwork.class);
            intent1.putExtra("activity", "forgot");
            startActivity(intent1);
            finish();
        }

        findViewById(R.id.forgot_to_login).setOnClickListener(this);

        btn=findViewById(R.id.forgot_btn);
        email=findViewById(R.id.forgot_email);

        firebaseAuth=FirebaseAuth.getInstance();


        final LoadingDialog loadingDialog=new LoadingDialog(ForgotPassword.this);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amIConnected()) {
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), NoNetwork.class);
                    intent1.putExtra("activity", "forgot");
                    startActivity(intent1);
                    finish();
                }
                String emailID=email.getEditText().getText().toString().trim();

                if(emailID.isEmpty()){
                    email.setError("Please enter your emailID");
                    email.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailID).matches()) {
                    email.setError("Please enter a valid email address");
                    email.requestFocus();
                }

                else  if(!(emailID.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailID).matches())){
                    loadingDialog.startLoadingDialog();
                    firebaseAuth.sendPasswordResetEmail(emailID).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ForgotPassword.this, "Link sent successfully", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPassword.this, "Something went wrong  "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgot_to_login:
                startActivity(new Intent(getApplicationContext(),Login.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
                break;
        }
    }

    public boolean amIConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}