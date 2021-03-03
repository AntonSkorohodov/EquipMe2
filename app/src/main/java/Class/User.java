package Class;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name,email,phone,address,profile_img;

    public User(){
        name = "";
        email = "";
        phone = "";
        address = "";
        profile_img = "";
    }

    public User(HashMap<String ,Object> user){
        setName(user);
        setEmail(user);
        setPhone(user);
        setAddress(user);
        setProfileImg(user);
    }

    public void setUser(HashMap<String ,Object> user){
        setName(user);
        setEmail(user);
        setPhone(user);
        setAddress(user);
        setProfileImg(user);
    }

    private void setName(HashMap<String ,Object> user) {
        try {
            name = user.get("name").toString();
        }catch (Exception e){
            Log.d("Pttt:","Exception:user don't have name");
            name = "";
        }
    }

    private void setEmail(HashMap<String ,Object> user) {
        try {
            email = user.get("email").toString();
        }catch (Exception e){
            Log.d("Pttt:","Exception:user don't have email");
            email = "";
        }
    }

    private void setPhone(HashMap<String ,Object> user) {
        try {
            phone = user.get("phone").toString();
        }catch (Exception e){
            Log.d("Pttt:","Exception: user don't have phone");
            phone = "";
        }
    }

    private void setAddress(HashMap<String ,Object> user) {
        try {
            address = user.get("address").toString();
        }catch (Exception e){
            Log.d("Pttt:","Exception: user don't have phone");
            address = "";
        }
    }

    private void setProfileImg(HashMap<String ,Object> user) {
        try {
            profile_img = user.get("profile_img").toString();
        }catch (Exception e){
            Log.d("Pttt:","Exception: user don't have profile image");
            profile_img = "";
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getProfileImage() {
        return profile_img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public Map getUser(){
        Map<String,Object> user = new HashMap<>();
        user.put("name",name);
        user.put("email",email);
        user.put("phone",phone);
        user.put("address",address);
        user.put("profile_img",profile_img);
        return user;
    }

}
