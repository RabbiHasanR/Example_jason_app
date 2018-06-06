package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    /** Query URL */
    private String mUrl;

    public EarthquakeLoader(Context context,String mUrl){
        super(context);
        this.mUrl=mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG,"onStartLoading");
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl==null){
            return null;
        }
        List<Earthquake> earthquakes=QueryUtils.fetchEarthquakeData(mUrl);
        Log.v(LOG_TAG,"loadInBackground");
        return earthquakes;
    }
}
