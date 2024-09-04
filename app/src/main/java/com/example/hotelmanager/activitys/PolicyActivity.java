package com.example.hotelmanager.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityPolicyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PolicyActivity extends AppCompatActivity {
    private ActivityPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_splash));
        binding.progressBar.setVisibility(View.VISIBLE);


        getDataPolicy();
        binding.btnBack.setOnClickListener(v->{
            finish();
        });


    }

    private void getDataPolicy() {
        FirebaseDatabase.getInstance().getReference("policy")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.progressBar.setVisibility(View.GONE);
                            String policy_text = snapshot.child("text").getValue(String.class);
                            binding.tvPolicy.setText(policy_text);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(PolicyActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }
}