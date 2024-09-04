package com.example.hotelmanager.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hotelmanager.R;
import com.example.hotelmanager.activitys.AddCustomerActivity;
import com.example.hotelmanager.activitys.BookingRoomActivity;
import com.example.hotelmanager.activitys.FeedBackActivity;
import com.example.hotelmanager.activitys.ListCustomerActivity;
import com.example.hotelmanager.activitys.MainActivity;
import com.example.hotelmanager.activitys.OrderRoomActivity;
import com.example.hotelmanager.adapter.BookedAdapter;
import com.example.hotelmanager.adapter.ViewpagerAdapter;
import com.example.hotelmanager.databinding.FragmentDashBoardBinding;
import com.example.hotelmanager.model.Banner;
import com.example.hotelmanager.model.Booking;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DashBoardFragment extends Fragment {

  private FragmentDashBoardBinding binding;
  private FirebaseAuth auth;
  private ViewPager2 viewPager2;
  private ViewpagerAdapter viewpagerAdapter;
  private List<Banner> mBanners;
    private int mYear, mMonth, mDay, mHours, mMinute;
    private int totalRooms = 0;
    private int countBookedRooms = 0;



    private Handler mHanler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (binding.viewpager2.getCurrentItem() == mBanners.size() - 1) {
                binding.viewpager2.setCurrentItem(0);
            } else {
                binding.viewpager2.setCurrentItem(binding.viewpager2.getCurrentItem() + 1);
            }


        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentDashBoardBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init view
        iNit(view);
        //chuyen den cac man hinh
        gotoActivityAction();

        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mHanler.removeCallbacks(mRunnable);
                mHanler.postDelayed(mRunnable, 3000);
            }
        });


        setUpListBanner();

        //Lay danh sach cac phong
        fetchAllRooms();


        //Hien thi tat ca doanh thu
        fetchAllRangeBooking();


        //Tinh doanh thu theo ngay
        caculateIncome();
    }

    private void fetchAllRooms() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            FirebaseDatabase.getInstance().getReference("Rooms").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            totalRooms = (int) snapshot.getChildrenCount();
                            //Hien thi tong so phong
                            binding.tvSumRoom.setText(String.valueOf(totalRooms));
                            caculatorEmtyRooms();
                            
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void caculatorEmtyRooms() {
        int spaceRooms = totalRooms - countBookedRooms;
        binding.tvCountSpaceRoom.setText(String.valueOf(spaceRooms));
    }

    private void fetchAllRangeBooking() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            FirebaseDatabase.getInstance().getReference("booking").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Booking> allBookingRange = new ArrayList<>();
                            for(DataSnapshot bookingSnapshot : snapshot.getChildren()){
                                Booking booking = bookingSnapshot.getValue(Booking.class);
                                if(booking != null){
                                    allBookingRange.add(booking);
                                    countBookedRooms++; // tang so phong moi khi duoc them vao
                                }
                            }
                            caculatorEmtyRooms();
                            caculatorTotal(allBookingRange);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void caculatorTotal(List<Booking> allBookings) {
        int totalRevenue = 0;
        for (Booking booking : allBookings) {
            totalRevenue += Integer.parseInt(booking.getPriceRoom()) * booking.getNumberOfDays();
        }
        // Hiển thị tổng doanh thu
        String totalRevenueFormatted = NumberFormat.getInstance().format(totalRevenue);
        binding.tvSumIncome.setText(totalRevenueFormatted + " VND");




    }

    private void caculateIncome() {
        binding.tvStartDate.setOnClickListener(v->{
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            //Hien thi dialog DatePicker
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, monthOfYear, dayOfMonth) -> {
                //Luu ngay duoc chon
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                //Hien thi ngay duoc chon
                binding.tvStartDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);

            },mYear,mMonth,mDay);
            datePickerDialog.show();
        });
        binding.tvEndDate.setOnClickListener(v->{
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            //Hien thi dialog DatePicker
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, monthOfYear, dayOfMonth) -> {
                //Luu ngay duoc chon
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                //Hien thi ngay duoc chon
                binding.tvEndDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);

            },mYear,mMonth,mDay);
            datePickerDialog.show();

        });
        binding.btnSeenIncome.setOnClickListener(v->{
            binding.loadingIncome.setVisibility(View.VISIBLE);
            String startDate = binding.tvStartDate.getText().toString().trim();
            String endDate = binding.tvEndDate.getText().toString().trim();
            if(startDate.isEmpty()){
                Snackbar.make(binding.layout4,"Vui lòng chọn ngày bắt đầu", Toast.LENGTH_SHORT).show();
                return;
            }
            if(endDate.isEmpty()){
                Snackbar.make(binding.layout4,"Vui lòng chọn ngày kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }
            fetchBookingInRange(startDate,endDate);
        });
    }

    private void fetchBookingInRange(String startDate, String endDate) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            FirebaseDatabase.getInstance().getReference("booking").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Booking> bookingInRange = new ArrayList<>();
                            for(DataSnapshot bookingSnapshot : snapshot.getChildren()){
                                Booking booking = bookingSnapshot.getValue(Booking.class);
                                if(booking != null && isBookingWithinRange(booking,startDate,endDate)){
                                    bookingInRange.add(booking);
                                }
                                //set len recyclerview
                                binding.recyclerview.setHasFixedSize(true);
                                binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
                                BookedAdapter bookedAdapter = new BookedAdapter(bookingInRange);
                                binding.recyclerview.setAdapter(bookedAdapter);
                            }
                            caculatorRevenue(bookingInRange);
                            binding.loadingIncome.setVisibility(View.GONE);
                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void caculatorRevenue(List<Booking> bookings) {
        int totalRevenue = 0;
        for(Booking booking : bookings){
            totalRevenue += Integer.parseInt(booking.getPriceRoom()) * booking.getNumberOfDays();
        }
        //HIen thi  doanh thu
        String totalRevenueFormated = NumberFormat.getInstance().format(totalRevenue);
        binding.tvSumMoney.setText(totalRevenueFormated + " VND");



    }

    private boolean isBookingWithinRange(Booking booking,String startDate, String endDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try{
            Date bookingStartDate = simpleDateFormat.parse(booking.getCheckInDatetime());
            Date bookingEndDate = simpleDateFormat.parse(booking.getCheckOutDatetime());
            Date selectedStartDate = simpleDateFormat.parse(startDate);
            Date selectedEndDate = simpleDateFormat.parse(endDate);

            return !bookingStartDate.before(selectedStartDate) && !bookingEndDate.after(selectedEndDate);

        } catch (ParseException e) {
          e.printStackTrace();
        }
        return  false;

    }

    private void setUpListBanner() {
        mBanners = new ArrayList<>();
        mBanners.add(new Banner(R.drawable.banner1));
        mBanners.add(new Banner(R.drawable.pdoi_2));
        mBanners.add(new Banner(R.drawable.pdoi_3));
        mBanners.add(new Banner(R.drawable.pdoi_4));
        mBanners.add(new Banner(R.drawable.pdoi_5));
        mBanners.add(new Banner(R.drawable.pviewcity_1));
        mBanners.add(new Banner(R.drawable.viewcity_3));



        viewpagerAdapter = new ViewpagerAdapter(mBanners);
        binding.viewpager2.setAdapter(viewpagerAdapter);
        binding.indicator3.setViewPager(binding.viewpager2);

    }

    private void gotoActivityAction() {
        binding.cAddCustomer.setOnClickListener(v->{
            startActivity(new Intent(requireActivity(), AddCustomerActivity.class));
        });
        binding.cBookRoom.setOnClickListener(v->{
            startActivity(new Intent(requireActivity(), OrderRoomActivity.class));
        });
        binding.cFeedbackCustomer.setOnClickListener(v->{
            startActivity(new Intent(requireActivity(), FeedBackActivity.class));
        });
        binding.cListCustomer.setOnClickListener(v->{
            startActivity(new Intent(requireActivity(), ListCustomerActivity.class));
        });

    }

    private void iNit(View view) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            binding.tvCurrentUser.setText(currentUser.getEmail());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mHanler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHanler.postDelayed(mRunnable, 3000);
    }

}