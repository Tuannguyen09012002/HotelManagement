package com.example.hotelmanager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.hotelmanager.R;
import com.example.hotelmanager.activitys.NotifiRoomCreatedActivity;
import com.example.hotelmanager.databinding.FragmentAddBinding;
import com.example.hotelmanager.model.Room;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private List<Uri> selectedImaes = new ArrayList<>();
    private ImageView currentImage;
    private String selectedRoomTypes;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private NavController navController;
    private FirebaseAuth auth;
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    selectedImaes.add(imageUri);
                    if (currentImage != null) {
                        currentImage.setImageURI(imageUri);
                    }
                }
            }
    );


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iNit(view);



        //**************Lay hinh anh ***********************//
        binding.imageRoom1.setOnClickListener(v -> {
            openGallery(binding.imageRoom1);
        });
        binding.imageRoom2.setOnClickListener(v -> {
            openGallery(binding.imageRoom2);
        });
        binding.imageRoom3.setOnClickListener(v -> {
            openGallery(binding.imageRoom3);
        });
        binding.imageRoom4.setOnClickListener(v -> {
            openGallery(binding.imageRoom4);
        });

        //dua thong tin phong len firebase
        pushDataToFirebase();


        // neu thanh cong thi se thuc thi cau lenh nay
        binding.btnCreateRoom.setOnClickListener(v -> {

            if(checkInputs()){
                pushDataToFirebase();
                startActivity(new Intent(getActivity(), NotifiRoomCreatedActivity.class));
                getActivity().finish();
            }
            else{
                Toast.makeText(requireContext(),"Vui lòng điền đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
            }





        });

        //Action cho nut btn back
        binding.btnBack.setOnClickListener(v->{
            navController.navigateUp();
        });
    }

    private void iNit(View view) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        navController = Navigation.findNavController(view);
        if(currentUser != null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(currentUser.getUid());
            storageReference = FirebaseStorage.getInstance().getReference().child("room_images");
        }

    }

    //Kiem tra input nhap
    private boolean checkInputs() {
        String nameRoom = binding.edtNameRoom.getText().toString().trim();
        String priceRoom = binding.edtPriceRoom.getText().toString().trim();
        String descriptionRoom = binding.edtDescription.getText().toString().trim();

        return !TextUtils.isEmpty(nameRoom) && !TextUtils.isEmpty(priceRoom) &&
                !TextUtils.isEmpty(descriptionRoom) && !selectedImaes.isEmpty();
    }

    private void pushDataToFirebase() {
        String nameRoom = binding.edtNameRoom.getText().toString().trim();
        String priceRoom = binding.edtPriceRoom.getText().toString().trim();
        String descriptonRoom = binding.edtDescription.getText().toString().trim();
        String roomType = selectedRoomTypes;


        //Dat su kien cho cac checkbox
        List<String> checkBoxValues = new ArrayList<>();
        if (binding.checkBtnBancong.isChecked()) {
            checkBoxValues.add("Có ban công");
        }
      if (binding.checkBtnMaylanhroom.isChecked()) {
            checkBoxValues.add("Có máy lạnh");
        }
       if (binding.checkBtnViewcity.isChecked()) {
            checkBoxValues.add("Có view thành phố");
        }



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.room_types, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRoom.setAdapter(adapter);

        binding.spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedRoomTypes = adapterView.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        //Luu danh sach cac anh
        List<String> imageUrls = new ArrayList<>();
        for (Uri imageUri : selectedImaes) {
            StorageReference imageRef = storageReference.child(imageUri.getLastPathSegment());
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    //Luu anh vao danh sach
                                    imageUrls.add(uri.toString());

                                    if (imageUrls.size() == selectedImaes.size()) {
                                        //Tao Room moi
                                        Room room = new Room(
                                                nameRoom,
                                                priceRoom,
                                                roomType,
                                                descriptonRoom,
                                                checkBoxValues,
                                                imageUrls

                                        );
                                        databaseReference.push().setValue(room);
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                });

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    });
        }
    }

    private void openGallery(ImageView imageView) {
        currentImage = imageView;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        launcher.launch(intent);
    }
}