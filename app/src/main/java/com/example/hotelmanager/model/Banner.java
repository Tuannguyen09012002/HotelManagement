package com.example.hotelmanager.model;

public class Banner {
    private int imageView;
    private String countBooked;
    private String emptyRoom;

    public Banner(int imageView) {
        this.imageView = imageView;
    }

    public Banner() {
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getCountBooked() {
        return countBooked;
    }

    public void setCountBooked(String countBooked) {
        this.countBooked = countBooked;
    }

    public String getEmptyRoom() {
        return emptyRoom;
    }

    public void setEmptyRoom(String emptyRoom) {
        this.emptyRoom = emptyRoom;
    }
}
