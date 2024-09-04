package com.example.hotelmanager.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.hotelmanager.R;
import com.example.hotelmanager.adapter.CustomerAdapter;
import com.example.hotelmanager.databinding.ActivityListCustomerBinding;
import com.example.hotelmanager.listener.ICustomerLoadListener;
import com.example.hotelmanager.model.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListCustomerActivity extends AppCompatActivity implements ICustomerLoadListener {
    private ActivityListCustomerBinding binding;
    ICustomerLoadListener customerLoadListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_splash));
        binding.btnBack.setOnClickListener(v->{
            finish();
        });

        iNit();
        loadCustomerFromFirebase();


    }

    private void loadCustomerFromFirebase() {
        List<Customer> customers = new ArrayList<>();
        FirebaseUser currentAdmin = auth.getCurrentUser();
        if(currentAdmin != null){

            FirebaseDatabase.getInstance().getReference("customer").child(currentAdmin.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot customerSnapshot : snapshot.getChildren()){
                                    HashMap<String, Object> customerData = (HashMap<String, Object>) customerSnapshot.getValue();
                                    if (customerData != null) {
                                        String customerId = customerSnapshot.getKey();
                                        String customerName = customerData.get("name_customer").toString();
                                        String customerAddress = customerData.get("address_customer").toString();
                                        String customerPhone = customerData.get("phone_customer").toString();
                                        String customerCCCD = customerData.get("cccd_customer").toString();
                                        String customerBirthday = customerData.get("birthday_customer").toString();

                                        Customer customer = new Customer(customerId, customerName, customerAddress, customerPhone, customerCCCD, customerBirthday);

                                        customers.add(customer);
                                    }
                                }
                                customerLoadListener.onCustomerLoadSuccess(customers);
                            }
                            else{
                                binding.progressBar.setVisibility(View.GONE);
                                customerLoadListener.onCustomerLoadFailed("Danh sách rỗng");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.progressBar.setVisibility(View.GONE);
                            customerLoadListener.onCustomerLoadFailed(error.getMessage());

                        }
                    });
        }

    }

    private void iNit() {
        binding.progressBar.setVisibility(View.VISIBLE);
        customerLoadListener = this;
        auth = FirebaseAuth.getInstance();
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onCustomerLoadSuccess(List<Customer> customerList) {
        binding.progressBar.setVisibility(View.GONE);
        CustomerAdapter customerAdapter = new CustomerAdapter(customerList,ListCustomerActivity.this);
        binding.recyclerview.setAdapter(customerAdapter);

    }

    @Override
    public void onCustomerLoadFailed(String message) {
        binding.progressBar.setVisibility(View.GONE);
        Log.e("LOAD_CUSTOMER",message);

    }
}