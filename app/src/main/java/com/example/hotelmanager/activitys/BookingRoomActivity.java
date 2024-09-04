package com.example.hotelmanager.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.ActivityBookingRoomBinding;
import com.example.hotelmanager.listener.IBookingStatusListener;
import com.example.hotelmanager.model.Booking;
import com.example.hotelmanager.model.Room;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BookingRoomActivity extends AppCompatActivity implements IBookingStatusListener {
    private ActivityBookingRoomBinding binding;
    private FirebaseAuth auth;
    String roomName,roomPrice,roomType;
    Boolean isCheckPayment;
    List<Room> mList;
    IBookingStatusListener bookingStatusListener;
    private int mYear, mMonth, mDay, mHours, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_splash));

        iNit();
        //Nhan thong tin
        Intent intent = getIntent();
        if(intent != null){
             roomName = intent.getStringExtra("roomName");
             roomPrice = intent.getStringExtra("roomPrice");
           roomType = intent.getStringExtra("typeRoom");

            //Hien thi thong tin

            binding.tvNameRoom.setText(roomName);
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            assert roomPrice != null;
            Double price = Double.parseDouble(roomPrice);
            binding.tvPriceRoom.setText(new StringBuffer(decimalFormat.format(price)).append(" VND"));
            binding.tvTypeRoom.setText(roomType);

        }
        //action button
        binding.btnCancel.setOnClickListener(v->{
            finish();
        });
        binding.btnBack.setOnClickListener(v->{
            finish();
        });
        //**********Action Booking room****************//
        binding.btnConfirmBooking.setOnClickListener(v->{
            String nameCustomer = binding.tvNameCustomer.getText().toString().trim();
            String cccdCustomer = binding.tvCccdCustomer.getText().toString().trim();
            String phoneCustomer = binding.tvPhoneCustomer.getText().toString().trim();
             isCheckPayment = binding.ischeckPayment.isChecked();
            String dateIn = binding.edtDateIn.getText().toString().trim();
            String dateOut = binding.edtDateOut.getText().toString().trim();
            if(dateOut.equals(dateIn)){
                Snackbar snackbar = Snackbar.make(binding.mainLayout, "Ngày trả không được trùng với ngày đặt!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
                snackbar.show();
                return;
            }

            if(TextUtils.isEmpty(nameCustomer)){
                Snackbar snackbar = Snackbar.make(binding.mainLayout, "Vui lòng nhập tên khách hàng!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
                snackbar.show();
                return;
            }
            if(TextUtils.isEmpty(cccdCustomer)){
                Snackbar snackbar = Snackbar.make(binding.mainLayout, "Vui lòng nhập CCCD/CMND khách hàng!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
                snackbar.show();
                return;
            }
            if(TextUtils.isEmpty(phoneCustomer)){
                Snackbar snackbar = Snackbar.make(binding.mainLayout, "Vui lòng nhập số điện thoại khách hàng!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
                snackbar.show();
                return;
            }
            if(dateIn.isEmpty()){
                Snackbar snackbar = Snackbar.make(binding.mainLayout, "Chưa chọn ngày đặt phòng!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
                snackbar.show();
                return;

            }
            if(dateOut.isEmpty()){
                Snackbar snackbar = Snackbar.make(binding.mainLayout, "Chưa chọn ngày trả phòng!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
                snackbar.show();
                return;
            }


                Booking booking = new Booking();
                booking.setBookId(cccdCustomer);
                booking.setNameCustomer(nameCustomer);
                booking.setCCCD(cccdCustomer);
                booking.setPhoneCustomer(phoneCustomer);
                booking.setNameRoom(roomName);
                booking.setPriceRoom(roomPrice);
                booking.setTypeRoom(roomType);
                booking.setCheckInDatetime(dateIn);
                booking.setCheckOutDatetime(dateOut);
                booking.setPayMentChecked(isCheckPayment);

                //Luu ngay het han cho moi phong
            long numberOfDayRent = booking.calculateNumberOfDays();
            booking.setNumberOfDays(numberOfDayRent);
            long soNgayConLai = booking.calculateSoNgayConLai();
            booking.setSoNgayConLai(soNgayConLai+1);





                FirebaseUser currentAdmin = auth.getCurrentUser();
                if(currentAdmin != null){
                    FirebaseDatabase.getInstance().getReference("booking")
                            .child(currentAdmin.getUid())
                            .child(cccdCustomer)
                            .setValue(booking)
                            .addOnSuccessListener(task->{
                                clearEdt();
                                startActivity(new Intent(BookingRoomActivity.this,BookSuccessActivity.class));
                                finish();

                            })
                            .addOnFailureListener(e->{
                                Snackbar snackbar = Snackbar.make(binding.mainLayout, "Failed!", Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red_error));
                                snackbar.show();
                            });
                }












        });

        //set Date and time dat phong
        binding.edtDateIn.setOnClickListener(v->{
            //Lay ngay hien tai
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            //Hien thi dialog DatePicker
            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingRoomActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                //Luu ngay duoc chon
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                //Hien thi ngay duoc chon
                binding.edtDateIn.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);

            },mYear,mMonth,mDay);
            datePickerDialog.show();
        });
        //set Date and time tra phong
        binding.edtDateOut.setOnClickListener(v->{
            //Lay ngay hien tai
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            //Hien thi dialog DatePicker
            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingRoomActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                //Luu ngay duoc chon
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                //Hien thi ngay duoc chon
                binding.edtDateOut.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);

            },mYear,mMonth,mDay);
            datePickerDialog.show();
        });
    }





    private void iNit() {
        auth = FirebaseAuth.getInstance();
        bookingStatusListener = this;

    }
    private void clearEdt(){
        binding.tvNameCustomer.setText("");
        binding.tvCccdCustomer.setText("");
        binding.tvPhoneCustomer.setText("");
        binding.edtDateIn.setText("");
        binding.edtDateOut.setText("");
        binding.ischeckPayment.setChecked(false);
    }

    @Override
    public void onBookingStateChange(boolean isExpired) {


    }
}