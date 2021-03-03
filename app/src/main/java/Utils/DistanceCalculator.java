package Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class DistanceCalculator {

    private double distance = 0;
    private double latitude_2 = 0, longitude_2 = 0;
    private double latitude_1 = 0, longitude_1 = 0;
    private boolean accomplished = false;
    private CallBack_Top call_back;

    private FusedLocationProviderClient fusedLocationClient;

    public DistanceCalculator() {
    }

    public void calucalteDistance(String address, Context context, Activity activity) {
        {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude_2 = location.getLatitude();
                                longitude_2 = location.getLongitude();
                                Log.d("Pttt", "Lat_2,Lon_2: " + location.getLatitude() + "," + location.getLongitude());
                                getMyLocation(context, address);
                                distanceMeasure(latitude_1, longitude_1, latitude_2, longitude_2);
                                accomplished = true;
                            } else {
                                Log.d("Pttt", "location_2 is null");
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Pttt", "Failed to get last location: " + e.getMessage());
                }

            });
        }
    }

    public boolean getAcompished() {
        return accomplished;
    }

    public double getDistance() {
        return distance;
    }

    public void getMyLocation(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(address, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            this.latitude_1 = addresses.get(0).getLatitude();
            this.longitude_1 = addresses.get(0).getLongitude();
            Log.d("Pttt", "Lat_1,Lon_1: " + latitude_1 + "," + longitude_1);
        }
    }

    public void setCalBackListener(CallBack_Top call_back_) {
        this.call_back = call_back_;
    }
    public interface CallBack_Top {
        void onCalculationFinished(double distance,double lat,double lng);
    }


    public void distanceMeasure(double latitude_1, double longitude_1, double latitude_2, double longitude_2) {

        double dLat = Math.toRadians(latitude_2 - latitude_1);
        double dLon = Math.toRadians(longitude_2 - longitude_1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitude_1)) * Math.cos(Math.toRadians(latitude_2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        distance = 6366 * c;
        if(call_back!=null) {
            call_back.onCalculationFinished(distance,longitude_1,latitude_1);
        }
        Log.d("Pttt", "measured distance: " + distance);
    }
}
