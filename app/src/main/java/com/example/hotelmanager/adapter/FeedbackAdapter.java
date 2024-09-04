package com.example.hotelmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.RowFeedbackCustomerBinding;
import com.example.hotelmanager.model.Booking;
import com.example.hotelmanager.model.Review;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    ArrayList<Review> mReviews;
    Context context;



    public FeedbackAdapter(ArrayList<Review> mReviews) {
        this.mReviews = mReviews;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RowFeedbackCustomerBinding binding = RowFeedbackCustomerBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
       return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {
        Review review = mReviews.get(position);
        context = holder.itemView.getContext();
        holder.binding.tvNameUser.setText(review.getCustomerName());
        holder.binding.tvNameRoom.setText(new StringBuilder("P.").append(review.getNameRoom()));
        holder.binding.ratingbar.setRating(Float.parseFloat(String.valueOf(review.getRating())));
        holder.binding.tvReview.setText(review.getReview());

        Glide.with(context).load(review.getCustomerUrl())
                .placeholder(R.drawable.avatar_default).into(holder.binding.ivAvatar);

        AtomicBoolean isLike = new AtomicBoolean(false);


        holder.binding.ivLike.setOnClickListener(v->{
            if(!isLike.get()){
                holder.binding.ivLike.setImageResource(R.drawable.like_enable_24);
                isLike.set(true);
            }
            else{
                holder.binding.ivLike.setImageResource(R.drawable.like_24);
                isLike.set(false);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RowFeedbackCustomerBinding binding;

        public ViewHolder(@NonNull RowFeedbackCustomerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
