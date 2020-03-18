package com.example.babyu.Login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.babyu.DiaryActivity;
import com.example.babyu.FirebaseDatabaseHelper;
import com.example.babyu.MainActivity1;
import com.example.babyu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Calendar;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_email, edit_password;
    private TextInputLayout layout_email, layout_password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Dialog myDialog;
    private TextView txtclose;
    private ImageView buttonDate;
    private Button btn_submit;
    private EditText edit_name,edit_nickname,edit_birthday;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        myDialog = new Dialog(this);

        findViewById(R.id.signup_button).setOnClickListener(this);
        findViewById(R.id.textView_login).setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_password = (EditText) findViewById(R.id.edit_password);
        layout_email = (TextInputLayout) findViewById(R.id.layout_email);
        layout_password = (TextInputLayout) findViewById(R.id.layout_password);
        layout_email.setErrorEnabled(true);
        layout_password.setErrorEnabled(true);
    }

    public void ShowPopup(){

        myDialog.setContentView(R.layout.profilepopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        btn_submit = (Button) myDialog.findViewById(R.id.btn_submit);
        edit_name=(EditText)myDialog.findViewById(R.id.edit_name);
        edit_nickname=(EditText)myDialog.findViewById(R.id.edit_nickname);
        edit_birthday=(EditText)myDialog.findViewById(R.id.edit_birthday);
        buttonDate=(ImageView)myDialog.findViewById(R.id.buttonDate);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day0fMonth = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog=new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edit_birthday.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                    }
                },year,month,day0fMonth);
                datePickerDialog.show();
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileData Profile=new ProfileData();
                Profile.setName(edit_name.getText().toString());
                Profile.setNickname(edit_nickname.getText().toString());
                Profile.setDate(edit_birthday.getText().toString());
                new FirebaseDatabaseHelper().addProfileData(Profile, new FirebaseDatabaseHelper.DataStatus_profile() {
                    @Override
                    public void DataIsLoaded(List<ProfileData> Profile, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
                Intent intent=new Intent(SignUpActivity.this, MainActivity1.class);
                startActivity(intent);
                finish();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void registerUser() {
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

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    ShowPopup();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "您已經註冊過了", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_button:
                registerUser();
                break;
            case R.id.textView_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

    }
}
