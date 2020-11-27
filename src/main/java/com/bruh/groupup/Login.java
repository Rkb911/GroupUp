package com.bruh.groupup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText textEmail,textPassword;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference reference;
    Button loginButton;
    TextView forgotPass,signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InitializeFields();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null)
        {
            SendToMain();
        }
    }

    private void SendToMain()
    {
        Intent i=new Intent(Login.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void InitializeFields() {
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        textEmail=(EditText)findViewById(R.id.textEmailAddress);
        textPassword=(EditText)findViewById(R.id.textPassword);
        progressBar=(ProgressBar)findViewById(R.id.progressBarLogin);
        reference= FirebaseDatabase.getInstance().getReference().child("User");
        loginButton=(Button)findViewById(R.id.login_button);
        forgotPass=(TextView)findViewById(R.id.forgot_password);
        signUp=(TextView)findViewById(R.id.sign_up);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser(view);
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPassword(view);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToRegister(view);
            }
        });
    }



    public void LoginUser(View v)
    {

                progressBar.setVisibility(View.VISIBLE);

                String email =textEmail.getText().toString();
                String password=textPassword.getText().toString();
                if(!email.equals("")&& !password.equals(""))
                {
                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()  {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(Login.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Wrong Email or Password",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }

            }



    public void GoToRegister(View v)
    {
        Intent i=new Intent(Login.this,SignUp.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }
    public void ForgotPassword(View v)
    {

                AlertDialog.Builder alert =new AlertDialog.Builder(Login.this);
                LinearLayout container = new LinearLayout(Login.this);
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams ip =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                ip.setMargins(50,0,0,100);

                final EditText input = new EditText(Login.this);

                input.setLayoutParams(ip);
                input.setGravity(Gravity.TOP|Gravity.START);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.setLines(1);
                input.setMaxLines(1);

                container.addView(input,ip);

                alert.setMessage("Enter your Registered Email Address");
                alert.setTitle("Forgot Password");
                alert.setView(container);

                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String enteredEmail=input.getText().toString();

                        auth.sendPasswordResetEmail(enteredEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            dialogInterface.dismiss();
                                            Toast.makeText(getApplicationContext(),"Email Sent",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                });



    }
}
