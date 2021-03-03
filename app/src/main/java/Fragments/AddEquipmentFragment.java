package Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

import com.example.equipme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Class.Equipment;

import static android.app.Activity.RESULT_OK;


public class AddEquipmentFragment extends Fragment {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private ImageView equipment_IMG_image;
    private EditText equipment_EDT_part_name, equipment_EDT_description, equipment_EDT_price, equipment_EDT_category;
    private Button equipment_BTN_add;
    private String userId;
    private int counter;
    private Uri returnUri;
    private Equipment equipment;
    Map<String,Number> counter_map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_equipment, container, false);

        findViews(view);

        equipment_IMG_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageGalleryandCheckPermission();
            }
        });
        getLastImageId();

        equipment_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnUri == null) {
                    Toast.makeText(getContext(), "Choose new Image", Toast.LENGTH_SHORT).show();
                }else {
                    counter++;
                    String item_image = (String.valueOf(counter)) + ".jpg";
                    equipment = new Equipment(equipment_EDT_part_name.getText().toString().trim(),
                            equipment_EDT_description.getText().toString(), equipment_EDT_category.getText().toString(),
                            equipment_EDT_price.getText().toString(), FirebaseAuth.getInstance().getUid().toString(), item_image);
                    pushEquipmentToFirestore();
                    pushImageToStorage();
                    updateFirestoreCounter();
                    updateUserequipmentList();
                }
            }
        });

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
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                equipment_IMG_image.setImageBitmap(bitmapImage);
            }
        }
    }


    private void updateUserequipmentList() {
        DocumentReference dr = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> temp_list = (List<String>) documentSnapshot.get("equipment_list");
                    Map<String,Object> temp_map = (HashMap<String, Object>)documentSnapshot.getData();
                    temp_list.add(String.valueOf(counter));
                    temp_map.replace("equipment_list", temp_list);
                    dr.set(temp_map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            getFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment, new StoreFragment(), "STORE_FRAGMENT").commit();
                            Log.d("Pttt","User updated equipment list");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Pttt","Could not update user data");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void pushImageToStorage() {
        StorageReference sr = FirebaseStorage.getInstance().getReference().child("equipment_image/" + equipment.getItemImage());
        sr.putFile(returnUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Pttt","Equipment image Uploaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Pttt", "Could not upload Image: " + e.getMessage());
            }
        });
    }

    private void pushEquipmentToFirestore() {
        DocumentReference dr = FirebaseFirestore.getInstance().collection("equipment").document(String.valueOf(counter));
        dr.set(equipment.getEquipment()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Pttt", "Equipment added");
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Pttt", "Could not add equipment to FireStore: " + e.getMessage());
            }
        });
    }

    public void updateFirestoreCounter(){
        DocumentReference document_referencer = FirebaseFirestore.getInstance().collection("counters").document("counters");
        document_referencer.update("equipment_id_last",counter).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Pttt", "counter updated");
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Pttt", "Could not update counter to FireStore: " + e.getMessage());
            }
        });
    }

    private void getLastImageId() {
        DocumentReference document_referencer = FirebaseFirestore.getInstance().collection("counters").document("counters");
        document_referencer.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    counter = ((Number) documentSnapshot.get("equipment_id_last")).intValue();
                    Log.d("Pttt", "Image id counter is = " + counter);
                    counter_map = new HashMap<String,Number>();
                    counter_map.put("equipment_id_last", counter);
                    Log.d("Pttt","old map: " + counter_map.toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Could not read counters: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void findViews(View view) {
        equipment_IMG_image = view.findViewById(R.id.equipment_IMG_image);
        equipment_EDT_part_name = view.findViewById(R.id.equipment_EDT_part_name);
        equipment_EDT_price = view.findViewById(R.id.equipment_EDT_price);
        equipment_EDT_description = view.findViewById(R.id.equipment_EDT_description);
        equipment_EDT_category = view.findViewById(R.id.equipment_EDT_category);
        equipment_BTN_add = view.findViewById(R.id.equipment_BTN_add);

    }
}
