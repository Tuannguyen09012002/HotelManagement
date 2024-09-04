package com.example.hotelmanager.listener;

import com.example.hotelmanager.model.Room;

import java.util.List;

public interface IRoomLoadListener {
    void onRoomLoadSuccess(List<Room> roomList);
    void onRoomLoadFailed(String message);
}
