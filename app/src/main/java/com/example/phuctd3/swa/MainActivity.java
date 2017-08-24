package com.example.phuctd3.swa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuctd3.swa.fragments.MainFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse, LocationListener {

    private JsonTask mJsonTask;
    private static List<HourDetail> mHourlyList;

    private LocationManager mLocationManager;
    private Location mLastLocation;

    private MainFragment mMainFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJsonTask = new JsonTask();
        mJsonTask.delegate = this;
        mHourlyList = new ArrayList<>();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
        mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (mLastLocation != null) {
            onLocationChanged(mLastLocation);
        }

        mMainFragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, mMainFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //mJsonTask.execute("https://api.darksky.net/forecast/2383cc9845f31fa5bc857dd1be0dc202/37.8267,-122.4233?units=ca&exclude=[minutely]");

    }

    @Override
    public void onPause() {
        mLocationManager.removeUpdates(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onJsonLoaded(String json) {
        Toast.makeText(this, "Json loaded", Toast.LENGTH_SHORT).show();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject currentlyObject = jsonObject.getJSONObject("currently");
            String temp = currentlyObject.get("temperature") + " C";

            JSONArray hourlyArr = jsonObject.getJSONObject("hourly").getJSONArray("data");
            String t = "";
            String summary = "";
            String time = "";
            String icon = "";
            SimpleDateFormat sdf = new SimpleDateFormat("HH'h'\ndd/MM");
            for(int i = 0; i < hourlyArr.length(); i++) {
                try {
                    JSONObject object = hourlyArr.getJSONObject(i);
                    t = object.getInt("temperature") + "";
                    summary = Utils.getSummaryVi(object.getString("summary"));
                    time = sdf.format(new Date(object.getLong("time") * 1000));
                    icon = object.getString("icon");
                    mHourlyList.add(new HourDetail(t, summary, time, icon));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mMainFragment.setHourlyList(mHourlyList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<HourDetail> getHourlyList() {
        return mHourlyList;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(Utils.isBetterLocation(location, mLastLocation)) {
            mLastLocation = location;
        }
        restartJsonTask(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void restartJsonTask(double lat, double lng) {
        try {
            mJsonTask.cancel(true);
            mJsonTask = new JsonTask();
            mJsonTask.delegate = this;
            mJsonTask.execute("https://api.darksky.net/forecast/2383cc9845f31fa5bc857dd1be0dc202/" + lat + "," + lng + "?units=ca&exclude=[minutely]");
        } catch (Exception e) {
            Log.e("----> ", e.toString());
        }
    }
}
