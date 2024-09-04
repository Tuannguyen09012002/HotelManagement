package com.example.hotelmanager.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.hotelmanager.R;
import com.example.hotelmanager.activitys.ChangePasswordActivity;
import com.example.hotelmanager.activitys.LoginActivity;
import com.example.hotelmanager.activitys.PolicyActivity;
import com.example.hotelmanager.activitys.UpdateInfoActivity;
import com.example.hotelmanager.adapter.ListViewAdapter;
import com.example.hotelmanager.databinding.FragmentAccountBinding;
import com.example.hotelmanager.model.ListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AccountFragment extends Fragment {
private FragmentAccountBinding binding;
ListViewAdapter listViewAdapter;
ArrayList<ListViewModel> listViewModels = new ArrayList<>();
ListViewModel listViewModel;
FirebaseAuth auth;
NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentAccountBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iNit(view);

        //getInfo
        fetchDataInFo();

        int[] images = {R.drawable.img_10,R.drawable.img_11,R.drawable.img_12,R.drawable.baseline_logout_24};
        String[] labels ={"Đổi mật khẩu mới","Cập nhật thông tin","Điều khoản & chính sách","Đăng xuất"};

        listViewModels.clear();

        for(int i = 0;i<images.length;i++){
            listViewModel = new ListViewModel(images[i],labels[i]);
            listViewModels.add(listViewModel);
        }
        listViewAdapter = new ListViewAdapter(requireContext(),listViewModels);
        binding.listview.setAdapter(listViewAdapter);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        Intent changePass = new Intent(requireContext(), ChangePasswordActivity.class);
                        startActivity(changePass);
                        break;
                    case 1:
                        Intent updateInfo = new Intent(requireContext(), UpdateInfoActivity.class);
                        startActivity(updateInfo);
                        break;
                    case 2:
                        Intent policy = new Intent(requireContext(), PolicyActivity.class);
                        startActivity(policy);
                        break;
                    case 3:
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setMessage("Bạn có muốn đăng xuất?");
                        builder.setPositiveButton("Có",(dialog,which)->{
                            auth.signOut();
                            startActivity(new Intent(requireContext(), LoginActivity.class));
                            requireActivity().finish();
                            dialog.dismiss();


                        });
                        builder.setNegativeButton("Không",(dialog,which)->{
                            dialog.dismiss();
                        });
                        builder.create().show();
                        break;

                }
            }
        });

        //********************Action btn back*****************
        binding.btnBack.setOnClickListener(v->{
            navController.navigateUp();
        });
    }

    private void fetchDataInFo() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            FirebaseDatabase.getInstance().getReference("admin")
                    .child(currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String nameHotel = snapshot.child("nameHotel").getValue(String.class);
                                binding.tvNameHotel.setText(nameHotel);
                                binding.tvEmail.setText(currentUser.getEmail());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(requireContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
        }


    }

    private void iNit(View view) {
        auth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
    }
}