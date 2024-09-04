package com.example.hotelmanager.activitys;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    Integer defaultColor;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //set mau cho status bar
        int color = ContextCompat.getColor(this, R.color.white);
        getWindow().setStatusBarColor(color);

        defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.bg_edt_login);
        setButtonColor(defaultColor);

        binding.edtEmail.addTextChangedListener(signUpTextWatcher);
        binding.edtPassword.addTextChangedListener(signUpTextWatcher);
        binding.edtConfirmPassword.addTextChangedListener(signUpTextWatcher);


        //**********init firebase auth**************
        auth = FirebaseAuth.getInstance();

        //action go to login activity
        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

        binding.btnSignup.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            String confirm_pass = binding.edtConfirmPassword.getText().toString().trim();
            checkValidEmailAndPass(email, password, confirm_pass);

        });

    }

    private void checkValidEmailAndPass(String email, String password, String confirmPass) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.progressBar.setVisibility(View.GONE);
            binding.edtEmail.setError("Vui lòng kiểm tra lại email!");
        } else if (password.length() < 6) {
            binding.progressBar.setVisibility(View.GONE);
            binding.edtPassword.setError("Mật khẩu phải >= 6 ký tự");
        } else {
            if (password.equals(confirmPass)) {
                createAccount(email, password);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private TextWatcher signUpTextWatcher = new TextWatcher() {
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
                    binding.edtPassword.getText().toString().trim().length() > 0 &&
                    binding.edtConfirmPassword.getText().toString().trim().length() > 0) {
                //thay mau cho button login
                int loginColor = ContextCompat.getColor(getApplicationContext(), R.color.bg_splash);
                setButtonColor(loginColor);
                binding.btnSignup.setEnabled(true);

            } else {
                binding.btnSignup.setEnabled(false);
                setButtonColor(defaultColor);
            }

        }
    };

    private void setButtonColor(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(25);
        binding.btnSignup.setElevation(8);
        drawable.setColor(color);
        binding.btnSignup.setBackground(drawable);
        binding.btnSignup.setPadding(10, 10, 10, 10);
    }
}