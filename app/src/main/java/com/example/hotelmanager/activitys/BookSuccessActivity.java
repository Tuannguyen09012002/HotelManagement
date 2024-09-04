package com.example.hotelmanager.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityBookSuccessBinding;

public class BookSuccessActivity extends BaseActivity {
    private ActivityBookSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnViewBooking.setOnClickListener(v->{
            Intent intent =    new Intent(BookSuccessActivity.this, MainActivity.class);
            intent.putExtra("fragmentToLoadBooked", "bookedFragment");
            startActivity(intent);
        });
    }
}