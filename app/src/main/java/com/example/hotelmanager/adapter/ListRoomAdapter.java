package com.example.hotelmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanager.R;
import com.example.hotelmanager.databinding.RowItemListRoomBinding;
import com.example.hotelmanager.listener.IBookingStatusListener;
import com.example.hotelmanager.listener.IRecyclerView;
import com.example.hotelmanager.model.Booking;
import com.example.hotelmanager.model.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.ViewHolder> {

    List<Room> mList;
    Context context;
    private IRecyclerView listener;
    private FirebaseAuth auth;





    public ListRoomAdapter(List<Room> mList, Context context,IRecyclerView listener) {
        this.mList = mList;
        this.context = context;
        this.listener = listener;
        auth = FirebaseAuth.getInstance();


    }

    @NonNull
    @Override
    public ListRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RowItemListRoomBinding binding = RowItemListRoomBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
       return new ViewHolder(binding,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRoomAdapter.ViewHolder holder, int position) {
        Room room = mList.get(position);


        holder.binding.tvNameRoom.setText(new StringBuffer("P.").append(room.getNameRoom()));
        DecimalFormat formatPrice = new DecimalFormat("#,###");
        Double price = Double.parseDouble(room.getPriceRoom());
        holder.binding.tvPriceRoom.setText(new StringBuffer("Giá: ")
                .append(formatPrice.format(price))
                .append(" VND"));
        holder.binding.tvLoaiRoom.setText(room.getRoomType());

       FirebaseUser currentUser = auth.getCurrentUser();
       if(currentUser != null){
           FirebaseDatabase.getInstance().getReference("booking")
                   .child(currentUser.getUid())
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(snapshot.exists()){
                               for(DataSnapshot bookedSnapshot : snapshot.getChildren()){
                                   Booking booking =bookedSnapshot.getValue(Booking.class);
                                   assert booking != null;
                                   String nameRoom = booking.getNameRoom();
                                   long checkSoNgay = booking.getSoNgayConLai();
                                   if(room.getNameRoom().equals(nameRoom) && checkSoNgay >0){
                                       holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.booked_grey));
                                       holder.binding.iconLock.setVisibility(View.VISIBLE);
                                       holder.itemView.setEnabled(false);
                                       return;
                                   }


                               }

                           }
                           holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
                           holder.binding.iconLock.setVisibility(View.GONE);
                           holder.itemView.setEnabled(true);

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
       }










    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        RowItemListRoomBinding binding;
        Booking booking;

        private IRecyclerView listener;

        public ViewHolder(@NonNull RowItemListRoomBinding binding, IRecyclerView listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;


            itemView.setOnClickListener(v->{
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(mList.get(position));
                    }
                }

            });


        }



    }


}
