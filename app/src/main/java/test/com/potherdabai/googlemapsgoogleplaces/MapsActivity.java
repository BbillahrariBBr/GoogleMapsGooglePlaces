package test.com.potherdabai.googlemapsgoogleplaces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is Ready here");
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            
        }

    }

    private static final String TAG = "MapsActivity";

    private  static final String FINE_LOCATION =   Manifest.permission.ACCESS_FINE_LOCATION;
    private static  final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static  final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private  static  final float DEFAULT_ZOOM = 15f;
    //var
    private  Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLccationPermission();
    }

    //get location method
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                       if (task.isSuccessful()){
                           Log.d(TAG, "onComplete: found location");
                           Location currentLocationn = (Location) task.getResult();
                           moveCamera(new LatLng(currentLocationn.getLatitude(),currentLocationn.getLongitude()),
                                   DEFAULT_ZOOM);
                       }
                       else {
                           Log.d(TAG, "onComplete: currrent location is null");
                           Toast.makeText(MapsActivity.this,
                                   "Unable to get current Location",Toast.LENGTH_SHORT).show();


                       }
                    }
                });
            }

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: "+ e.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, Float zoom){
        Log.d(TAG, "moveCamere: Moving Camera to: lat: "+latLng.latitude + ", lng: " +latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));


    }

    //create method init Map
    private void initMap(){
        Log.d(TAG, "initMap: initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //call  OnMap ready call back method

        mapFragment.getMapAsync(MapsActivity.this);

        //if implements onMapreadycallback then not need this, otherwise need it.

//                new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                mMap = googleMap;
//            }
//        });
    }

    //permission Check
    private void getLccationPermission(){
        Log.d(TAG, "getLccationPermission: getting location permission");
        String[] permissions = {FINE_LOCATION,COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }
            else {
                ActivityCompat.requestPermissions(this,permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this,permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //OVERride request permission result press ctrl+ O
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Called");

        mLocationPermissionGranted =false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0)

                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                         mLocationPermissionGranted =false;
                            Log.d(TAG, "onRequestPermissionsResult: permission Failed");
                         return;
                         }
                    }
                Log.d(TAG, "onRequestPermissionsResult: permission Granted");
                    mLocationPermissionGranted =true;
                    //initialise Our Map
                    initMap();
                }
            }
        }


}
