package Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Class.Adpter_CartItem;
import Utils.CartItem;


public class CartFragment extends Fragment {

    RecyclerView cart_LST_view;
    Adpter_CartItem adapter_cart_item;
    Bitmap bitmap;
    private List<String> request_list;
    private ArrayList<CartItem> cart_array;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cart_array = new ArrayList<>();
        cart_LST_view = view.findViewById(R.id.cart_LST_view);
        cart_LST_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_cart_item = new Adpter_CartItem(getContext(), cart_array);

        getRequests();

        cart_LST_view.setAdapter(adapter_cart_item);

        return view;
    }

    private void updateRequest(int position, String request) {
        FirebaseFirestore.getInstance().collection("requests").document(request_list.get(position))
                .update("accepted",request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Pttt", position+" request updated to "+request);
            }
        });
    }

    private void getRequests() {
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d("pttt", "Fetched user data");
                            //List<String> temp_list
                            request_list= (List<String>) documentSnapshot.get("request_list");
                            request_list.remove(0);

                            for (String iter : request_list) {
                                FirebaseFirestore.getInstance().collection("requests").document(iter).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    //get image
                                                    if(documentSnapshot.get("owner_id").toString().equals(FirebaseAuth.getInstance().getUid())){
                                                        return;
                                                    }
                                                    try {
                                                        Log.d("pttt", "try to get image");
                                                        File file = File.createTempFile("image", "jpg");
                                                        FirebaseStorage.getInstance().getReference("equipment_image")
                                                                .child((String) documentSnapshot.get("image_res"))
                                                                .getFile(file)
                                                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                                        cart_array.add(new CartItem()
                                                                                .setDate((String) documentSnapshot.get("date"))
                                                                                .setPartName((String) documentSnapshot.get("part_name"))
                                                                                .setOwnerName((String) documentSnapshot.get("owner_name"))
                                                                                .setAccepted((String) documentSnapshot.get("accepted"))
                                                                                .setDescription((String) documentSnapshot.get("description"))
                                                                                .setOwnerAddress((String) documentSnapshot.get("owner_address"))
                                                                                .setImageResBitmap(bitmap));
                                                                        cart_LST_view.setAdapter(adapter_cart_item);
                                                                    }
                                                                });

                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Pttt", "Could not get data request data: "+e.getMessage());
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Pttt", "could not read request list of user");
            }
        });
    }

}
