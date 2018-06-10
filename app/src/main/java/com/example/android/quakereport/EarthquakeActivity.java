/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private EarthquakeAdapter earthquakeAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingSpinner;
    private ListView earthquakeListView;
    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        findView();
        checkNetworkConnectivity();
        setEarthquakeInfo();
        //create instance for EarthquakeAsyncTask class which extends AsyncTask<E> clas
        /*EarthquakeAsyncTask task=new  EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);*/

    }

    public void findView(){
        earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mLoadingSpinner=(ProgressBar)findViewById(R.id.loading_spinner);
    }

    /**
     * Update the UI with the given earthquake information.
     */
    public void setEarthquakeInfo(){
        // Find a reference to the {@link ListView} in the layout
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        // Create a new adapter that takes an empty list of earthquakes as input
        earthquakeAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(earthquakeAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = earthquakeAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getEarthquakeDetailUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"onCreateLoader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        //set progress bar visibility gone when background loading finish
        mLoadingSpinner.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);
        // Clear the adapter of previous earthquake data
        earthquakeAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            earthquakeAdapter.addAll(earthquakes);
            Log.v(LOG_TAG,"onLoadFinished");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
        earthquakeAdapter.clear();
        Log.v(LOG_TAG,"onLoadReset");
    }

//without loader using AsyncTask<E> class for background thread

   /* private class  EarthquakeAsyncTask extends AsyncTask<String,Void,List<Earthquake>> {
        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            if(urls.length<1 || urls[0]==null){
                return null;
            }
             List<Earthquake> earthquakeInfo=QueryUtils.fetchEarthquakeData(USGS_REQUEST_URL);
            return earthquakeInfo;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakeInfo) {
            if(earthquakeInfo==null){
                return;
            }
            earthquakeAdapter.clear();
            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (earthquakeInfo != null && !earthquakeInfo.isEmpty()) {
                earthquakeAdapter.addAll(earthquakeInfo);
            }

        }
    }*/

   public void checkNetworkConnectivity(){
       ConnectivityManager cm =
               (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

       NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
       boolean isConnected = activeNetwork != null &&
               activeNetwork.isConnectedOrConnecting();
       //boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
       if(isConnected ){
           // Get a reference to the LoaderManager, in order to interact with loaders.
           LoaderManager loaderManager = getLoaderManager();

           // Initialize the loader. Pass in the int ID constant defined above and pass in null for
           // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
           // because this activity implements the LoaderCallbacks interface).
           loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
           Log.v(LOG_TAG,"initLoader");
       }
       else{
           mEmptyStateTextView.setText(R.string.no_internet);
           mLoadingSpinner.setVisibility(View.GONE);
       }
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
