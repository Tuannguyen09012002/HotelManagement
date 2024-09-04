package com.example.hotelmanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelmanager.databinding.RowListCustomerBinding;
import com.example.hotelmanager.model.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    List<Customer> mList;
    Context context;
    FirebaseAuth auth;

    public CustomerAdapter(List<Customer> mList, Context context) {
        this.mList = mList;
        this.context = context;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowListCustomerBinding binding = RowListCustomerBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        Customer customer = mList.get(position);
        holder.binding.tvName.setText(customer.getCustomerName());
        holder.binding.tvAddress.setText(customer.getCustomerAddress());
        holder.binding.tvBirthday.setText(customer.getCustomerBirthday());
        holder.binding.tvCccd.setText(customer.getCustomerCCCD());
        holder.binding.tvPhone.setText(customer.getCustomerPhone());

        holder.binding.btnDeleteCustomer.setOnClickListener(v->{
            Customer deleteCustomer = mList.get(holder.getAdapterPosition());
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Bạn có chắc muốn xóa khách hàng này?");
            builder.setPositiveButton("Có",(dialog,which) ->{
                FirebaseUser currentAdmin = auth.getCurrentUser();
                if(currentAdmin != null){
                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("customer")
                            .child(currentAdmin.getUid());
                    dataRef.child(deleteCustomer.getCustomerId())
                            .removeValue()
                            .addOnSuccessListener(aVoid ->{
                               mList.remove(deleteCustomer);
                               notifyDataSetChanged();
                                Toast.makeText(context,"Đã xóa",Toast.LENGTH_SHORT).show();

                            })
                            .addOnFailureListener(e->{
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            });
                }
            });
            builder.setNegativeButton("Hủy",(dialog,which)->{
                dialog.dismiss();

            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RowListCustomerBinding binding;

        public ViewHolder(@NonNull RowListCustomerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
