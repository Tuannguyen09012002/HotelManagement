package com.example.hotelmanager.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Booking {
    private String bookId;
    private String nameRoom;
    private String priceRoom;
    private String typeRoom;
    private String nameCustomer;
    private String CCCD;
    private String phoneCustomer;
    private String checkInDatetime;
    private String checkOutDatetime;
    private Boolean isPayMentChecked;
    private long numberOfDays;
    private long soNgayConLai;

    public Booking() {
    }

    public Booking(String bookId, String nameRoom, String priceRoom, String typeRoom, String nameCustomer, String CCCD, String phoneCustomer, String checkInDatetime, String checkOutDatetime, Boolean isPayMentChecked,long numberOfDays,long soNgayConLai) {
        this.bookId = bookId;
        this.nameRoom = nameRoom;
        this.priceRoom = priceRoom;
        this.typeRoom = typeRoom;
        this.nameCustomer = nameCustomer;
        this.CCCD = CCCD;
        this.phoneCustomer = phoneCustomer;
        this.checkInDatetime = checkInDatetime;
        this.checkOutDatetime = checkOutDatetime;
        this.isPayMentChecked = isPayMentChecked;
        this.numberOfDays = numberOfDays;
        this.soNgayConLai = soNgayConLai;

    }

    public long getSoNgayConLai() {
        return soNgayConLai;
    }

    public void setSoNgayConLai(long soNgayConLai) {
        this.soNgayConLai = soNgayConLai;
    }

    public long getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(long numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Boolean getPayMentChecked() {
        return isPayMentChecked;
    }

    public void setPayMentChecked(Boolean payMentChecked) {
        isPayMentChecked = payMentChecked;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public String getPriceRoom() {
        return priceRoom;
    }

    public void setPriceRoom(String priceRoom) {
        this.priceRoom = priceRoom;
    }

    public String getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(String typeRoom) {
        this.typeRoom = typeRoom;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }

    public String getCheckInDatetime() {
        return checkInDatetime;
    }

    public void setCheckInDatetime(String checkInDatetime) {
        this.checkInDatetime = checkInDatetime;
    }

    public String getCheckOutDatetime() {
        return checkOutDatetime;
    }

    public void setCheckOutDatetime(String checkOutDatetime) {
        this.checkOutDatetime = checkOutDatetime;
    }

    public long calculateNumberOfDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            // Chuyển đổi ngày đặt và ngày trả phòng sang định dạng Date
            Date checkInDate = sdf.parse(checkInDatetime);
            Date checkOutDate = sdf.parse(checkOutDatetime);

            // Tính số ngày bằng cách lấy sự khác biệt giữa ngày trả phòng và ngày đặt
            long diffInMillis = checkOutDate.getTime() - checkInDate.getTime();
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu có lỗi xảy ra
    }

    public long calculateSoNgayConLai() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        try {
            // Chuyển đổi ngày trả phòng sang định dạng Date
            Date checkOutDate = sdf.parse(checkOutDatetime);

            // Tính số ngày bằng cách lấy sự khác biệt giữa ngày trả phòng và ngày đặt
            long diffInMillis = (checkOutDate.getTime()- currentDate.getTime());
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu có lỗi xảy ra
    }





}
