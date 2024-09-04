package com.example.hotelmanager.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityUpdateInfoBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateInfoActivity extends AppCompatActivity {
    private ActivityUpdateInfoBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_splash));
        //init
        iNit();
        binding.btnUpdateinfo.setOnClickListener(v->{
            binding.progressBar.setVisibility(View.VISIBLE);
            updateInfoToFirebase();
        });
        binding.btnBack.setOnClickListener(v->{
            finish();
        });





    }
    private void clearEditText(){
        binding.edtName.setText("");
        binding.edtPhone.setText("");
        binding.edtLinkZalo.setText("");
    }

    private void updateInfoToFirebase() {
        String name_hotel = binding.edtName.getText().toString().trim();
        String phone_number = binding.edtPhone.getText().toString().trim();
        String linkContact = binding.edtLinkZalo.getText().toString().trim();
        if(TextUtils.isEmpty(name_hotel)){
            binding.progressBar.setVisibility(View.GONE);
            binding.edtName.setError("Vui lòng nhập tên khách sạn");
            return;
        }
        if(TextUtils.isEmpty(phone_number)){
            binding.progressBar.setVisibility(View.GONE);
            binding.edtPhone.setError("Vui lòng nhập số điện thoại");
            return;
        }
      DatabaseReference myRef =  FirebaseDatabase.getInstance().getReference("admin");
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            myRef.child(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                //neu ton tai roi thi update info
                                HashMap<String,Object> hasMap = new HashMap<>();
                                hasMap.put("nameHotel",name_hotel);
                                hasMap.put("phone",phone_number);
                                hasMap.put("link_contact",linkContact);
                                myRef.child(user.getUid()).updateChildren(hasMap)
                                        .addOnSuccessListener(task ->{
                                            clearEditText();
                                            binding.progressBar.setVisibility(View.GONE);
                                            Snackbar snackbar = Snackbar.make(binding.mainLayout, "Cập nhật thông tin thành công", Snackbar.LENGTH_SHORT);
                                            snackbar.setBackgroundTint(ContextCompat.getColor(UpdateInfoActivity.this, R.color.color_success));
                                            snackbar.show();
                                        })
                                        .addOnFailureListener(e->{
                                            binding.progressBar.setVisibility(View.GONE);
                                            Snackbar snackbar = Snackbar.make(binding.mainLayout, "Cập nhật thông tin thất bại", Snackbar.LENGTH_SHORT);
                                            snackbar.setBackgroundTint(ContextCompat.getColor(UpdateInfoActivity.this, R.color.red_error));
                                            snackbar.show();
                                        });

                            }
                            else{
                                //chua ton tai them moi thong tin admin
                                HashMap<String,Object> newHasMap = new HashMap<>();
                                newHasMap.put("nameHotel",name_hotel);
                                newHasMap.put("phone",phone_number);
                                newHasMap.put("link_contact",linkContact);
                                myRef.child(user.getUid()).setValue(newHasMap)
                                        .addOnSuccessListener(newTask ->{
                                            clearEditText();
                                            binding.progressBar.setVisibility(View.GONE);
                                            Snackbar snackbar = Snackbar.make(binding.mainLayout, "Thông tin đã được lưu", Snackbar.LENGTH_SHORT);
                                            snackbar.setBackgroundTint(ContextCompat.getColor(UpdateInfoActivity.this, R.color.color_success));
                                            snackbar.show();
                                        })
                                        .addOnFailureListener(e->{
                                            binding.progressBar.setVisibility(View.GONE);
                                            Snackbar snackbar = Snackbar.make(binding.mainLayout, "Thêm thông tin thất bại!", Snackbar.LENGTH_SHORT);
                                            snackbar.setBackgroundTint(ContextCompat.getColor(UpdateInfoActivity.this, R.color.red_error));
                                            snackbar.show();

                                        });



                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.progressBar.setVisibility(View.GONE);
                            Log.e("ERROR",error.getMessage());

                        }
                    });
        }
        else{
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(UpdateInfoActivity.this,"Bạn chưa đăng nhập!",Toast.LENGTH_SHORT).show();
        }



    }

    private void iNit() {
        auth = FirebaseAuth.getInstance();

    }
}