package com.example.equipme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.equipme.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import Fragments.CartFragment;
import Fragments.MyEquipmentRequestFragment;
import Fragments.StoreFragment;
import Fragments.ProfileFragment;
import Fragments.SearchFragment;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer_layout;
    NavigationView navigation_view;
    Toolbar toolbar;
    TextView header_LBL_name;
    private String name,email,user_image_name;
    Bitmap user_image;
    ImageView header_IMG_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        closeDrawerWithBackButton();
        navigation_view.setNavigationItemSelectedListener(this);
        getUserData();

        getSupportFragmentManager().beginTransaction().add(R.id.main_CNT_fragment,new SearchFragment()).commit();

    }



    private void closeDrawerWithBackButton() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer_layout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START);
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment,new SearchFragment(),"SEARCH_FRAGMENT").commit();
        }
    }

    private void findViews() {
        drawer_layout = findViewById(R.id.main_LYT_drawer);
        navigation_view = findViewById(R.id.main_NWV_menu);
        toolbar = findViewById(R.id.main_TLB_toolbar);
        View header_view = navigation_view.getHeaderView(0);
        header_LBL_name = header_view.findViewById(R.id.header_LBL_name);
        header_IMG_user = header_view.findViewById(R.id.header_IMG_user);


    }

    private void getUserData() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    name = documentSnapshot.get("name").toString();
                    header_LBL_name.setText(name);
                    email = documentSnapshot.get("email").toString();
                    try{ user_image_name = documentSnapshot.get("profile_img").toString(); } catch (Exception e){Log.d("Pttt","user dont have image");}

                    //get Image from FireBase Storage
                    try {
                    StorageReference storage_reference = FirebaseStorage.getInstance().getReference("users_image").child(user_image_name);
                        File file = File.createTempFile("image", "jpg");
                        storage_reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if (taskSnapshot != null) {
                                    Log.d("Pttt", "decode file");
                                    user_image = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    header_IMG_user.setImageBitmap(user_image);
                                    Log.d("Pttt", "User header image set");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Pttt","Could not fetch data from storage: "+e.getMessage());
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("Pttt", "Main Activity get_image exception: "+e.getMessage());
                    }

                }else{
                    Toast.makeText(MainActivity.this,"User does not have any data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Could not read data: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("Pttt","Navigation Item Clicked");
        switch (item.getItemId()){
            case R.id.menu_ITM_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment,new ProfileFragment(),"PROFILE_FRAGMENT").commit();
                drawer_layout.closeDrawers();
                break;
            case R.id.menu_ITM_store:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment,new StoreFragment(),"STORE_FRAGMEMT").commit();
                drawer_layout.closeDrawers();
                break;
            case R.id.menu_ITM_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment,new SearchFragment(),"SEARCH_FRAGMENT").commit();
                drawer_layout.closeDrawers();
                break;
            case R.id.menu_ITM_request_my_equipment:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment,new MyEquipmentRequestFragment(),"CART_FRAGMENT").commit();
                drawer_layout.closeDrawers();
                break;
            case R.id.menu_ITM_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment,new CartFragment(),"CART_FRAGMENT").commit();
                drawer_layout.closeDrawers();
                break;
            case R.id.menu_ITM_logout:
                super.onBackPressed();
                break;
        }
        return true;
    }


}