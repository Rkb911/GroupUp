package com.bruh.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bruh.groupup.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    EditText textEmail,textPassword,textName;
    ProgressBar progressBar;
    DatabaseReference reference;
    Button signUpButton;
    TextView login;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textEmail=(EditText)findViewById(R.id.textEmailAddressRegister);
        textPassword=(EditText)findViewById(R.id.textPasswordRegister);
        progressBar=(ProgressBar)findViewById(R.id.progressBarRegister);
        textName=(EditText)findViewById(R.id.nameRegister);
        reference= FirebaseDatabase.getInstance().getReference().child("User");
        auth=FirebaseAuth.getInstance();
        login=(TextView)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToLogin(view);
            }
        });
        signUpButton =(Button)findViewById(R.id.SignUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser(view);
            }
        });
    }




    private void RegisterUser(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        String email = textEmail.getText().toString();
        String name= textName.getText().toString();
        String password = textPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
           progressBar.setVisibility(View.VISIBLE);

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {

                                String currentUserId=auth.getCurrentUser().getUid();
                                reference.child("Users").child(currentUserId).setValue("");
                                

                                FirebaseUser firebaseUser=auth.getCurrentUser();
                                User u=new User();
                                u.setName(name);
                                u.setEmail(email);

                                SendToMain(view);
                                Toast.makeText(SignUp.this, "Account Created Successfully...", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(SignUp.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }



    public void SendToMain(View v)
    {
        Intent i=new Intent(SignUp.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }
    public void GoToLogin(View v)
    {
        Intent i=new Intent(SignUp.this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}