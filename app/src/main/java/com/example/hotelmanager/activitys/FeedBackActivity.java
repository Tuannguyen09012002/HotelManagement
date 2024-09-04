package com.example.hotelmanager.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.hotelmanager.R;
import com.example.hotelmanager.adapter.FeedbackAdapter;
import com.example.hotelmanager.databinding.ActivityFeedBackBinding;
import com.example.hotelmanager.model.Customer;
import com.example.hotelmanager.model.Review;
import com.example.hotelmanager.others.Configs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedBackActivity extends AppCompatActivity {
    private ActivityFeedBackBinding binding;
    private FirebaseAuth auth;
    private ArrayList<Review> mFeedbacks;
    private FeedbackAdapter feedbackAdapter;
    private Configs configs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_splash));

        binding.btnBack.setOnClickListener(v->{
            finish();
        });

        iNit();
        loadFeedbackFromFirebase();
    }

    private void loadFeedbackFromFirebase() {
        mFeedbacks = new ArrayList<>();
        DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference("Users");
        feedbackRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mFeedbacks.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot customerSnapshot : snapshot.getChildren()) {
                        String CCCD = customerSnapshot.getKey();
                        String nameUser = customerSnapshot.child("name_user").getValue(String.class);
                        String imgUrl = customerSnapshot.child("imgUrl").getValue(String.class);

                        for (DataSnapshot ratingSnapshot : customerSnapshot.child("Ratings").getChildren()) {
                            String nameRoom = ratingSnapshot.getKey();
                            String room_title = ratingSnapshot.child("product_title").getValue(String.class);
                            String rating = ratingSnapshot.child("rating").getValue(String.class);
                            String review = ratingSnapshot.child("review").getValue(String.class);

                            Review feedback = new Review();
                            feedback.setCustomerName(nameUser);
                            feedback.setCustomerUrl(imgUrl);
                            feedback.setRating(rating);
                            feedback.setReview(review);
                            feedback.setNameRoom(room_title);

                            mFeedbacks.add(feedback);

                        }


                    }
                    if (mFeedbacks.size() > 0) {
                        configs.hideProgressDialog();
                        binding.recyclerview.setHasFixedSize(true);
                        binding.recyclerview.setLayoutManager(new LinearLayoutManager(FeedBackActivity.this));
                        feedbackAdapter = new FeedbackAdapter(mFeedbacks);
                        binding.recyclerview.setAdapter(feedbackAdapter);

                    }
                    else{
                        configs.hideProgressDialog();
                    }
                }
                else{
                    configs.hideProgressDialog();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                configs.hideProgressDialog();

            }
        });
    }

    private void iNit() {

        auth = FirebaseAuth.getInstance();
        configs = new Configs();
        configs.showProgressDialog(this);

    }
}