package com.keyur.raftis.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.keyur.raftis.HomePage;
import com.keyur.raftis.R;

public class ContactUs extends AppCompatActivity {
    EditText subject,message;
    Button button;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        subject=findViewById(R.id.contact_subject);
        message=findViewById(R.id.contact_message);
        button=findViewById(R.id.contact_btn);
        back=findViewById(R.id.contact_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject.setError(null);
                message.setError(null);
                String sub=subject.getText().toString().trim();
                String msg=message.getText().toString().trim();

                if(sub.isEmpty()){
                    subject.setError("Subject is empty");
                    subject.requestFocus();
                }else if(msg.isEmpty()){
                    message.setError("Message is empty");
                    message.requestFocus();
                }else if(!(sub.isEmpty() && msg.isEmpty())){
                    sendEmail();
                }
            }
        });
    }
    protected void sendEmail() {
        String to="project.keyur@gmail.com";
        Uri uri = Uri.parse("mailto:"+to)
                .buildUpon()
                .appendQueryParameter("to",to)
                .appendQueryParameter("subject", subject.getText().toString().trim())
                .appendQueryParameter("body", message.getText().toString().trim())
                .build();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(emailIntent,"Please select your favourite application"));
    }
}