package com.example.phuctd3.swa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity implements AsyncResponse{

    private TextView mTextMessage;


    private JsonTask mJsonTask;

    private MainFragment mMainFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mJsonTask = new JsonTask();
        mJsonTask.delegate = this;

        mJsonTask.execute("https://api.darksky.net/forecast/2383cc9845f31fa5bc857dd1be0dc202/37.8267,-122.4233?units=ca&exclude=[minutely]");

    }

    @Override
    public void onJsonLoaded(String json) {
        Toast.makeText(this, "Json loaded", Toast.LENGTH_SHORT).show();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject currentlyObject = jsonObject.getJSONObject("currently");
            String temp = currentlyObject.get("temperature") + " C";
            mTextMessage.setText(temp);

            List<HourDetail> hourlyList = new ArrayList<>();
            JSONArray hourlyArr = jsonObject.getJSONObject("hourly").getJSONArray("data");
            String t = "";
            String summary = "";
            String time = "";
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            for(int i = 0; i < hourlyArr.length(); i++) {
                try {
                    JSONObject object = hourlyArr.getJSONObject(i);
                    t = object.getString("temperature");
                    summary = object.getString("summary");
                    time = sdf.format(new Date(object.getLong("time")));
                    hourlyList.add(new HourDetail(t, summary, time));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mMainFragment.setHourlyList(hourlyList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
