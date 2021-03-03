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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Class.Adpter_StoreItem;
import Utils.StoreItem;

public class StoreFragment extends Fragment {

    RecyclerView store_LST_equipment;
    FloatingActionButton store_BTN_add_equipment;
    Adpter_StoreItem adapter_searched_list_item;
    Bitmap bitmap;
    private ArrayList<StoreItem> equipment_array;
    private List<String> user_equipment_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        equipment_array = new ArrayList<>();
        findViews(view);

        store_LST_equipment.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_searched_list_item = new Adpter_StoreItem(getContext(), equipment_array);

        getUserEquipment();
        store_LST_equipment.setAdapter(adapter_searched_list_item);

        store_BTN_add_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().add(R.id.main_CNT_fragment, new AddEquipmentFragment(), "ADD_EQUIPMENT_FRAGMENT").commit();
            }
        });


        return view;
    }

    private void findViews(View view) {
        store_LST_equipment = view.findViewById(R.id.store_LST_equipment);
        store_BTN_add_equipment = view.findViewById(R.id.store_BTN_add_equipment);
    }

    private void getUserEquipment() {
        //fetch user equipment ids
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user_equipment_id = (List<String>) documentSnapshot.get("equipment_list");
                        user_equipment_id.remove(0);
                        Log.d("Pttt", "List of user equipment " + user_equipment_id.toString() + " and has units " + user_equipment_id.size());

                        //fetch equipment of current user
                        if (user_equipment_id.size() == 0) {
                            return;
                        }
                        for (String equipment_id : user_equipment_id) {
                            FirebaseFirestore.getInstance().collection("equipment").document(equipment_id).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            StorageReference storage_reference = FirebaseStorage.getInstance().getReference("equipment_image").child(documentSnapshot.get("item_image").toString());
                                            try {
                                                File file = File.createTempFile("image", "jpg");
                                                storage_reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    //store user equipment in StoreItem Class
                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                        equipment_array.add(new StoreItem()
                                                                .setPartName((String) documentSnapshot.get("part_name"))
                                                                .setDescription((String) documentSnapshot.get("description"))
                                                                .setImageRes((String) documentSnapshot.get("item_image"))
                                                                .setImageResBitmap(bitmap));
                                                        store_LST_equipment.setAdapter(adapter_searched_list_item);
                                                        Log.d("Pttt", "List of user equipment " + documentSnapshot.toString());
                                                    }
                                                });
                                            } catch (Exception e) {
                                                Log.d("Exception:", e.getMessage());
                                            }
                                        }
                                    });
                        }

                    }
                });
    }


}
