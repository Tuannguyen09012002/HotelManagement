package com.example.hotelmanager.model;

import java.util.List;

public class Room {
    private String nameRoom;
    private String priceRoom;
    private String roomType;
    private String descriptionRoom;
    private List<String> listCheckBox;
    private List<String> images;


    public Room() {
    }

    public Room(String nameRoom, String priceRoom, String roomType, String descriptionRoom, List<String> listCheckBox, List<String> images) {
        this.nameRoom = nameRoom;
        this.priceRoom = priceRoom;
        this.roomType = roomType;
        this.descriptionRoom = descriptionRoom;
        this.listCheckBox = listCheckBox;
        this.images = images;

    }


    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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

    public String getDescriptionRoom() {
        return descriptionRoom;
    }

    public void setDescriptionRoom(String descriptionRoom) {
        this.descriptionRoom = descriptionRoom;
    }

    public List<String> getListCheckBox() {
        return listCheckBox;
    }

    public void setListCheckBox(List<String> listCheckBox) {
        this.listCheckBox = listCheckBox;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }


}
