package com.example.babyu.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.babyu.DiaryActivity;
import com.example.babyu.MainActivity1;
import com.example.babyu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText edit_email, edit_password;
    private TextInputLayout layout_email, layout_password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.textView_signup).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_password = (EditText) findViewById(R.id.edit_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layout_email = (TextInputLayout) findViewById(R.id.layout_email);
        layout_password = (TextInputLayout) findViewById(R.id.layout_password);
        layout_email.setErrorEnabled(true);
        layout_password.setErrorEnabled(true);
    }

    private void UserLogin() {
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();

        if (email.isEmpty()) {
            layout_email.setError("請輸入帳號");
            layout_password.setError("");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layout_email.setError("請輸入有效的帳號");
            layout_password.setError("");
            return;
        }

        if (password.isEmpty()) {
            layout_password.setError("請輸入密碼");
            layout_email.setError("");
            return;
        }
        layout_email.setError("");
        layout_password.setError("");

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent intent=new Intent(LoginActivity.this, MainActivity1.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_signup:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.login_button:
                UserLogin();
                break;
        }

    }

}
