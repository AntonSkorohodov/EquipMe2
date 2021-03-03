package Class;


import android.graphics.Bitmap;

import com.google.android.gms.maps.MapView;
import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import Utils.Category;



public class Equipment {

    private String part_name,description,user_id,item_image,category;
    private List<Timestamp> not_avalible;
    private String price;

    public Equipment() {
        part_name = "null";
        description = "null";
        price = "";
        not_avalible = new ArrayList<Timestamp>();
        user_id = "";
    }

    public Equipment(String part_name, String description, String category, String price, String user_id, String item_image) {
        this.part_name = part_name;
        this.description = description;
        this.not_avalible = new ArrayList<Timestamp>();
        this.category = category;
        this.price = price;
        this.user_id = user_id;
        this.item_image = item_image;
    }

    public Map<String,Object> getEquipment(){
        Map<String,Object> equipment_map = new HashMap<String, Object>();
        equipment_map.put("part_name", part_name);
        equipment_map.put("description",description );
        equipment_map.put("user_id",user_id );
        equipment_map.put("not_avalible",not_avalible );
        equipment_map.put("category",category );
        equipment_map.put("item_image",item_image );
        equipment_map.put("price",price );
        return equipment_map;
    }

    public List<Timestamp> getNotAvalible(){
        return not_avalible;
    }

    public String getPrice(){
        return price;
    }

    public String getPart_name() {
        return part_name;
    }

    public String getDescription() {
        return description;
    }

    public String getTCategory() {
        return category;
    }

    public String getItemImage() { return item_image; }



}

