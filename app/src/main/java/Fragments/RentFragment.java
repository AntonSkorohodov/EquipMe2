package Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.equipme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.SearchedListItem;


public class RentFragment extends Fragment implements OnMapReadyCallback {


    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private String json_string, date;
    private int counter;
    private SearchedListItem searched_item;
    private GoogleMap google_map;
    private LatLng location;
    TextView rent_LBL_description, rent_LBL_measered_distance, rent_LBL_part_name, rent_LBL_address;
    ImageView rent_IMG_image;
    MapView rent_MAP_map;
    Button rent_BTN_rent;
    //CalendarView rent_calendar;
    CalendarView rent_calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent, container, false);

        Bundle bundle = getArguments();
        parseBundle(bundle);
        location = searched_item.getLat_lng();
        findViews(view);
        initMap(savedInstanceState);
        updateView();
        setPinOnMap();

        ArrayList<Timestamp> arr = searched_item.getDates();
        List<Calendar> calendars = new ArrayList<>();
        rent_calendar.setDisabledDays(calendars);
        rent_calendar.setMinimumDate(Calendar.getInstance());

        getCounter();

        rent_calendar.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                date = clickedDayCalendar.get(Calendar.DAY_OF_MONTH) + "/" + clickedDayCalendar.get(Calendar.MONTH) + "/" + clickedDayCalendar.get(Calendar.YEAR);
                Log.d("Pttt", "Calendar clicked dara: " + clickedDayCalendar.get(Calendar.DAY_OF_MONTH) + "/" + clickedDayCalendar.get(Calendar.MONTH) + "/" + clickedDayCalendar.get(Calendar.YEAR));
            }
        });

        rent_BTN_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date != null) {
                    rentRequest();
                    Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please choose date", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void getCounter() {
        FirebaseFirestore.getInstance().collection("counters").document("counters").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        counter = ((Number) documentSnapshot.get("request_id_last")).intValue();
                    }
                });
    }

    private void rentRequest() {
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> temp = new HashMap<String, Object>();
                        temp.put("date", date);
                        temp.put("owner_address", (String) searched_item.getAddress());
                        temp.put("description", (String) searched_item.getDescription());
                        temp.put("part_name", (String) searched_item.getPartName());
                        temp.put("image_res", (String) searched_item.getImageRes());
                        temp.put("owner_id", (String) searched_item.getUserId());
                        temp.put("owner_name", (String) searched_item.getName());
                        temp.put("requester_id", FirebaseAuth.getInstance().getUid());
                        temp.put("requester_name", documentSnapshot.get("name"));
                        temp.put("accepted", "requesting");
                        counter++;
                        FirebaseFirestore.getInstance().collection("requests").document(String.valueOf(counter)).set(temp)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        updateCountersAndRequestLists((ArrayList<String>) documentSnapshot.get("request_list"), searched_item.getUserId());
                                        getFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment, new SearchFragment(), "ADD_EQUIPMENT_FRAGMENT").commit();
                                    }
                                });
                    }
                });

    }

    private void updateCountersAndRequestLists(ArrayList<String> owner_request_list, String owner_id) {

        FirebaseFirestore.getInstance().collection("counters").document("counters").update("request_id_last", (Number) counter)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Pttt", "Request id counter uploaded, new counter is: " + counter);
                    }
                });


        owner_request_list.add(String.valueOf(counter));
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).update("request_list", owner_request_list)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Pttt", "Requester request_list uploaded, new request_list is: " + owner_request_list.toString());
                    }
                });


        FirebaseFirestore.getInstance().collection("users").document(owner_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> temp_array = (ArrayList<String>) documentSnapshot.get("request_list");
                        temp_array.add(String.valueOf(counter));
                        FirebaseFirestore.getInstance().collection("users").document(owner_id).update("request_list", temp_array)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Pttt", "Owner request_list uploaded, new request_list is: " + temp_array.toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Pttt", "Could not upload Owner request_lis becase" + e.getMessage());
                            }
                        });

                    }
                });


    }


    private void initMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        rent_MAP_map.onCreate(mapViewBundle);

        rent_MAP_map.getMapAsync(this);

            /*rent_MAP_map.onCreate(savedInstanceState);
            rent_MAP_map.onResume();
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        rent_MAP_map.onSaveInstanceState(mapViewBundle);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(location).title("Marker"));
    }

    private void setPinOnMap() {
        rent_MAP_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                google_map = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                google_map.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                google_map.addMarker(new MarkerOptions().position(location).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
                google_map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    private void updateView() {
        rent_IMG_image.setImageBitmap(searched_item.getImageResBitmap());
        rent_LBL_part_name.setText(searched_item.getPartName());
        rent_LBL_description.setText(searched_item.getDescription());
        rent_LBL_measered_distance.setText(searched_item.getMeaseredDistance() + "Km");
        rent_LBL_address.setText(searched_item.getAddress());
    }

    private void parseBundle(Bundle bundle) {
        if (null != bundle) {
            json_string = bundle.getString("json_list");
            Log.d("Pttt,", "Bundle had :" + json_string);
            Gson gson = new Gson();
            searched_item = gson.fromJson(json_string, new TypeToken<SearchedListItem>() {
            }.getType());
            Log.d("Pttt", "List<HashMap<String,Object>>[0] = " + searched_item);
        }
    }

    private void findViews(View view) {
        rent_LBL_description = view.findViewById(R.id.rent_LBL_description);
        rent_LBL_measered_distance = view.findViewById(R.id.rent_LBL_measered_distance);
        rent_LBL_part_name = view.findViewById(R.id.rent_LBL_part_name);
        rent_IMG_image = view.findViewById(R.id.rent_IMG_image);
        rent_MAP_map = view.findViewById(R.id.rent_MAP_map);
        rent_BTN_rent = view.findViewById(R.id.rent_BTN_rent);
        rent_calendar = view.findViewById(R.id.rent_calendar);
        rent_LBL_address = view.findViewById(R.id.rent_LBL_address);
    }

    @Override
    public void onResume() {
        super.onResume();
        rent_MAP_map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        rent_MAP_map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rent_MAP_map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        rent_MAP_map.onLowMemory();
    }

}
