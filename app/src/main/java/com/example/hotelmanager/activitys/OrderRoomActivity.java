package com.example.hotelmanager.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hotelmanager.R;
import com.example.hotelmanager.adapter.ListRoomAdapter;
import com.example.hotelmanager.databinding.ActivityOrderRoomBinding;
import com.example.hotelmanager.listener.IRecyclerView;
import com.example.hotelmanager.listener.IRoomLoadListener;
import com.example.hotelmanager.model.Booking;
import com.example.hotelmanager.model.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderRoomActivity extends AppCompatActivity implements IRoomLoadListener {
    private ActivityOrderRoomBinding binding;
    IRoomLoadListener roomLoadListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_splash));

        binding.btnBack.setOnClickListener(v->{
            finish();
        });

        iNit();
        loadListRoomFromFirebase();
    }

    private void loadListRoomFromFirebase() {
        FirebaseUser currentUser = auth.getCurrentUser();
        List<Room> rooms = new ArrayList<>();
        if(currentUser != null){
            FirebaseDatabase.getInstance().getReference("Rooms").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot roomSnapshot : snapshot.getChildren()){
                                    Room room = roomSnapshot.getValue(Room.class);
                                    rooms.add(room);

                                }
                                roomLoadListener.onRoomLoadSuccess(rooms);
                            }
                            else{
                                binding.progressBar.setVisibility(View.GONE);
                                roomLoadListener.onRoomLoadFailed("Không tồn tại phòng!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.progressBar.setVisibility(View.GONE);
                            roomLoadListener.onRoomLoadFailed(error.getMessage());

                        }
                    });

        }
    }

    private void iNit() {
        roomLoadListener = this;
        auth = FirebaseAuth.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRoomLoadSuccess(List<Room> roomList) {
        binding.progressBar.setVisibility(View.GONE);
        ListRoomAdapter listRoomAdapter = new ListRoomAdapter(roomList, OrderRoomActivity.this, new IRecyclerView() {
            @Override
            public void onItemClick(Room room) {
           Intent intent = new Intent(OrderRoomActivity.this, BookingRoomActivity.class);
              intent.putExtra("roomName",room.getNameRoom());
              intent.putExtra("roomPrice",room.getPriceRoom());
              intent.putExtra("typeRoom",room.getRoomType());
              startActivity(intent);

            }
        });
        binding.recyclerview.setAdapter(listRoomAdapter);

    }

    @Override
    public void onRoomLoadFailed(String message) {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(OrderRoomActivity.this,message,Toast.LENGTH_SHORT).show();

    }
}