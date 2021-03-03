package Utils;

import android.graphics.Bitmap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class SearchedListItem {
    private String part_name,description,measered_distance,user_id,image_res,address,name,date;
    private LatLng lat_lng;
    private Bitmap image_res_bit_map;
    private ArrayList<Timestamp> date_arr;

    public SearchedListItem() { }

    public ArrayList<Timestamp> getDates() { return date_arr; }

    public SearchedListItem setDates(ArrayList<Timestamp> date_arr) {
        this.date_arr = date_arr;
        return this;
    }

    public String getAddress() { return address; }

    public SearchedListItem setAddress(String address) {
        this.address = address;
        return this;
    }

    public LatLng getLat_lng() { return lat_lng; }

    public SearchedListItem setLat_lng(LatLng lat_lng) {
        this.lat_lng = lat_lng;
        return this;
    }

    public String getImageRes() {
        return image_res;
    }

    public SearchedListItem setImageRes(String image_res) {
        this.image_res = image_res;
        return this;
    }

    public Bitmap getImageResBitmap() {
        return image_res_bit_map;
    }

    public SearchedListItem setImageResBitmap(Bitmap image_res_bit_map) {
        this.image_res_bit_map = image_res_bit_map;
        return this;
    }

    public String getPartName() {
        return part_name;
    }

    public SearchedListItem setPartName(String part_name) {
        this.part_name = part_name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SearchedListItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMeaseredDistance() {
        return measered_distance;
    }

    public SearchedListItem setMeaseredDistance(String measered_distance) {
        this.measered_distance = measered_distance;
        return this;
    }

    public String getUserId() {
        return user_id;
    }

    public SearchedListItem setUserId(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getName(){ return name;}

    public SearchedListItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getDate() {return date;}

    public SearchedListItem setDate(String date){
        this.date = date;
        return this;
    }
}
