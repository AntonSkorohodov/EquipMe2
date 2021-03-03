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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipme.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;


import Utils.DistanceCalculator;
import Utils.SearchedListItem;
import Class.Adpter_SearchedListItem;


public class SearchedListFragment extends Fragment implements DistanceCalculator.CallBack_Top {

    private String json_string;
    private ArrayList<SearchedListItem> users_array;
    private RecyclerView searched_LST_equipment;
    Adpter_SearchedListItem adapter_searched_list_item;
    private List<HashMap<String, Object>> list_map;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_searched_list, container, false);
        Bundle bundle = getArguments();
        if (null != bundle) {
            json_string = bundle.getString("json_list");
            Log.d("Pttt,", "Bundle had :" + json_string);
            Gson gson = new Gson();
            list_map = gson.fromJson(json_string, new TypeToken<List<HashMap<String, Object>>>() {
            }.getType());
            Log.d("Pttt", "List<HashMap<String,Object>>[0] = " + list_map.get(0));
        }

        SearchedListAddapter(list_map);
        users_array = new ArrayList<SearchedListItem>();
        searched_LST_equipment = view.findViewById(R.id.searched_LST_equipment);
        searched_LST_equipment.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_searched_list_item = new Adpter_SearchedListItem(getContext(), users_array);


        adapter_searched_list_item.setClickListener(new Adpter_SearchedListItem.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentTransaction fragment_transaction = getFragmentManager().beginTransaction();
                Bundle bundle_object = new Bundle();
                bundle_object.putString("json_list", new Gson().toJson(users_array.get(position)));
                RentFragment rent_fragment = new RentFragment();
                rent_fragment.setArguments(bundle_object);
                fragment_transaction.addToBackStack(null);
                getFragmentManager().beginTransaction().add(R.id.main_CNT_fragment, rent_fragment, "rent_fragment").commit();
            }

            @Override
            public void onReportClick(int position) {
            }
        });
        searched_LST_equipment.setAdapter(adapter_searched_list_item);

        return view;

    }

    private void SearchedListAddapter(List<HashMap<String, Object>> l) {
        ListIterator<HashMap<String, Object>> listIterator = l.listIterator();
        while (listIterator.hasNext()) {
            HashMap<String, Object> temp = listIterator.next();

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(temp.get("user_id").toString());
            StorageReference storage_reference = FirebaseStorage.getInstance().getReference("equipment_image").child(temp.get("item_image").toString());

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DistanceCalculator cal = new DistanceCalculator();
                    String address = documentSnapshot.get("address").toString();
                    String name = documentSnapshot.get("name").toString();
                    cal.calucalteDistance(address, getContext(), getActivity());
                    cal.getDistance();
                    cal.setCalBackListener(new DistanceCalculator.CallBack_Top() {
                        @Override
                        public void onCalculationFinished(double distance,double lat,double lng) {
                            LatLng location = new LatLng(lat,lng );
                            DecimalFormat twoDForm = new DecimalFormat("#.#");
                            try {
                                File file = File.createTempFile("image", "jpg");
                                storage_reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        users_array.add(new SearchedListItem()
                                                .setPartName((String) temp.get("part_name"))
                                                .setDescription((String) temp.get("description"))
                                                .setImageRes((String) temp.get("item_image"))
                                                .setName(name)
                                                .setImageResBitmap(bitmap)
                                                .setUserId((String) temp.get("user_id"))
                                                .setAddress(address)
                                                .setLat_lng(location)
                                                .setDates((ArrayList<Timestamp>) temp.get("not_avalible"))
                                                .setMeaseredDistance(String.valueOf(Double.valueOf(twoDForm.format(distance))))); //cast to one number after decimal dot format (###.#)
                                        Log.d("SearchList", "Bitmap width" + bitmap.getWidth());
                                        Log.d("Pttt","Name: "+temp.get("name"));
                                        searched_LST_equipment.setAdapter(adapter_searched_list_item);
                                        Log.d("Pttt", "fond item added");
                                    }
                                });


                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("Pttt", "Exception: " + e.getMessage());

                            }
                        }

                    });

                }
            });

        }

    }

    @Override
    public void onCalculationFinished(double distance_,double lat,double lng) {
        users_array.set(users_array.size(), users_array.get(users_array.size()).setMeaseredDistance(String.valueOf(distance_)));
        Log.d("CallBack", "distance is = " + distance_);
        searched_LST_equipment.setAdapter(adapter_searched_list_item);
    }

}
