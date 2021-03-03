package Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import Class.User;

import com.example.equipme.Activities.MainActivity;
import com.example.equipme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {


    private ImageView profile_IMG_image;
    private EditText profile_EDT_name,profile_EDT_email,profile_EDT_phone,profile_EDT_address;
    private Button profile_BTN_update;
    private Bitmap bitmap_image;
    private Uri returnUri;
    private FirebaseAuth authentication;
    private FirebaseFirestore firestore;
    private StorageReference storage_reference;
    private FirebaseStorage firebase_storage;
    private DocumentReference documentReference;
    private String userId;
    private User user = new User();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile,container,false);

        findViews(view);
        Context activity = getActivity().getBaseContext();

        authentication = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = authentication.getCurrentUser().getUid();
        returnUri = Uri.parse(String.valueOf(R.drawable.no_image_icon));

        profile_IMG_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageGalleryandCheckPermission();
            }
        });

        documentReference = firestore.collection("users").document(userId);
        getUserData();
        return view;
    }

    private void openImageGalleryandCheckPermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            pickImageFromFallery();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2000);
        }
    }

    private void pickImageFromFallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                returnUri = data.getData();
                try {
                    bitmap_image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profile_IMG_image.setImageBitmap(bitmap_image);
            }
        }
    }


    private void setFieldsData() {
        profile_EDT_name.setText(user.getName());
        profile_EDT_email.setText(user.getEmail());
        profile_EDT_phone.setText(user.getPhone());
        profile_EDT_address.setText(user.getAddress());
    }

    private void getImage() {
        firebase_storage = FirebaseStorage.getInstance();
        try{
            storage_reference = firebase_storage.getReference("users_image").child(user.getProfileImage());
            File file = File.createTempFile("image","jpg");
            storage_reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap_image = BitmapFactory.decodeFile(file.getAbsolutePath());
                    profile_IMG_image.setImageBitmap(bitmap_image);
                    Log.d("Pttt","Fetched Image");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Pttt","Failed to fetch image");
                }
            });
        }catch (Exception e){
            Log.d("Pttt","Exception" +e.getMessage());
        }

    }

    private void getUserData() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user.setUser((HashMap<String, Object>)documentSnapshot.getData());
                    Log.d("Pttt","Profile user name: "+user.getName());
                    Log.d("Pttt","Profiler image name: "+user.getProfileImage());
                    getImage();
                    setFieldsData();
                    setNewData();
                }else{
                    Toast.makeText(getActivity(),"User does not have any data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Could not read data: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("Pttt","user initialized ");

    }

    //interface

    private void setNewData() {
        profile_BTN_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setAddress(profile_EDT_address.getText().toString());
                user.setName(profile_EDT_name.getText().toString());
                user.setPhone(profile_EDT_phone.getText().toString());
                documentReference.set(user.getUser(), SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Pttt","Profile updated");
                    }
                });
                FirebaseStorage.getInstance().getReference().child("users_image/"+FirebaseAuth.getInstance().getUid()+".jpg").putFile(returnUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Pttt","Profile image updated");
                        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid())
                                .update("profile_img",FirebaseAuth.getInstance().getUid()+".jpg").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Pttt", "Could not update profile image"+e.getMessage());
                    }
                });

            }
        });
    }

    private void findViews(View view) {
        profile_IMG_image = view.findViewById(R.id.profile_IMG_image);
        profile_EDT_name = view.findViewById(R.id.profile_EDT_name);
        profile_EDT_phone = view.findViewById(R.id.profile_EDT_phone);
        profile_EDT_email = view.findViewById(R.id.profile_EDT_email);
        profile_EDT_address = view.findViewById(R.id.profile_EDT_address);
        profile_BTN_update = view.findViewById(R.id.profile_BTN_update);
    }
}
