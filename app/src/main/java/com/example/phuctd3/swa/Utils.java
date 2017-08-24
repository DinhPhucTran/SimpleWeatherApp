package com.example.phuctd3.swa;

import android.location.Location;

/**
 * Created by PhucTD3 on 8/23/2017.
 */

public class Utils {
    public static String getSummaryVi(String summary) {
        String vi = "";
        if("Clear".equalsIgnoreCase(summary)) {
            vi = "Trời quang mây";
        } else if("Partly Cloudy".equalsIgnoreCase(summary)) {
            vi = "Có mây";
        } else if("Mostly Cloudy".equalsIgnoreCase(summary)) {
            vi = "Nhiều mây";
        } else if("Rain".equalsIgnoreCase(summary)) {
            vi = "Mưa";
        } else if("Light Rain".equalsIgnoreCase(summary)) {
            vi = "Mưa nhẹ";
        } else if("Drizzle".equalsIgnoreCase(summary)) {
            vi = "";
        } else if("".equalsIgnoreCase(summary)) {
            vi = "";
        } else if("".equalsIgnoreCase(summary)) {
            vi = "";
        } else if("Overcast".equalsIgnoreCase(summary)) {
            vi = "???";
        } else {
            vi = summary;
        }
        return vi;
    }

    public static int getWeatherIcon(String icon) {
        switch (icon) {
            case "wind":
                return R.drawable.wind;
            case "clear-day":
                return R.drawable.clear_day;
            case "clear-night":
                return R.drawable.clear_night;
            case "partly-cloudy-day":
                return R.drawable.partly_cloudy_day;
            case "partly-cloudy-night":
                return R.drawable.partly_cloudy_night;
            case "cloudy":
                return R.drawable.cloudy;
            case "fog":
                return R.drawable.fog;
            case "sleet":
                return R.drawable.sleet;
            case "snow":
                return R.drawable.snow;
            case "rain":
                return R.drawable.rain;
        }
        return R.drawable.clear_day;
    }

    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > 120000;
        boolean isSignificantlyOlder = timeDelta < -120000;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
