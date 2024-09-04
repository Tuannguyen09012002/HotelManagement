package com.example.hotelmanager.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityAddCustomerBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class AddCustomerActivity extends AppCompatActivity {
    private ActivityAddCustomerBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_splash));

        iNit();
        //back btn
        binding.btnBack.setOnClickListener(v->{
            finish();
        });


        binding.btnAddCustomer.setOnClickListener(v->{
            binding.progressBar.setVisibility(View.VISIBLE);
            uploadInfoCustomerToFirebase();
        });
    }

    private void iNit() {
        auth = FirebaseAuth.getInstance();
    }
    private void clearInfomation(){
        binding.edtNameCustomer.setText("");
        binding.edtAddressCustomer.setText("");
        binding.edtPhoneCustomer.setText("");
        binding.edtCccdCustomer.setText("");
        binding.edtBirthdayCustomer.setText("");

    }

    private void uploadInfoCustomerToFirebase() {
       String nameCustomer = binding.edtNameCustomer.getText().toString().trim();
       String addressCustomer = binding.edtAddressCustomer.getText().toString().trim();
       String phoneCustomer = binding.edtPhoneCustomer.getText().toString().trim();
       String CCCD_CMND = binding.edtCccdCustomer.getText().toString().trim();
       String birthdayCustomer = binding.edtBirthdayCustomer.getText().toString().trim();
       if(TextUtils.isEmpty(nameCustomer)){
           binding.progressBar.setVisibility(View.GONE);
           Snackbar snackbar = Snackbar.make(binding.layoutMain, "Vui lòng nhập họ tên!", Snackbar.LENGTH_SHORT);
           snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
           snackbar.show();
           return;
       }
        if(TextUtils.isEmpty(addressCustomer)){
            binding.progressBar.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(binding.layoutMain, "Vui lòng nhập địa chỉ!", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
            snackbar.show();
            return;
        }
        if(TextUtils.isEmpty(phoneCustomer)){
            binding.progressBar.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(binding.layoutMain, "Vui lòng nhập số điện thoại!", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
            snackbar.show();
            return;
        }
        if(TextUtils.isEmpty(CCCD_CMND)){
            binding.progressBar.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(binding.layoutMain, "CCCD/CMND là thông tin bắt buộc!", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
            snackbar.show();
            return;
        }
        if(TextUtils.isEmpty(birthdayCustomer)){
            binding.progressBar.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(binding.layoutMain, "Vui lòng nhập ngày tháng năm sinh!", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
            snackbar.show();
            return;
        }

        FirebaseUser currentAdmin = auth.getCurrentUser();
       if(currentAdmin != null){
           DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("customer").child(currentAdmin.getUid());
           myRef.child(CCCD_CMND)
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(snapshot.exists()){
                               // Khách hàng đã tồn tại trong cơ sở dữ liệu
                               binding.progressBar.setVisibility(View.GONE);
                               Snackbar snackbar = Snackbar.make(binding.layoutMain, "Thông tin khách hàng đã tồn tại", Snackbar.LENGTH_SHORT);
                               snackbar.setBackgroundTint(ContextCompat.getColor(AddCustomerActivity.this, R.color.red_error));
                               snackbar.show();

                           }
                           else{
                               HashMap<String,Object> hashMap = new HashMap<>();
                               hashMap.put("name_customer",nameCustomer);
                               hashMap.put("address_customer",addressCustomer);
                               hashMap.put("phone_customer",phoneCustomer);
                               hashMap.put("cccd_customer",CCCD_CMND);
                               hashMap.put("birthday_customer",birthdayCustomer);
                               myRef.child(CCCD_CMND).setValue(hashMap)
                                       .addOnSuccessListener(task->{
                                           binding.progressBar.setVisibility(View.GONE);
                                           clearInfomation();
                                           Snackbar snackbar = Snackbar.make(binding.layoutMain, "Thêm thông tin khách hàng thành công", Snackbar.LENGTH_SHORT);
                                           snackbar.setBackgroundTint(ContextCompat.getColor(AddCustomerActivity.this, R.color.color_success));
                                           snackbar.show();


                                       })
                                       .addOnFailureListener(e->{
                                           binding.progressBar.setVisibility(View.GONE);
                                           Log.e("ADD_CUSTOMER", "");
                                           Snackbar snackbar = Snackbar.make(binding.layoutMain, "Thêm thông tin khách hàng thất bại", Snackbar.LENGTH_SHORT);
                                           snackbar.setBackgroundTint(ContextCompat.getColor(AddCustomerActivity.this, R.color.red_error));
                                           snackbar.show();
                                       });

                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           binding.progressBar.setVisibility(View.GONE);

                       }
                   });
       }



    }
}