package com.example.hotelmanager.activitys;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityMainBinding;
import com.example.hotelmanager.fragments.AccountFragment;
import com.example.hotelmanager.fragments.AddFragment;
import com.example.hotelmanager.fragments.BookingFragment;
import com.example.hotelmanager.fragments.DashBoardFragment;
import com.example.hotelmanager.fragments.RoomFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Thiet lap mau cho thanh status bar
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_splash));

        navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        binding.bottomNavigationView.setOnItemSelectedListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String fragmentToLoad = extras.getString("fragmentToLoad");
            String fragmentToLoadBooked = extras.getString("fragmentToLoadBooked");
            if (fragmentToLoad != null && fragmentToLoad.equals("roomFragment")){
                navController.navigate(R.id.roomFragment);

            }
            if(fragmentToLoadBooked != null && fragmentToLoadBooked.equals("bookedFragment")){
                navController.navigate(R.id.bookingFragment);
            }

        }

    }

    @Override
    public void onBackPressed() {
        if(!navController.popBackStack()){
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.dashBoardFragment){
           navController.navigate(R.id.dashBoardFragment);
            return true;
        }
        else if(item.getItemId() == R.id.bookingFragment){
            navController.navigate(R.id.bookingFragment);
            return true;
        }
        else if(item.getItemId() == R.id.addFragment){
            navController.navigate(R.id.addFragment);
            return true;
        }
        else if(item.getItemId() == R.id.roomFragment){
            navController.navigate(R.id.roomFragment);
            return true;
        }
        else if(item.getItemId() == R.id.accountFragment){
            navController.navigate(R.id.accountFragment);
            return true;
        }

        return false;
    }
}