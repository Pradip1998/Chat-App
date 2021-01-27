package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText email,fullname,password,phone;
    TextView auth,acc,log;
    Button btn;
    ProgressBar progressbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fullname=(EditText)findViewById(R.id.fullname);
        email=(EditText)findViewById(R.id.emailreg);
        password=(EditText)findViewById(R.id.passwordreg);
        phone=(EditText) findViewById(R.id.phone);
        btn=(Button) findViewById(R.id.Register);
        auth=(TextView)findViewById(R.id.name12);
        acc=(TextView)findViewById(R.id.pbtn);
        log=(TextView)findViewById(R.id.textview2);

        fStore= FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        progressbar=(ProgressBar)findViewById(R.id.progressBar);
        if (fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),ChatApp.class));
            finish();
        }

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final    String  Email=email.getText().toString().trim();
                final   String FullName=fullname.getText().toString().trim();
                String Password=password.getText().toString().trim();
                final String Phone=phone.getText().toString().trim();


                if (TextUtils.isEmpty(Email)){
                    email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(FullName)){
                    fullname.setError("Name is required");
                    return;
                }
                if (TextUtils.isEmpty(Password)){
                    password.setError("Enter the password");
                    return;
                }
                if (TextUtils.isEmpty(Phone)){
                    phone.setError("Enter the Phone Number");
                    return;
                }
                if (Password.length()<6){
                    password.setError("Password Must be Grater then 6 Digit");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    email.setError("Fill the right Email");
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

                //Regitering

                fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "User Creater Successfully", Toast.LENGTH_SHORT).show();
                            userId=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("users").document(userId);
                            Map<String,Object> user=new HashMap<>();
                            user.put("Name", FullName);
                            user.put("Email",Email);
                            user.put("Phone",Phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess:user profile is created for" +userId);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+e.toString());

                                }
                            });


                            startActivity(new Intent(getApplicationContext(),Image.class));
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });



    }
}