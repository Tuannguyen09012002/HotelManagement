package com.example.hotelmanager.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hotelmanager.R;
import com.example.hotelmanager.adapter.BookedAdapter;
import com.example.hotelmanager.databinding.FragmentBookingBinding;
import com.example.hotelmanager.model.Booking;
import com.example.hotelmanager.others.Configs;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingFragment extends Fragment {
    private FragmentBookingBinding binding;
    private BookedAdapter bookedAdapter;
    private ArrayList<Booking> mBookedList;
    private FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentBookingBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init
        iNit();

        loadDataBookedFromFirebase();
        //Xoa toan bo danh sach
        binding.actionDeleteList.setOnClickListener(v->{
            FirebaseUser currentUser = auth.getCurrentUser();
               if(currentUser != null){
                   if(mBookedList.size() >0){
                       AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                       builder.setMessage("Bạn có chắc muốn xóa toàn bộ danh sách ?");
                       builder.setPositiveButton("Có",(dialog,which)->{
                           FirebaseDatabase.getInstance().getReference("booking").child(currentUser.getUid())
                                   .removeValue();
                           bookedAdapter.refreshList(mBookedList);
                           Snackbar.make(binding.mainLayout,"Đã xóa toàn bộ danh sách.", Toast.LENGTH_SHORT).show();
                           dialog.dismiss();

                       });
                       builder.setNegativeButton("Hủy",(dialog,which)->{
                           dialog.dismiss();
                       });
                       AlertDialog alertDialog = builder.create();
                       alertDialog.show();
                   }
                   else{
                       Snackbar.make(binding.mainLayout,"Danh sách rỗng", Toast.LENGTH_SHORT).show();
                   }

               }
        });





    }

    private void loadDataBookedFromFirebase() {
        mBookedList = new ArrayList<>();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            DatabaseReference bookedRef = FirebaseDatabase.getInstance().getReference("booking")
                    .child(currentUser.getUid());
            bookedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mBookedList.clear();
                    if(snapshot.exists()){
                        for(DataSnapshot bookedSnapshot : snapshot.getChildren()){
                            Booking booked = bookedSnapshot.getValue(Booking.class);
                            if(booked != null){
                                mBookedList.add(booked);
                            }

                        }
                        if(mBookedList.size()>0){
                            bookedAdapter = new BookedAdapter(mBookedList);
                            binding.progressBar.setVisibility(View.GONE);
                            binding.recyclerview.setAdapter(bookedAdapter);

                        }
                        else{
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }
                    else{
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e("Logg",error.getMessage());

                }
            });
        }

    }

    private void iNit() {
        auth = FirebaseAuth.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

}