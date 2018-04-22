package com.amr.atlas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public String place;

    public boolean isCountry;
    public HashMap<String, Double> latlng;
    public static final String API = "AIzaSyB9V9K50LVgdXVwJHHcyE1rwDkUetHW2KA";

    class SearchLocation extends AsyncTask<String, Void, HashMap<String, Double>> {

        @Override
        protected HashMap<String, Double> doInBackground(String... places) {

            String place = places[0].replaceAll(" ", "+");
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+place+"&key="+API);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();

                String json = read(in);
                JSONObject jsonObject = new JSONObject(json);
                JSONObject geometry = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                MapsActivity.this.isCountry = ((jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(0).getJSONArray("types").getString(0).equals("country"))?true:false);

                HashMap<String, Double> latlong = new HashMap<String, Double>();
                latlong.put("latitude", location.getDouble("lat"));
                latlong.put("longitude", location.getDouble("lng"));

                return latlong;

            } catch (Exception e) {
                e.printStackTrace();
                Log.w("amr", "unable to load 1");
            }
            return null;
        }

        public String read(InputStream in) throws Exception {
            InputStreamReader reader = new InputStreamReader(in);
            String content = "";
            int data = reader.read();

            while(data != -1) {
                content += (char) data;
                data = reader.read();
            }
            return content;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place = getIntent().getExtras().getString("place", null);
        try {
            if(place != null) {
                latlng = new SearchLocation().execute(place).get();
            }else{
                latlng = null;
            }
        } catch (Exception e) {
            latlng = null;
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MainActivity.MapType);
        if(latlng != null) {
            LatLng target = new LatLng(latlng.get("latitude"), latlng.get("longitude"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, (isCountry ? 6:12)));
        }else {
            if(place != null) {
                Toast.makeText(this, "cannot find the location " + place, Toast.LENGTH_LONG).show();
            }
        }
    }

}
