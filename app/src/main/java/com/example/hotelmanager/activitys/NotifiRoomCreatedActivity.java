package com.example.hotelmanager.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityNotifiRoomCreatedBinding;

public class NotifiRoomCreatedActivity extends BaseActivity {
    private ActivityNotifiRoomCreatedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifiRoomCreatedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnViewRoom.setOnClickListener(v->{
        Intent intent =    new Intent(NotifiRoomCreatedActivity.this, MainActivity.class);
            intent.putExtra("fragmentToLoad", "roomFragment");
            startActivity(intent);

        });
    }
}