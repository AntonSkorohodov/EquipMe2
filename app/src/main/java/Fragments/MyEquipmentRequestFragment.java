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
import Class.Adpter_MyEquipmentRequestItem;

import Utils.MyEquipmentRequestItem;


public class MyEquipmentRequestFragment extends Fragment {

    RecyclerView my_equipment_request_LST_view;
    Adpter_MyEquipmentRequestItem adapter_my_equipmet_request_item;
    Bitmap bitmap;
    private List<String> request_list;
    private ArrayList<MyEquipmentRequestItem> my_equipmet_request_array;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_equipment_request, container, false);

        my_equipmet_request_array = new ArrayList<>();
        my_equipment_request_LST_view = view.findViewById(R.id.my_equipment_request_LST_view);
        my_equipment_request_LST_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_my_equipmet_request_item = new Adpter_MyEquipmentRequestItem(getContext(), my_equipmet_request_array);

        getRequests();

        adapter_my_equipmet_request_item.setClickListener(new Adpter_MyEquipmentRequestItem.ItemClickListener() {

            @Override
            public void onItemClick(View view, int position,String request) {
                updateRequest(position,request);
                getFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment, new MyEquipmentRequestFragment(), "CART_FRAGMENT").commit();
            }

            @Override
            public void onReportClick(int position) {

            }
        });

        my_equipment_request_LST_view.setAdapter(adapter_my_equipmet_request_item);

        return view;
    }

    private void updateRequest(int position, String request) {
        String request_id = my_equipmet_request_array.get(position).getRequestId();
        FirebaseFirestore.getInstance().collection("requests").document(request_list.get(Integer.valueOf(request_id)))
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
                                                    if(documentSnapshot.get("requester_id").toString().equals(FirebaseAuth.getInstance().getUid())){
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
                                                                        my_equipmet_request_array.add(new MyEquipmentRequestItem()
                                                                                .setDate((String) documentSnapshot.get("date"))
                                                                                .setPartName((String) documentSnapshot.get("part_name"))
                                                                                .setRequesterName((String) documentSnapshot.get("requester_name"))
                                                                                .setAccepted((String) documentSnapshot.get("accepted"))
                                                                                .setDescription((String) documentSnapshot.get("description"))
                                                                                .setImageResBitmap(bitmap)
                                                                                .setRequestId(iter));
                                                                        my_equipment_request_LST_view.setAdapter(adapter_my_equipmet_request_item);
                                                                        Log.d("Pttt",my_equipmet_request_array.get(my_equipmet_request_array.size()-1).getPartName());
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
