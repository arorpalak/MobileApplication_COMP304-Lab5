package com.example.palakarora_shivangishandilya_comp304_lab5;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng mapLocation;
    private LatLng landMarkLocation;
    private LocationRequest mLocationRequest;
    private TextView mTextView;
    private List<Marker> mMarkers = new ArrayList<>();
    private Polyline mPolyline;

    public static final String TAG = "MapActivity";
    private static final String ERROR_MSG = "Google Play services are unavailable.";
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    private Landmarks landmarks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mTextView = findViewById(R.id.myLocationText);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GoogleApiAvailability availability =
                GoogleApiAvailability.getInstance();

        int result = availability.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (!availability.isUserResolvableError(result)) {
                Toast.makeText(this, ERROR_MSG, Toast.LENGTH_LONG).show();
            }
        }

        mLocationRequest = new LocationRequest()
                .setInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Intent in = getIntent();
        int id = in.getIntExtra(Landmarks.ID_EXTRA, -1);

        if (id == -1) {
            Toast.makeText(this,
                    "invalid landmark!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        landmarks = Landmarks.getLandmarkById(id);

        if (landmarks != null) {
            mapLocation = landmarks.getLatLng();
        } else finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void updateTextView(Location location) {
        String latLongString = "Location Not Found";
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = "Lat:" + lat + "\nLong:" + lng;
        }

        String address = geocodeLocation(location);

        String outputText = "Current Location:\n" + latLongString;
        if (!address.isEmpty())
            outputText += "\n\n" + address;

        mTextView.setText(outputText);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType((GoogleMap.MAP_TYPE_SATELLITE));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.CYAN)
                .geodesic(true);
        mPolyline = mMap.addPolyline(polylineOptions);
    }
    LocationCallback mLocationCallback = new LocationCallback() {
        //called when device location information is available.
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {
                updateTextView(location);
            }
            if (location != null) {
                updateTextView(location);
                if (mMap != null) {
                    LatLng latLng = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    Calendar c = Calendar.getInstance();
                    String dateTime = DateFormat.format("MM/dd/yyyy HH:mm:ss",
                            c.getTime()).toString();

                    int markerNumber = mMarkers.size()+1;
                    mMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(mapLocation)
                            .title(dateTime)
                            .snippet("Marker #" + markerNumber +
                                    " @ " + dateTime)));
                    List<LatLng> points = mPolyline.getPoints();
                    points.add(latLng);
                    mPolyline.setPoints(points);
                }
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();

        int permission = ActivityCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION);

        if (permission == PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, locationSettingsResponse -> {
            requestLocationUpdates();
        });

        task.addOnFailureListener(this, e -> {
            int statusCode = ((ApiException) e).getStatusCode();

            switch (statusCode) {
                case CommonStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e(TAG, "Location Settings resolution failed.", sendEx);
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.d(TAG, "Location Settings can't be resolved.");
                    requestLocationUpdates();
                    break;
            }
        });
    }
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                        == PERMISSION_GRANTED) {

            FusedLocationProviderClient fusedLocationClient
                    = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }
    private void getLastLocation() {
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                        == PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            updateTextView(location);
                        }
                    });
        }
    }
    private String geocodeLocation(Location location) {
        String returnString = "";

        if (location == null) {
            Log.d(TAG, "No Location to Geocode");
            return returnString;
        }

        if (!Geocoder.isPresent()) {
            Log.e(TAG, "No Geocoder Available");
            return returnString;
        } else {
            Geocoder gc = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses
                        = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(),
                        1); // One Result

                StringBuilder sb = new StringBuilder();

                if (addresses.size() > 0) {
                    Address address = addresses.get(0);

                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb.append(address.getAddressLine(i)).append("\n");

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                }
                returnString = sb.toString();
            } catch (IOException e) {
                Log.e(TAG, "I/O Error Geocoding.", e);
            }
            return returnString;
        }
    }
    public void setMapLocation(LatLng newLatLng)
    {
        mapLocation = newLatLng;
    }
    public void setLandMarkLocations(LatLng newLatLng)
    {
        landMarkLocation = newLatLng;
    }

}