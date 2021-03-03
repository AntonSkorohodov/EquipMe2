package Utils;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class StoreItem {
    private String part_name,description,measered_distance,user_id,image_res,address;
    private LatLng lat_lng;
    private Bitmap image_res_bit_map;
    private ArrayList<Timestamp> date_arr;

    public StoreItem(){    }

    public ArrayList<Timestamp> getDates() { return date_arr; }

    public StoreItem setDates(ArrayList<Timestamp> date_arr) {
        this.date_arr = date_arr;
        return this;
    }

    public String getAddress() { return address; }

    public StoreItem setAddress(String address) {
        this.address = address;
        return this;
    }

    public LatLng getLat_lng() { return lat_lng; }

    public StoreItem setLat_lng(LatLng lat_lng) {
        this.lat_lng = lat_lng;
        return this;
    }

    public String getImageRes() {
        return image_res;
    }

    public StoreItem setImageRes(String image_res) {
        this.image_res = image_res;
        return this;
    }

    public Bitmap getImageResBitmap() {
        return image_res_bit_map;
    }

    public StoreItem setImageResBitmap(Bitmap image_res_bit_map) {
        this.image_res_bit_map = image_res_bit_map;
        return this;
    }

    public String getPartName() {
        return part_name;
    }

    public StoreItem setPartName(String part_name) {
        this.part_name = part_name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public StoreItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMeaseredDistance() {
        return measered_distance;
    }

    public StoreItem setMeaseredDistance(String measered_distance) {
        this.measered_distance = measered_distance;
        return this;
    }

    public String getUserId() {
        return user_id;
    }

    public StoreItem setUserId(String user_id) {
        this.user_id = user_id;
        return this;
    }
}
