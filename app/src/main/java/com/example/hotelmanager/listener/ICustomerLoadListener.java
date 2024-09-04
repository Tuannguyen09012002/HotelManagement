package com.example.hotelmanager.listener;

import com.example.hotelmanager.model.Customer;

import java.util.List;

public interface ICustomerLoadListener {
    void onCustomerLoadSuccess(List<Customer> customerList);
    void onCustomerLoadFailed(String message);
}
