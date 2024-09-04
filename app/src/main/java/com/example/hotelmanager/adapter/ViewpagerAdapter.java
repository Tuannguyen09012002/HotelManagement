package com.example.hotelmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanager.databinding.RowViewpagerDardboardBinding;
import com.example.hotelmanager.model.Banner;

import java.util.List;

public class ViewpagerAdapter extends RecyclerView.Adapter<ViewpagerAdapter.ViewHolder> {
    List<Banner> mBanners;
    Context context;

    public ViewpagerAdapter(List<Banner> mBanners) {
        this.mBanners = mBanners;
    }

    @NonNull
    @Override
    public ViewpagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RowViewpagerDardboardBinding binding = RowViewpagerDardboardBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
       return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewpagerAdapter.ViewHolder holder, int position) {
        Banner banner = mBanners.get(position);
        holder.binding.imageView.setImageResource(banner.getImageView());
      //  holder.binding.tvBooked.setText(new StringBuilder().append("(").append(banner.getCountBooked()).append(")"));

    }

    @Override
    public int getItemCount() {
        return mBanners.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        RowViewpagerDardboardBinding binding;

        public ViewHolder(@NonNull RowViewpagerDardboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
