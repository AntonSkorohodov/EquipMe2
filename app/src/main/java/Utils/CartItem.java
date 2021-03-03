package Utils;

import android.graphics.Bitmap;

public class CartItem {
    private String part_name,description,owner_id,requester_id,image_res,date,accepted,owner_name,requester_name,address;
    private Bitmap image_res_bit_map;


    public CartItem(){    }

    public String getOwnerAddress(){return address;}

    public CartItem setOwnerAddress(String address) {
        this.address = address;
        return this;
    }

    public String getImageRes() {
        return image_res;
    }

    public CartItem setImageRes(String image_res) {
        this.image_res = image_res;
        return this;
    }

    public Bitmap getImageResBitmap() {
        return image_res_bit_map;
    }

    public CartItem setImageResBitmap(Bitmap image_res_bit_map) {
        this.image_res_bit_map = image_res_bit_map;
        return this;
    }

    public String getPartName() {
        return part_name;
    }

    public CartItem setPartName(String part_name) {
        this.part_name = part_name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CartItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDate() {
        return date;
    }

    public CartItem setDate(String date) {
        this.date = date;
        return this;
    }

    public String getRequesterName() {
        return requester_name;
    }

    public CartItem setRequesterName(String requester_name) {
        this.requester_name = requester_name;
        return this;
    }

    public String getAccpeted() { return accepted; }

    public CartItem setAccepted(String accepted){
        this.accepted = accepted;
        return this;
    }

    public String getOwnerId() {return owner_id;}

    public CartItem setOwnerId(String owner_id){
        this.owner_id = owner_id;
        return this;
    }

    public String getRequesterId() { return requester_id; }

    public CartItem setRequesterId(String requester_id) {
        this.requester_id = requester_id;
        return this;
    }

    public String getOwnerName() { return owner_name; }

    public CartItem setOwnerName(String owner_name){
        this.owner_name = owner_name;
        return this;
    }

}
