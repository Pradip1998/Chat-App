package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private TextView logintextview,logincreate;
    private EditText loginemail,loginpassword;
    private Button loginbutton ;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logintextview=(TextView)findViewById(R.id.loginextview);
        logincreate=(TextView)findViewById(R.id.logincreat);
        loginemail=(EditText)findViewById(R.id.loginemail);
        loginpassword=(EditText)findViewById(R.id.loginpassword);
        loginbutton=(Button)findViewById(R.id.loginbutton);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        fAuth= FirebaseAuth.getInstance();
        logincreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });




        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Emaill=loginemail.getText().toString().trim();
                String Passwordl=loginpassword.getText().toString().trim();

                if (TextUtils.isEmpty(Emaill)){
                    loginemail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(Passwordl)){
                    loginpassword.setError("Password is Required");
                    return;
                }
                if (Passwordl.length()<6){
                    loginpassword.setError("Password Must be More than ^ character");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Emaill).matches()){
                    loginemail.setError("Fill with Correct Email");
                }


                progressBar.setVisibility(View.VISIBLE);

                //login
                fAuth.signInWithEmailAndPassword(Emaill,Passwordl).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ChatApp.class));
                        }else {
                            Toast.makeText(Login.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



    }
}