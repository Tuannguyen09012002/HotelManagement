package com.example.hotelmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hotelmanager.R;
import com.example.hotelmanager.model.ListViewModel;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<ListViewModel> {
    public ListViewAdapter(@NonNull Context context, ArrayList<ListViewModel> listData) {
        super(context, R.layout.row_item_listview_account,listData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ListViewModel listViewModel = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_item_listview_account,parent,false);

        }
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView label = view.findViewById(R.id.tv_label);

        assert listViewModel != null;
        imageView.setImageResource(listViewModel.image);
        label.setText(listViewModel.label);

        return view;

    }
}
