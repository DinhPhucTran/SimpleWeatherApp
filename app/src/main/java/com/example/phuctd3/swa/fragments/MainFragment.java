package com.example.phuctd3.swa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuctd3.swa.HourDetail;
import com.example.phuctd3.swa.HourlyListAdapter;
import com.example.phuctd3.swa.MainActivity;
import com.example.phuctd3.swa.R;
import com.example.phuctd3.swa.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private HourlyListAdapter mAdapter;
    private TextView mCurrentTemp;
    private TextView mLowTemp;
    private TextView mHighTemp;
    private ImageView mCurrentIcon;
    private TextView mCurrentSummary;
    private TextView mCurrentTime;

    private TextView mCurrentHumidity;
    private TextView mCurrentPressure;
    private TextView mCurrentWind;
    private TextView mCurrentCloud;
    private TextView mCurrentUV;
    private TextView mCurrentOzone;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mAdapter = new HourlyListAdapter();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.h_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mCurrentTemp = (TextView) view.findViewById(R.id.current_temp);
        mLowTemp = (TextView) view.findViewById(R.id.low_temp);
        mHighTemp = (TextView) view.findViewById(R.id.high_temp);
        mCurrentIcon = (ImageView) view.findViewById(R.id.current_icon);
        mCurrentSummary = (TextView) view.findViewById(R.id.current_summary);
        mCurrentTime = (TextView) view.findViewById(R.id.current_time);

        mCurrentHumidity = (TextView) view.findViewById(R.id.current_humidity);
        mCurrentPressure = (TextView) view.findViewById(R.id.current_pressure);
        mCurrentWind = (TextView) view.findViewById(R.id.current_wind);
        mCurrentCloud = (TextView) view.findViewById(R.id.current_cloud);
        mCurrentUV = (TextView) view.findViewById(R.id.current_uv);
        mCurrentOzone = (TextView) view.findViewById(R.id.current_ozone);

        return view;
    }

    public void setHourlyList(List<HourDetail> list) {
        if(mAdapter != null)
        mAdapter.setItems(list);
    }

    public void onWeatherLoaded(String json) {
        Toast.makeText(getContext(), "Json loaded", Toast.LENGTH_SHORT).show();
        try {
            List<HourDetail> hourlyList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            JSONObject currentlyObject = jsonObject.getJSONObject("currently");
            String temp = currentlyObject.getInt("temperature") + "";
            mCurrentTemp.setText(temp);
            mCurrentSummary.setText(Utils.getSummaryVi(currentlyObject.getString("summary")));
            mCurrentIcon.setImageResource(Utils.getWeatherIcon(currentlyObject.getString("icon")));
            mCurrentTime.setText(new SimpleDateFormat("EEEE dd/MM, HH:mm", new Locale("vi", "VN")).format(new Date(currentlyObject.getLong("time") * 1000)));

            JSONArray dailyArr = jsonObject.getJSONObject("daily").getJSONArray("data");
            JSONObject todayObject = dailyArr.getJSONObject(0);
            mLowTemp.setText(todayObject.getInt("temperatureMin") + "\u00B0");
            mHighTemp.setText(todayObject.getInt("temperatureMax") + "\u00B0");

            JSONArray hourlyArr = jsonObject.getJSONObject("hourly").getJSONArray("data");
            String t = "";
            String summary = "";
            String time = "";
            String icon = "";
            SimpleDateFormat sdf = new SimpleDateFormat("HH'h'\ndd/MM");
            for(int i = 0; i < hourlyArr.length(); i++) {
                try {
                    JSONObject object = hourlyArr.getJSONObject(i);
                    t = object.getInt("temperature") + "\u00B0";
                    summary = Utils.getSummaryVi(object.getString("summary"));
                    time = sdf.format(new Date(object.getLong("time") * 1000));
                    icon = object.getString("icon");
                    hourlyList.add(new HourDetail(t, summary, time, icon));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setHourlyList(hourlyList);

            mCurrentHumidity.setText((currentlyObject.getDouble("humidity") * 100) + "%");
            mCurrentWind.setText(currentlyObject.getDouble("windSpeed") + " km/h");
            mCurrentPressure.setText(currentlyObject.getDouble("pressure") + "");
            mCurrentCloud.setText((currentlyObject.getDouble("cloudCover") * 100) + "%");
            mCurrentUV.setText(currentlyObject.getInt("uvIndex") + "");
            mCurrentOzone.setText(currentlyObject.getDouble("ozone") + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
