package com.example.hotelmanager.others;

import android.app.AlertDialog;
import android.content.Context;

import com.example.hotelmanager.R;

public class Configs {
    private AlertDialog alertDialog;

    public void  showProgressDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.loading_progress_layout);
        alertDialog = builder.create();
        alertDialog.show();

    }
    public void hideProgressDialog(){
        alertDialog.dismiss();
    }
}
