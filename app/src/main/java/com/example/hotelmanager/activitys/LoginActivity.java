package com.example.hotelmanager.activitys;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Integer defaultColor;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int color = ContextCompat.getColor(this, R.color.white);
        getWindow().setStatusBarColor(color);
        defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.bg_edt_login);
        setButtonColor(defaultColor);

        binding.edtEmail.addTextChangedListener(loginTextWatcher);
        binding.edtPassword.addTextChangedListener(loginTextWatcher);


        //**********init firebase auth
        auth = FirebaseAuth.getInstance();

        binding.tvForgotpass.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
            finish();
        });

        //action go to signup activity
        binding.tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        binding.btnLogin.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                //no thing code
                binding.progressBar.setVisibility(View.GONE);
            } else {
                loginAccount(email, password);
            }

        });

    }

    private void loginAccount(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar.make(binding.layoutMain, "Đăng nhập thành công", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.color_success));
                        snackbar.show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar.make(binding.layoutMain, "Đăng nhập thất bại!", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
                        snackbar.show();
                    }
                });
    }

    //Kiem tra nguoi dung nhap text
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //kiem tra nguoi dung co nhap khong
            if (binding.edtEmail.getText().toString().trim().length() > 0 &&
                    binding.edtPassword.getText().toString().trim().length() > 0) {
                //thay mau cho button login
                int loginColor = ContextCompat.getColor(getApplicationContext(), R.color.bg_splash);
                setButtonColor(loginColor);

            } else {
                setButtonColor(defaultColor);
            }

        }
    };

    private void setButtonColor(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(25);
        binding.btnLogin.setElevation(8);
        drawable.setColor(color);
        binding.btnLogin.setBackground(drawable);
        binding.btnLogin.setPadding(10, 10, 10, 10);
    }
}