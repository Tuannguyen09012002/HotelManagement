package com.example.hotelmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.RowBookedOrderingBinding;
import com.example.hotelmanager.model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.ViewHolder> {
    List<Booking> mBookings;
    Context context;
    private FirebaseAuth auth;

    public BookedAdapter(List<Booking> mBookings) {
        this.mBookings = mBookings;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BookedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RowBookedOrderingBinding binding = RowBookedOrderingBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
       return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedAdapter.ViewHolder holder, int position) {
        Booking booking = mBookings.get(position);
        context = holder.itemView.getContext();
        DecimalFormat format = new DecimalFormat("#,### VND");
        holder.binding.tvNameRoom.setText(new StringBuilder("P.").append(booking.getNameRoom()));
        holder.binding.tvTypeRoom.setText(new StringBuilder("(").append(booking.getTypeRoom()).append(")"));
        holder.binding.tvNameCustomer.setText(new StringBuilder("Khách hàng: ").append(booking.getNameCustomer()));
        holder.binding.tvCccd.setText(new StringBuilder("CCCD: ").append(booking.getCCCD()));
        holder.binding.tvPhone.setText(new StringBuilder("SDT: ").append(booking.getPhoneCustomer()));
        holder.binding.tvPriceRoom.setText(new StringBuilder("Giá phòng: ").append(format.format(Long.parseLong(booking.getPriceRoom()))));
        holder.binding.tvDateBook.setText(new StringBuilder("Ngày đặt phòng: ").append(booking.getCheckInDatetime()));
        holder.binding.tvDateReturn.setText(new StringBuilder("Ngày trả phòng: ").append(booking.getCheckOutDatetime()));

       long sumMoney = booking.getNumberOfDays() * Long.parseLong(booking.getPriceRoom());

       holder.binding.tvSumItem.setText(new StringBuilder("Tổng tiền: ").append(format.format(sumMoney))
               .append("/").append(booking.getNumberOfDays()).append(" ngày"));

       holder.binding.actionDelete.setOnClickListener(v->{
           AlertDialog.Builder builder = new AlertDialog.Builder(context);
           builder.setMessage("Bạn có chắc muốn xóa ?");
           builder.setPositiveButton("Có",(dialog,which)->{
               FirebaseUser currentUser = auth.getCurrentUser();
               if(currentUser!=null){
                   FirebaseDatabase.getInstance().getReference("booking")
                           .child(currentUser.getUid())
                           .child(booking.getBookId())
                           .removeValue();
                   refreshList(mBookings);
                   dialog.dismiss();
                   Toast.makeText(context,"Đã xóa",Toast.LENGTH_SHORT).show();


               }

           });
           builder.setNegativeButton("Hủy",(dialog,which)->{
               dialog.dismiss();
           });

           AlertDialog alertDialog = builder.create();
           alertDialog.show();
       });
       if(booking.getSoNgayConLai() >0){
         holder.binding.ivStatus.setImageResource(R.drawable.status_red_circle_24);
         holder.binding.tvStatus.setText("Đang thuê");
       }
       else{
           holder.binding.ivStatus.setImageResource(R.drawable.status_green_circle_24);
           holder.binding.tvStatus.setText("Đã trả");
       }

    }

    @Override
    public int getItemCount() {
        return mBookings.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        RowBookedOrderingBinding binding;

        public ViewHolder(@NonNull RowBookedOrderingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void refreshList(List<Booking> newList){
        newList.clear();
        mBookings.addAll(newList);
        notifyDataSetChanged();
    }
}
