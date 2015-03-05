package com.ekeitho.shake;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by ekeitho on 2/18/15.
 */
public class CustomMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap googleMap;

    ParseUser parse_user = ParseUser.getCurrentUser();

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("MyApp", "Connected to Google Map server.");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        parse_user.put("location", new ParseGeoPoint(loc.latitude, loc.longitude));
        parse_user.saveInBackground();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        googleMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title("Me!"));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {

        Log.d("MyApp", "Build google api client.");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("MyApp", "Begin on activity");
        googleMap = getMap();
        buildGoogleApiClient();
        Log.d("MyApp", "End on activity created.");
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}