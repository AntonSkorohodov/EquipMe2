package Utils;

import android.graphics.Bitmap;

public class MyEquipmentRequestItem {
    private String part_name,description,owner_id,requester_id,image_res,date,accepted,owner_name,requester_name,owner_address;
    private Bitmap image_res_bit_map;


    public MyEquipmentRequestItem(){    }

    public String getImageRes() {
        return image_res;
    }

    public MyEquipmentRequestItem setImageRes(String image_res) {
        this.image_res = image_res;
        return this;
    }

    public Bitmap getImageResBitmap() {
        return image_res_bit_map;
    }

    public MyEquipmentRequestItem setImageResBitmap(Bitmap image_res_bit_map) {
        this.image_res_bit_map = image_res_bit_map;
        return this;
    }

    public String getPartName() {
        return part_name;
    }

    public MyEquipmentRequestItem setPartName(String part_name) {
        this.part_name = part_name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MyEquipmentRequestItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDate() {
        return date;
    }

    public MyEquipmentRequestItem setDate(String date) {
        this.date = date;
        return this;
    }

    public String getRequesterName() {
        return requester_name;
    }

    public MyEquipmentRequestItem setRequesterName(String requester_name) {
        this.requester_name = requester_name;
        return this;
    }

    public String getAccpeted() { return accepted; }

    public MyEquipmentRequestItem setAccepted(String accepted){
        this.accepted = accepted;
        return this;
    }

    public String getOwnerId() {return owner_id;}

    public MyEquipmentRequestItem setOwnerId(String owner_id){
        this.owner_id = owner_id;
        return this;
    }

    public String getRequesterId() { return requester_id; }

    public MyEquipmentRequestItem setRequesterId(String requester_id) {
        this.requester_id = requester_id;
        return this;
    }

    public String getOwnerName() { return owner_name; }

    public MyEquipmentRequestItem setOwnerName(String owner_name){
        this.owner_name = owner_name;
        return this;
    }

    public String getOwnerAddress() { return owner_address;}


    public MyEquipmentRequestItem setOwnerAddress(String owner_address) {
        this.owner_address = owner_address;
        return this;
    }
}
