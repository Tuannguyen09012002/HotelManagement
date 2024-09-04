package com.example.hotelmanager.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityChangePasswordBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg_splash));

        auth = FirebaseAuth.getInstance();
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        binding.btnUpdatePass.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            String currentPass = binding.edtCurrentPass.getText().toString().trim();
            String newPass = binding.edtNewpassword.getText().toString().trim();
            String confirmNewPass = binding.edtConfirmPassword.getText().toString().trim();
            if (TextUtils.isEmpty(currentPass)) {
                binding.progressBar.setVisibility(View.GONE);
                binding.edtCurrentPass.setError("Vui lòng điền mật khẩu cũ");
                return;
            }
            if (TextUtils.isEmpty(newPass)) {
                binding.progressBar.setVisibility(View.GONE);
                binding.edtNewpassword.setError("Vui lòng điền mật khẩu mới");
                return;
            }
            if (TextUtils.isEmpty(confirmNewPass)) {
                binding.progressBar.setVisibility(View.GONE);
                binding.edtConfirmPassword.setError("Vui lòng xác nhận mật khẩu mới");
                return;
            }
            if (newPass.equals(confirmNewPass)) {
                updatePassWord(currentPass, newPass, confirmNewPass);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.layoutMain, "Vui lòng xác nhận lại mật khẩu mới.", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void updatePassWord(String currentPass, String newPass, String confirmNewPass) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            auth.signInWithEmailAndPassword(user.getEmail(), currentPass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            user.updatePassword(newPass)
                                    .addOnCompleteListener(passwordTask -> {
                                        if (passwordTask.isSuccessful()) {
                                            binding.progressBar.setVisibility(View.GONE);
                                            Snackbar.make(binding.layoutMain, "Đổi mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            binding.progressBar.setVisibility(View.GONE);
                                            Snackbar.make(binding.layoutMain, "Đổi mật khẩu thất bại. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                        }

                                    });
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Snackbar.make(binding.layoutMain, "Mật khẩu cũ không đúng. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                        }

                    });
        } else {
            binding.progressBar.setVisibility(View.GONE);
            Snackbar.make(binding.layoutMain, "Bạn chưa đăng nhập!.", Toast.LENGTH_SHORT).show();
        }
    }
}